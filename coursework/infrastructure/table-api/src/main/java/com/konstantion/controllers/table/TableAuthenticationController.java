package com.konstantion.controllers.table;

import com.konstantion.authentication.AuthenticationService;
import com.konstantion.controllers.table.converter.TableMapper;
import com.konstantion.controllers.table.dto.LoginTableRequestDto;
import com.konstantion.response.ResponseDto;
import com.konstantion.table.TableService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static com.konstantion.utils.EntityNameConstants.JWT_TOKEN;
import static java.util.Map.of;

@RestController
@RequestMapping("/table-api/authentication")
public record  TableAuthenticationController(
        TableService tableService,
        AuthenticationService authenticationService
) {
    private static final TableMapper tableMapper = TableMapper.INSTANCE;
    public static final String BEARER_PREFIX = "Bearer ";

    @PostMapping()
    public ResponseEntity<ResponseDto> authenticate(
            @RequestBody LoginTableRequestDto requestDto
    ) {
        String jwtToken = authenticationService.authenticate(
                tableMapper.toLoginTableRequest(requestDto)
        );

        String bearerToken = BEARER_PREFIX + jwtToken;

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .body(ResponseDto.builder()
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully authenticated")
                        .timeStamp(LocalDateTime.now())
                        .data(of(JWT_TOKEN, jwtToken))
                        .build()
                );
    }
}
