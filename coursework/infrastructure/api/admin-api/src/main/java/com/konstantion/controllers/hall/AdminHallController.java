package com.konstantion.controllers.hall;

import com.konstantion.dto.hall.converter.HallMapper;
import com.konstantion.dto.hall.dto.CreateHallRequestDto;
import com.konstantion.dto.hall.dto.HallDto;
import com.konstantion.hall.HallService;
import com.konstantion.response.ResponseDto;
import com.konstantion.user.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.konstantion.utils.EntityNameConstants.HALL;
import static com.konstantion.utils.EntityNameConstants.HALLS;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/admin-api/halls")
public record AdminHallController(
        HallService hallService
) {
    private static final HallMapper hallMapper = HallMapper.INSTANCE;

    @GetMapping()
    public ResponseDto getAllHalls() {
        List<HallDto> dtos = hallMapper.toDto(hallService.getAll(false));

        return ResponseDto.builder()
                .message("All active halls successfully returned")
                .timeStamp(now())
                .status(OK)
                .statusCode(OK.value())
                .data(Map.of(HALLS, dtos))
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
