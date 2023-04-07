package com.konstantion.controllers;

import com.konstantion.bucket.Bucket;
import com.konstantion.dto.mappers.OrderMapper;
import com.konstantion.dto.order.OrderDto;
import com.konstantion.dto.response.ResponseDto;
import com.konstantion.order.OrderService;
import com.konstantion.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/web-api/orders")
public record OrderController(OrderService orderService,
                              Bucket bucket) {
    public static final OrderMapper orderMapper = OrderMapper.INSTANCE;

    @PostMapping
    public ResponseEntity<ResponseDto> createOrder(
            @AuthenticationPrincipal User user
    ) {
        OrderDto dto = orderMapper.toDto(orderService.createOrder(user, bucket));
        return ResponseEntity.ok(ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message(String.format("Order with id %s successfully created", dto.uuid()))
                .data(Map.of("uuid", dto.uuid()))
                .timeStamp(now())
                .build()
        );
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ResponseDto> getOrderById(
            @PathVariable("uuid") UUID uuid,
            @AuthenticationPrincipal User user
    ) {
        OrderDto dto = orderMapper.toDto(orderService.findOrderById(uuid, user));
        return ResponseEntity.ok(ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message(String.format("Order with id %s ", dto.uuid()))
                .data(Map.of("order", dto))
                .timeStamp(now())
                .build());
    }

    @GetMapping("/users/{uuid}")
    public ResponseEntity<ResponseDto> getUserOrders(
            @PathVariable("uuid") UUID uuid,
            @AuthenticationPrincipal User user
    ) {
        List<OrderDto> dto = orderMapper.toDto(orderService.findOrdersByUserId(uuid, user));
        return ResponseEntity.ok(ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message(format("Order for user with id %s ", uuid))
                .data(Map.of("orders", dto))
                .timeStamp(now())
                .build());
    }

    @GetMapping("/users/authorized")
    public ResponseEntity<ResponseDto> getAuthorizedUserOrders(
            @AuthenticationPrincipal User user
    ) {
        List<OrderDto> dto = orderMapper.toDto(orderService.findOrdersByUserId(user.getId(), user));
        return ResponseEntity.ok(ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message(format("All orders of user with id %s", user.getId()))
                .data(Map.of("orders", dto))
                .timeStamp(now())
                .build());
    }

    @PutMapping("/{uuid}/complete")
    public ResponseEntity<ResponseDto> completeOrder(
            @PathVariable("uuid") UUID uuid,
            @AuthenticationPrincipal User user
    ) {
        orderService.completeOrder(uuid, user);

        return ResponseEntity.ok(ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message("Order completed")
                .data(Map.of("uuid", uuid))
                .timeStamp(now())
                .build());
    }

    @PutMapping("/{uuid}/cancel")
    public ResponseEntity<ResponseDto> cancelOrder(
            @PathVariable("uuid") UUID uuid,
            @AuthenticationPrincipal User user
    ) {
        orderService.cancelOrder(uuid, user);

        return ResponseEntity.ok(ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message("Order canceled")
                .data(Map.of("uuid", uuid))
                .timeStamp(now())
                .build());
    }
}
