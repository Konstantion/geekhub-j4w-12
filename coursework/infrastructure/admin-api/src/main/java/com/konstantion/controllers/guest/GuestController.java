package com.konstantion.controllers.guest;

import com.konstantion.controllers.guest.converter.GuestMapper;
import com.konstantion.controllers.guest.dto.GuestDto;
import com.konstantion.guest.GuestService;
import com.konstantion.response.ResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.konstantion.utils.EntityNameConstants.GUESTS;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/admin-api/guests")
public record GuestController(
        GuestService guestService
) {
    private static final GuestMapper guestMapper = GuestMapper.INSTANCE;

    @GetMapping()
    public ResponseDto getAllGuests() {
        List<GuestDto> dtos = guestMapper.toDto(guestService.getAll(false));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(now())
                .message("All guests")
                .data(Map.of(GUESTS, dtos))
                .build();
    }
}
