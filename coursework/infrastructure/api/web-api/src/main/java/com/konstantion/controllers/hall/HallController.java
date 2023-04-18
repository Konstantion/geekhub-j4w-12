package com.konstantion.controllers.hall;

import com.konstantion.dto.hall.converter.HallMapper;
import com.konstantion.dto.hall.dto.HallDto;
import com.konstantion.dto.table.converter.TableMapper;
import com.konstantion.dto.table.dto.TableDto;
import com.konstantion.hall.HallService;
import com.konstantion.response.ResponseDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.konstantion.utils.EntityNameConstants.*;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/web-api/halls")
public record HallController(
        HallService hallService
) {
    private static final HallMapper hallMapper = HallMapper.INSTANCE;
    private static final TableMapper tableMapper = TableMapper.INSTANCE;

    @GetMapping("/{id}")
    public ResponseDto getHallById(
            @PathVariable("id") UUID id
    ) {
        HallDto dto = hallMapper.toDto(hallService.getById(id));

        return ResponseDto.builder()
                .message(format("Hall with id %s", id))
                .timeStamp(now())
                .status(OK)
                .statusCode(OK.value())
                .data(Map.of(HALL, dto))
                .build();
    }

    @GetMapping()
    public ResponseDto getAllActiveHalls() {
        List<HallDto> dtos = hallMapper.toDto(hallService.getAll());

        return ResponseDto.builder()
                .message("All active halls")
                .timeStamp(now())
                .status(OK)
                .statusCode(OK.value())
                .data(Map.of(HALLS, dtos))
                .build();
    }

    @GetMapping("/{id}/tables")
    public ResponseDto getTablesByHallId(
            @PathVariable("id") UUID id
    ) {
        List<TableDto> dtos = tableMapper.toDto(hallService.getTablesByHallId(id));

        return ResponseDto.builder()
                .message(format("Tables for hall with id %s", id))
                .timeStamp(now())
                .status(OK)
                .statusCode(OK.value())
                .data(Map.of(TABLES, dtos))
                .build();
    }
}
