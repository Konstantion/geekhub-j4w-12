package com.konstantion.api.web.order;

import com.github.javafaker.Faker;
import com.konstantion.bill.Bill;
import com.konstantion.bill.BillPort;
import com.konstantion.dto.order.converter.OrderMapper;
import com.konstantion.dto.order.dto.OrderDto;
import com.konstantion.dto.order.dto.OrderProductsRequestDto;
import com.konstantion.jwt.JwtService;
import com.konstantion.order.Order;
import com.konstantion.order.OrderPort;
import com.konstantion.product.Product;
import com.konstantion.product.ProductPort;
import com.konstantion.response.ResponseDto;
import com.konstantion.table.Table;
import com.konstantion.table.TablePort;
import com.konstantion.table.TableType;
import com.konstantion.testcontainers.configuration.DatabaseContainer;
import com.konstantion.testcontainers.configuration.DatabaseTestConfiguration;
import com.konstantion.user.Permission;
import com.konstantion.user.Role;
import com.konstantion.user.User;
import com.konstantion.user.UserPort;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.konstantion.utils.EntityNameConstants.*;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Testcontainers
@ContextConfiguration(classes = {DatabaseTestConfiguration.class})
@ActiveProfiles("test")
class OrderControllerTest {
    @ClassRule
    @Container
    public static PostgreSQLContainer<DatabaseContainer> postgresSQLContainer = DatabaseContainer.getInstance();
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private TablePort tablePort;
    @Autowired
    private UserPort userPort;
    @Autowired
    private ProductPort productPort;
    @Autowired
    private OrderPort orderPort;
    @Autowired
    private BillPort billPort;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    private String jwtToken;
    Faker faker;
    private static final String API_URL = "/web-api/orders";

    @BeforeEach
    void setUp() {
        faker = new Faker();
        User waiter = User.builder()
                .email(faker.internet().emailAddress())
                .active(true)
                .password(passwordEncoder.encode("test"))
                .roles(Role.getWaiterRole())
                .permissions(Permission.getDefaultWaiterPermission())
                .build();
        userPort.save(waiter);
        Map<String, Object> extraClaim = Map.of(ENTITY, USER);
        jwtToken = jwtService.generateToken(extraClaim, waiter);
    }

    @AfterEach
    void cleanUp() {
        tablePort.deleteAll();
        userPort.deleteAll();
        orderPort.deleteAll();
        billPort.deleteAll();
        productPort.deleteAll();
    }

    @Test
    void shouldReturnForbiddenWhenUnauthorizedUser() {
        webTestClient.get()
                .uri(API_URL)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void shouldReturnActiveOrderWhenGetAllActiveOrder() {
        Order first = Order.builder().active(true).build();
        Order second = Order.builder().active(true).build();
        Order third = Order.builder().active(false).build();
        orderPort.save(first);
        orderPort.save(second);
        orderPort.save(third);

        EntityExchangeResult<ResponseDto<List<OrderDto>>> result = webTestClient.get()
                .uri(API_URL)
                .header(HttpHeaders.AUTHORIZATION, format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ResponseDto<List<OrderDto>>>() {
                })
                .returnResult();

        List<OrderDto> orderDtos = result.getResponseBody()
                .data().get(ORDERS);

        assertThat(orderDtos)
                .hasSize(2)
                .containsExactlyInAnyOrder(
                        OrderMapper.INSTANCE.toDto(first),
                        OrderMapper.INSTANCE.toDto(second)
                );
    }

    @Test
    void shouldReturnBadRequestWhenGetOrderByIdWithNonExistingId() {
        Order order = Order.builder().active(true).build();
        orderPort.save(order);

        webTestClient.get()
                .uri(API_URL + "/{id}", UUID.randomUUID())
                .header(HttpHeaders.AUTHORIZATION, format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturnOrderGetOrderById() {
        Order order = Order.builder().active(true).build();
        orderPort.save(order);

        EntityExchangeResult<ResponseDto<OrderDto>> result = webTestClient.get()
                .uri(API_URL + "/{id}", order.getId())
                .header(HttpHeaders.AUTHORIZATION, format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ResponseDto<OrderDto>>() {
                })
                .returnResult();

        OrderDto orderDto = result.getResponseBody()
                .data().get(ORDER);

        assertThat(orderDto).isNotNull()
                .isEqualTo(OrderMapper.INSTANCE.toDto(order));
    }

    @Test
    void shouldDeleteOrderWhenCloseOrderIfOrderWithoutProducts() {
        Order order = Order.builder().active(true).build();
        orderPort.save(order);

        EntityExchangeResult<ResponseDto<OrderDto>> result = webTestClient.put()
                .uri(API_URL + "/{id}/close", order.getId())
                .header(HttpHeaders.AUTHORIZATION, format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ResponseDto<OrderDto>>() {
                })
                .returnResult();

        OrderDto orderDto = result.getResponseBody()
                .data().get(ORDER);
        Optional<Order> dbOrder = orderPort.findById(order.getId());

        assertThat(orderDto).isNotNull()
                .isEqualTo(OrderMapper.INSTANCE.toDto(order));
        assertThat(dbOrder).isNotPresent();
    }

    @Test
    void shouldCloseOrderWhenCloseOrder() {
        Product product = Product.builder()
                .active(true)
                .name("product")
                .price(10.0)
                .build();
        productPort.save(product);

        Table table = Table.builder()
                .active(true)
                .name("test")
                .password("test")
                .tableType(TableType.COMMON)
                .build();
        tablePort.save(table);

        Order order = Order.builder()
                .active(true)
                .tableId(table.getId())
                .productsId(List.of(product.getId()))
                .build();
        orderPort.save(order);
        table.setOrderId(order.getId());
        tablePort.save(table);

        Bill bill = Bill.builder()
                .active(false)
                .orderId(order.getId())
                .price(10.0)
                .priceWithDiscount(10.0)
                .build();
        billPort.save(bill);
        order.setBillId(bill.getId());
        orderPort.save(order);

        EntityExchangeResult<ResponseDto<OrderDto>> result = webTestClient.put()
                .uri(API_URL + "/{id}/close", order.getId())
                .header(HttpHeaders.AUTHORIZATION, format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ResponseDto<OrderDto>>() {
                })
                .returnResult();

        OrderDto orderDto = result.getResponseBody()
                .data().get(ORDER);
        Optional<Order> dbOrder = orderPort.findById(order.getId());

        assertThat(dbOrder).isPresent()
                .get().extracting(Order::isActive).isEqualTo(false);
    }

    @Test
    void shouldAddProductToOrderWhenAddProductToOrder() {
        Product product = Product.builder()
                .active(true)
                .name("product")
                .price(10.0)
                .build();
        productPort.save(product);

        Order order = Order.builder()
                .active(true)
                .build();
        orderPort.save(order);

        OrderProductsRequestDto requestDto = new OrderProductsRequestDto(product.getId(), 5);

        EntityExchangeResult<ResponseDto<OrderDto>> result = webTestClient.put()
                .uri(API_URL + "/{id}/products", order.getId())
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .bodyValue(requestDto)
                .header(HttpHeaders.AUTHORIZATION, format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ResponseDto<OrderDto>>() {
                })
                .returnResult();

        OrderDto orderDto = result.getResponseBody()
                .data().get(ORDER);

        assertThat(orderDto.productsId())
                .hasSize(5)
                .containsOnly(product.getId());
    }

    @Test
    void shouldRemoveProductToOrderWhenRemoveProductToOrder() {
        Product product = Product.builder()
                .active(true)
                .name("product")
                .price(10.0)
                .build();
        productPort.save(product);

        Order order = Order.builder()
                .active(true)
                .productsId(List.of(product.getId(), product.getId(), product.getId()))
                .build();
        orderPort.save(order);

        OrderProductsRequestDto requestDto = new OrderProductsRequestDto(product.getId(), 2);

        EntityExchangeResult<ResponseDto<OrderDto>> result = webTestClient.put()
                .uri(API_URL + "/{id}/products/remove", order.getId())
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .bodyValue(requestDto)
                .header(HttpHeaders.AUTHORIZATION, format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ResponseDto<OrderDto>>() {
                })
                .returnResult();

        OrderDto orderDto = result.getResponseBody()
                .data().get(ORDER);

        assertThat(orderDto.productsId())
                .hasSize(1)
                .containsOnly(product.getId());
    }
}