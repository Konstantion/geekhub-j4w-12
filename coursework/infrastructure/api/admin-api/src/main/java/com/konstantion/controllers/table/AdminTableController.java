package com.konstantion.controllers.table;

import com.konstantion.dto.table.converter.TableMapper;
import com.konstantion.dto.table.dto.CreateTableRequestDto;
import com.konstantion.dto.table.dto.TableDto;
import com.konstantion.dto.table.dto.TableWaitersRequestDto;
import com.konstantion.dto.table.dto.UpdateTableRequestDto;
import com.konstantion.response.ResponseDto;
import com.konstantion.table.TableService;
import com.konstantion.user.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.konstantion.utils.EntityNameConstants.*;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/admin-api/tables")
public record AdminTableController(
        TableService tableService
) {
    private static final TableMapper tableMapper = TableMapper.INSTANCE;

    @GetMapping()
    public ResponseDto getAllTables() {
        List<TableDto> dtos = tableMapper.toDto(tableService.getAll(false));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message("All tables successfully returned")
                .timeStamp(now())
                .data(Map.of(TABLES, dtos))
                .build();
    }

    @PostMapping
    public ResponseDto createTable(
            @RequestBody CreateTableRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {
        TableDto dto = tableMapper.toDto(
                tableService.create(
                        tableMapper.toCreateTableRequest(requestDto),
                        user
                )
        );

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message("Table successfully created")
                .timeStamp(now())
                .data(Map.of(TABLE, dto))
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseDto deleteTableById(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal User user
    ) {
        TableDto dto = tableMapper.toDto(tableService.delete(id, user));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message("Table successfully deleted")
                .timeStamp(now())
                .data(Map.of(ORDER, dto))
                .build();
    }

    @PutMapping("/{id}")
    public ResponseDto updateTableById(
            @PathVariable("id") UUID id,
            @RequestBody UpdateTableRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {
        TableDto dto = tableMapper.toDto(tableService.update(
                id,
                tableMapper.toUpdateTableRequest(requestDto),
                user
        ));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message("Table successfully updated")
                .timeStamp(now())
                .data(Map.of(TABLE, dto))
                .build();
    }

    @PutMapping("/{id}/activate")
    public ResponseDto activateTableById(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal User user
    ) {
        TableDto dto = tableMapper.toDto(tableService.activate(id, user));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message("Table successfully activated")
                .timeStamp(now())
                .data(Map.of(TABLE, dto))
                .build();
    }

    @PutMapping("/{id}/deactivate")
    public ResponseDto deactivateTableById(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal User user
    ) {
        TableDto dto = tableMapper.toDto(tableService.deactivate(id, user));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message("Table successfully deactivate")
                .timeStamp(now())
                .data(Map.of(TABLE, dto))
                .build();
    }

    @PutMapping("/{id}/waiters/")
    public ResponseDto addWaiterToTable(
            @PathVariable("id") UUID id,
            @RequestBody TableWaitersRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {
        TableDto dto = tableMapper.toDto(tableService.addWaiter(
                id,
                tableMapper.toTableWaitersRequest(requestDto),
                user
        ));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message("Waiter successfully added")
                .timeStamp(now())
                .data(Map.of(TABLE, dto))
                .build();
    }

    @DeleteMapping("/{tableId}/waiters/{waiterId}")
    public ResponseDto removeWaiterFromTable(
            @PathVariable("tableId") UUID tableId,
            @PathVariable("waiterId") UUID waiterId,
            @AuthenticationPrincipal User user
    ) {
        TableWaitersRequestDto requestDto = new TableWaitersRequestDto(waiterId);

        TableDto dto = tableMapper.toDto(tableService.removeWaiter(
                tableId,
                tableMapper.toTableWaitersRequest(requestDto),
                user
        ));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message("Waiter successfully removed")
                .timeStamp(now())
                .data(Map.of(TABLE, dto))
                .build();
    }


    @DeleteMapping("/{id}/waiters")
    ResponseDto removeAllWaitersFromTable(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal User user
    ) {
        TableDto dto = tableMapper.toDto(
                tableService.removeAllWaiters(id, user)
        );

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message("Waiters successfully removed")
                .timeStamp(now())
                .data(Map.of(TABLE, dto))
                .build();
    }
}
