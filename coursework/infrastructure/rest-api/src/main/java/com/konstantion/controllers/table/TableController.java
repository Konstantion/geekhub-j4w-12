package com.konstantion.controllers.table;

import com.konstantion.controllers.order.converter.OrderMapper;
import com.konstantion.controllers.order.dto.OrderDto;
import com.konstantion.controllers.table.converter.TableMapper;
import com.konstantion.controllers.table.dto.TableDto;
import com.konstantion.response.ResponseDto;
import com.konstantion.table.TableService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.konstantion.response.ResponseEntitiesNames.*;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/web-api/tables")
public record TableController(
        TableService tableService
) {
    private static final TableMapper tableMapper = TableMapper.INSTANCE;
    private static final OrderMapper orderMapper = OrderMapper.INSTANCE;

    @GetMapping()
    public ResponseDto getAllTables() {
        List<TableDto> dtos = tableMapper.toDto(tableService.getAll());

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message("All tables")
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
    public ResponseDto getTableOrder(
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
}
