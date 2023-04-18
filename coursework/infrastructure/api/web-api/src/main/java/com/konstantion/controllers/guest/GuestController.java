package com.konstantion.controllers.guest;

import com.konstantion.dto.guest.converter.GuestMapper;
import com.konstantion.dto.guest.dto.GuestDto;
import com.konstantion.guest.GuestService;
import com.konstantion.response.ResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.konstantion.utils.EntityNameConstants.GUEST;
import static com.konstantion.utils.EntityNameConstants.GUESTS;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/web-api/guests")
public record GuestController(
        GuestService guestService
) {
    private static final GuestMapper guestMapper = GuestMapper.INSTANCE;

    @GetMapping()
    public ResponseDto getAllActiveGuests() {
        List<GuestDto> dtos = guestMapper.toDto(guestService.getAll());

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(now())
                .message("All active guests")
                .data(Map.of(GUESTS, dtos))
                .build();
    }

    @GetMapping("/{id}")
    public ResponseDto getGuestById(
            @PathVariable("id") UUID id
    ) {
        GuestDto dto = guestMapper.toDto(guestService.getById(id));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(now())
                .message(format("Guest with id %s", dto.id()))
                .data(Map.of(GUEST, dto))
                .build();
    }
}
