package com.konstantion.controllers.call;

import com.konstantion.call.CallService;
import com.konstantion.dto.call.converter.CallMapper;
import com.konstantion.dto.call.dto.CallDto;
import com.konstantion.response.ResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.konstantion.utils.EntityNameConstants.CALLS;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/admin-api/calls")
public record AdminCallController(
        CallService callService
) {
    private static final CallMapper callMapper = CallMapper.INSTANCE;

    @GetMapping()
    public ResponseDto getAllCall(
    ) {
        List<CallDto> dtos = callMapper.toDto(callService.getAll());

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(now())
                .message("All calls successfully returned")
                .data(Map.of(CALLS, dtos))
                .build();
    }
}
