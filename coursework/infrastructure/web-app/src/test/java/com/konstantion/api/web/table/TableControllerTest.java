package com.konstantion.api.web.table;

import com.github.javafaker.Faker;
import com.konstantion.dto.order.dto.OrderDto;
import com.konstantion.dto.table.converter.TableMapper;
import com.konstantion.dto.table.dto.TableDto;
import com.konstantion.dto.user.converter.UserMapper;
import com.konstantion.dto.user.dto.UserDto;
import com.konstantion.jwt.JwtService;
import com.konstantion.order.OrderPort;
import com.konstantion.response.ResponseDto;
import com.konstantion.table.Table;
import com.konstantion.table.TablePort;
import com.konstantion.testcontainers.configuration.DatabaseContainer;
import com.konstantion.testcontainers.configuration.DatabaseTestConfiguration;
import com.konstantion.user.Permission;
import com.konstantion.user.Role;
import com.konstantion.user.User;
import com.konstantion.user.UserPort;
import org.junit.ClassRule;
import org.junit.jupiter.api.*;
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
import java.util.Set;
import java.util.UUID;

import static com.konstantion.table.TableType.COMMON;
import static com.konstantion.utils.EntityNameConstants.*;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Testcontainers
@ContextConfiguration(classes = {DatabaseTestConfiguration.class})
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TableControllerTest {
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
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private OrderPort orderPort;
    private User waiter;
    private String jwtToken;
    private static final TableMapper tableMapper = TableMapper.INSTANCE;
    Faker faker;
    private static final String API_URL = "/web-api/tables";

    @BeforeEach
    void setUp() {
        faker = new Faker();
        waiter = User.builder()
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
    }

    @Test
    void shouldReturnForbiddenWhenUnauthorizedUser() {
        webTestClient.get()
                .uri(API_URL)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void shouldReturnActiveTablesWhenGetAllActiveTables() {
        Table first = Table.builder()
                .active(true)
                .name("first")
                .password("first")
                .tableType(COMMON)
                .build();
        Table second = Table.builder()
                .active(false)
                .name("second")
                .password("second")
                .tableType(COMMON)
                .build();
        Table third = Table.builder()
                .active(true)
                .name("third")
                .password("third")
                .tableType(COMMON)
                .build();
        tablePort.save(first);
        tablePort.save(second);
        tablePort.save(third);

        EntityExchangeResult<ResponseDto<List<TableDto>>> result = webTestClient.get()
                .uri(API_URL)
                .header(HttpHeaders.AUTHORIZATION, format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ResponseDto<List<TableDto>>>() {
                })
                .returnResult();

        List<TableDto> tableDtos = result.getResponseBody()
                .data().get(TABLES);

        assertThat(tableDtos)
                .hasSize(2)
                .containsExactlyInAnyOrder(tableMapper.toDto(first), tableMapper.toDto(third));
    }

    @Test
    void shouldReturnTableByIdWhenGetTableById() {
        Table table = Table.builder()
                .active(true)
                .name("test")
                .password("test")
                .tableType(COMMON)
                .build();
        tablePort.save(table);

        EntityExchangeResult<ResponseDto<TableDto>> result = webTestClient.get()
                .uri(API_URL + "/{id}", table.getId())
                .header(HttpHeaders.AUTHORIZATION, format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ResponseDto<TableDto>>() {
                })
                .returnResult();

        TableDto tableDto = result.getResponseBody()
                .data().get(TABLE);

        assertThat(tableDto).isNotNull()
                .isEqualTo(tableMapper.toDto(table));
    }

    @Test
    void shouldReturnBadRequestWhenGetTableByIdWithNonExistingId() {
        Table table = Table.builder()
                .active(true)
                .name("test")
                .password("test")
                .tableType(COMMON)
                .build();
        tablePort.save(table);

        webTestClient.get()
                .uri(API_URL + "/{id}", UUID.randomUUID())
                .header(HttpHeaders.AUTHORIZATION, format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturnNullWhenGetOrderByTableIdWithTableWithoutOrder() {
        Table table = Table.builder()
                .active(true)
                .name("test")
                .password("test")
                .tableType(COMMON)
                .build();
        tablePort.save(table);

        EntityExchangeResult<ResponseDto<OrderDto>> result = webTestClient.get()
                .uri(API_URL + "/{id}/order", table.getId())
                .header(HttpHeaders.AUTHORIZATION, format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ResponseDto<OrderDto>>() {
                })
                .returnResult();

        OrderDto tableDto = result.getResponseBody()
                .data().get(ORDER);

        assertThat(tableDto).isNull();
    }

    @Test
    void shouldCreateOrderWhenOpenTableOrder() {
        Table table = Table.builder()
                .active(true)
                .name("test")
                .password("test")
                .tableType(COMMON)
                .build();
        tablePort.save(table);

        EntityExchangeResult<ResponseDto<OrderDto>> result = webTestClient.post()
                .uri(API_URL + "/{id}/order", table.getId())
                .header(HttpHeaders.AUTHORIZATION, format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ResponseDto<OrderDto>>() {
                })
                .returnResult();

        OrderDto orderDto = result.getResponseBody()
                .data().get(ORDER);
        Table dbTable = tablePort.findById(table.getId()).get();

        assertThat(orderDto).isNotNull()
                .extracting(OrderDto::id).isNotNull()
                .isEqualTo(dbTable.getOrderId());

        assertThat(orderDto.tableId())
                .isEqualTo(dbTable.getId());
    }

    @Test
    void shouldReturnBadRequestWhenOpenTableOrderWithTableThatHasOrder() {
        Table table = Table.builder()
                .active(true)
                .name("test")
                .password("test")
                .tableType(COMMON)
                .build();
        tablePort.save(table);

        webTestClient.post()
                .uri(API_URL + "/{id}/order", table.getId())
                .header(HttpHeaders.AUTHORIZATION, format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus().isOk();

        webTestClient.post()
                .uri(API_URL + "/{id}/order", table.getId())
                .header(HttpHeaders.AUTHORIZATION, format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturnBadRequestWhenOpenTableOrderWithInactiveTable() {
        Table table = Table.builder()
                .active(false)
                .name("test")
                .password("test")
                .tableType(COMMON)
                .build();
        tablePort.save(table);

        webTestClient.post()
                .uri(API_URL + "/{id}/order", table.getId())
                .header(HttpHeaders.AUTHORIZATION, format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturnTableWaitersWhenGetWaitersByTableId() {
        Table table = Table.builder()
                .active(false)
                .name("test")
                .password("test")
                .tableType(COMMON)
                .waitersId(Set.of(waiter.getId()))
                .build();
        tablePort.save(table);

        EntityExchangeResult<ResponseDto<List<UserDto>>> result = webTestClient.get()
                .uri(API_URL + "/{id}/waiters", table.getId())
                .header(HttpHeaders.AUTHORIZATION, format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ResponseDto<List<UserDto>>>() {
                })
                .returnResult();

        List<UserDto> userDtos = result.getResponseBody()
                .data().get(USERS);

        assertThat(userDtos)
                .hasSize(1)
                .containsExactlyInAnyOrder(UserMapper.INSTANCE.toDto(waiter));
    }
}
