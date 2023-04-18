package com.konstantion.controllers.order;

import com.konstantion.controllers.order.converter.OrderMapper;
import com.konstantion.controllers.order.dto.OrderDto;
import com.konstantion.controllers.order.dto.OrderProductsRequestDto;
import com.konstantion.order.OrderService;
import com.konstantion.response.ResponseDto;
import com.konstantion.user.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.konstantion.utils.EntityNameConstants.ORDER;
import static com.konstantion.utils.EntityNameConstants.ORDERS;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/web-api/orders")
public record OrderController(
        OrderService orderService
) {
    private static final OrderMapper orderMapper = OrderMapper.INSTANCE;

    @GetMapping()
    public ResponseDto getAllActiveOrders() {
        List<OrderDto> dtos = orderMapper.toDto(orderService.getAll());

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(now())
                .message("All active orders")
                .data(Map.of(ORDERS, dtos))
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
                .message(format("Order with id %s", id))
                .data(Map.of(ORDER, dto))
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
                .message("Order successfully closed")
                .data(Map.of(ORDER, dto))
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseDto deleteOrderById(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal User user
    ) {
        OrderDto dto = orderMapper.toDto(orderService.delete(id, user));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(now())
                .message("Order successfully deleted")
                .data(Map.of(ORDER, dto))
                .build();
    }

    @PutMapping("/{id}/products")
    public ResponseDto addProductToOrder(
            @PathVariable("id") UUID id,
            @RequestBody OrderProductsRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {

        OrderDto dto = orderMapper.toDto(
                orderService.addProduct(
                        id,
                        orderMapper.toOrderProductsRequest(requestDto),
                        user
                )
        );

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(now())
                .message("Products successfully added to the order")
                .data(Map.of(ORDER, dto))
                .build();
    }

    @DeleteMapping("/{id}/products")
    public ResponseDto removeProductFromOrder(
            @PathVariable("id") UUID id,
            @RequestBody OrderProductsRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {

        OrderDto dto = orderMapper.toDto(
                orderService.removeProduct(
                        id,
                        orderMapper.toOrderProductsRequest(requestDto),
                        user
                )
        );

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(now())
                .message("Products successfully removed from the order")
                .data(Map.of(ORDER, dto))
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
                .data(Map.of(ORDER, dto))
                .build();
    }
}
