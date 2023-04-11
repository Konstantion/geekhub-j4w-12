package com.konstantion.controllers.hall;

import com.konstantion.controllers.hall.converter.HallMapper;
import com.konstantion.controllers.hall.dto.CreateHallRequestDto;
import com.konstantion.controllers.hall.dto.HallDto;
import com.konstantion.controllers.table.converter.TableMapper;
import com.konstantion.controllers.table.dto.TableDto;
import com.konstantion.hall.HallService;
import com.konstantion.response.ResponseDto;
import com.konstantion.user.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.konstantion.response.ResponseEntitiesNames.*;
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
    public ResponseDto getHalls() {
        List<HallDto> dtos = hallMapper.toDto(hallService.getAll());

        return ResponseDto.builder()
                .message("All halls")
                .timeStamp(now())
                .status(OK)
                .statusCode(OK.value())
                .data(Map.of(HALLS, dtos))
                .build();
    }

    @GetMapping("/{id}/tables")
    public ResponseDto getHallTables(
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

    @PostMapping
    public ResponseDto createHall(
            @RequestBody CreateHallRequestDto createHallRequestDto,
            @AuthenticationPrincipal User user
    ) {
        HallDto dto = hallMapper.toDto(hallService.create(
                hallMapper.toCreateHallRequest(createHallRequestDto),
                user
        ));

        return ResponseDto.builder()
                .message("Created hall")
                .timeStamp(now())
                .status(OK)
                .statusCode(OK.value())
                .data(Map.of(HALL, dto))
                .build();
    }

    @PutMapping("/{id}/activate")
    public ResponseDto activateHall(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal User user
    ) {
        HallDto dto = hallMapper.toDto(hallService.activate(id, user));

        return ResponseDto.builder()
                .message("Activated hall")
                .timeStamp(now())
                .status(OK)
                .statusCode(OK.value())
                .data(Map.of(HALL, dto))
                .build();
    }

    @PutMapping("/{id}/deactivate")
    public ResponseDto deactivateHall(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal User user
    ) {
        HallDto dto = hallMapper.toDto(hallService.deactivate(id, user));

        return ResponseDto.builder()
                .message("Deactivated hall")
                .timeStamp(now())
                .status(OK)
                .statusCode(OK.value())
                .data(Map.of(HALL, dto))
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseDto deleteHall(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal User user
    ) {
        HallDto dto = hallMapper.toDto(hallService.delete(id, user));

        return ResponseDto.builder()
                .message("Deleted hall")
                .timeStamp(now())
                .status(OK)
                .statusCode(OK.value())
                .data(Map.of(HALL, dto))
                .build();
    }
}
