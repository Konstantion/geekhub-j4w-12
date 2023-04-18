package com.konstantion.controllers.table;

import com.konstantion.controllers.order.converter.OrderMapper;
import com.konstantion.controllers.order.dto.OrderDto;
import com.konstantion.controllers.table.converter.TableMapper;
import com.konstantion.controllers.table.dto.TableDto;
import com.konstantion.controllers.user.converter.UserMapper;
import com.konstantion.controllers.user.dto.UserDto;
import com.konstantion.order.OrderService;
import com.konstantion.response.ResponseDto;
import com.konstantion.table.TableService;
import com.konstantion.user.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.konstantion.utils.EntityNameConstants.*;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/web-api/tables")
public record TableController(
        TableService tableService,
        OrderService orderService
) {
    private static final TableMapper tableMapper = TableMapper.INSTANCE;
    private static final OrderMapper orderMapper = OrderMapper.INSTANCE;

    private static final UserMapper userMapper = UserMapper.INSTANCE;

    @GetMapping()
    public ResponseDto getAllActiveTables() {
        List<TableDto> dtos = tableMapper.toDto(tableService.getAll());

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message("All active tables")
                .timeStamp(now())
                .data(Map.of(TABLES, dtos))
                .build();
    }

    @GetMapping("/{id}")
    public ResponseDto getTableById(
            @PathVariable("id") UUID id
    ) {
        TableDto dto = tableMapper.toDto(tableService.getById(id));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message(format("Table with id %s", dto.id()))
                .timeStamp(now())
                .data(Map.of(TABLE, dto))
                .build();
    }

    @GetMapping("/{id}/order")
    public ResponseDto getOrderByTableId(
            @PathVariable("id") UUID id
    ) {
        OrderDto dto = orderMapper.toDto(tableService.getOrderByTableId(id));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message(format("Order for table with id %s", id))
                .timeStamp(now())
                .data(Map.of(ORDER, dto))
                .build();
    }

    @PostMapping("/{id}/order")
    public ResponseDto openTableOrder(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal User user
    ) {
        OrderDto dto = orderMapper.toDto(orderService.open(id, user));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message("Order successfully created")
                .timeStamp(now())
                .data(Map.of(ORDER, dto))
                .build();
    }

    @GetMapping("/{id}/waiters")
    public ResponseDto getWaitersByTableId(
            @PathVariable("id") UUID id
    ) {
        List<UserDto> dtos = userMapper.toDto(
                tableService.getWaitersByTableId(id)
        );

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message(format("Waiters for table with id %s", id))
                .timeStamp(now())
                .data(Map.of(USERS, dtos))
                .build();
    }
}
