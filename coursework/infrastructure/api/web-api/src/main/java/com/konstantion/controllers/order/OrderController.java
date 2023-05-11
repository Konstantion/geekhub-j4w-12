package com.konstantion.controllers.order;

import com.konstantion.dto.order.converter.OrderMapper;
import com.konstantion.dto.order.dto.OrderDto;
import com.konstantion.dto.order.dto.OrderProductsRequestDto;
import com.konstantion.dto.product.converter.ProductMapper;
import com.konstantion.dto.product.dto.ProductDto;
import com.konstantion.order.OrderService;
import com.konstantion.response.ResponseDto;
import com.konstantion.user.User;
import com.konstantion.utils.HashMaps;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.konstantion.utils.EntityNameConstants.*;
import static java.lang.Math.max;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/web-api/orders")
public record OrderController(
        OrderService orderService
) {
    private static final OrderMapper orderMapper = OrderMapper.INSTANCE;
    private static final ProductMapper productMapper = ProductMapper.INSTANCE;

    @GetMapping()
    public ResponseDto getAllActiveOrders() {
        List<OrderDto> dtos = orderMapper.toDto(orderService.getAll());

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(now())
                .message("All active orders successfully returned")
                .data(HashMaps.of(ORDERS, dtos))
                .build();
    }

    @GetMapping("/{id}")
    public ResponseDto getOrderById(
            @PathVariable("id") UUID id
    ) {
        OrderDto dto = orderMapper.toDto(orderService.getById(id));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(now())
                .message(format("Order with id %s successfully returned", id))
                .data(HashMaps.of(ORDER, dto))
                .build();
    }

    @PutMapping("/{id}/close")
    public ResponseDto closeOrderById(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal User user
    ) {
        OrderDto dto = orderMapper.toDto(orderService.close(id, user));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(now())
                .message(format("Order with id %s successfully closed", id))
                .data(HashMaps.of(ORDER, dto))
                .build();
    }

    @PutMapping("/{id}/products")
    public ResponseDto addProductToOrder(
            @PathVariable("id") UUID id,
            @RequestBody OrderProductsRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {

        int count = orderService.addProduct(
                id,
                orderMapper.toOrderProductsRequest(requestDto),
                user);
        OrderDto dto = orderMapper.toDto(orderService.getById(id));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(now())
                .message(format("%s products with id %s successfully added to the order with id %s", count, requestDto.productId(), id))
                .data(HashMaps.of(ORDER, dto))
                .build();
    }

    @PutMapping("/{id}/products/remove")
    public ResponseDto removeProductFromOrder(
            @PathVariable("id") UUID id,
            @RequestBody OrderProductsRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {

        int count = orderService.removeProduct(
                id,
                orderMapper.toOrderProductsRequest(requestDto),
                user);
        OrderDto dto = orderMapper.toDto(orderService.getById(id));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(now())
                .message(format("%s products with id %s successfully removed from the order with id %s", count, requestDto.productId(), id))
                .data(HashMaps.of(ORDER, dto))
                .build();
    }

    @GetMapping("/{id}/products")
    public ResponseDto getOrderProducts(
            @PathVariable("id") UUID orderId
    ) {
        List<ProductDto> products = productMapper.toDto(
                orderService.getProductsByOrderId(orderId)
        );
        Page<ProductDto> pageDto = new PageImpl<>(products, Pageable.ofSize(max(products.size(), 1)), max(products.size(), 1));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(now())
                .message(format("Products from order with id %s successfully returned", orderId))
                .data(HashMaps.of(PRODUCTS, pageDto))
                .build();
    }

    @PutMapping("/{orderId}/transfer/tables/{tableId}")
    public ResponseDto transferOrder(
            @PathVariable("orderId") UUID orderId,
            @PathVariable("tableId") UUID tableId,
            @AuthenticationPrincipal User user
    ) {
        OrderDto dto = orderMapper.toDto(orderService.transferToAnotherTable(
                orderId,
                tableId,
                user
        ));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(now())
                .message(format("Order with id %s successfully transferred to the table with id %s", orderId, tableId))
                .data(HashMaps.of(ORDER, dto))
                .build();
    }
}
