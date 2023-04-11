package com.konstantion.controllers.bill;

import com.konstantion.bill.BillService;
import com.konstantion.controllers.bill.converter.BillMapper;
import com.konstantion.controllers.bill.dto.BillDto;
import com.konstantion.controllers.bill.dto.CreateBillRequestDto;
import com.konstantion.response.ResponseDto;
import com.konstantion.user.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.konstantion.response.ResponseEntitiesNames.BILL;
import static com.konstantion.response.ResponseEntitiesNames.BILLS;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/web-api/bills")
public record BillController(
        BillService billService
) {
    private static final BillMapper billMapper = BillMapper.INSTANCE;

    @GetMapping
    public ResponseDto getAllBills() {
        List<BillDto> dtos = billMapper.toDto(billService.getAll());

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message("All bills")
                .timeStamp(now())
                .data(Map.of(BILLS, dtos))
                .build();
    }


    @GetMapping("/{id}")
    public ResponseDto getBillById(
            @PathVariable("id") UUID id
    ) {
        BillDto dto = billMapper.toDto(billService.getById(id));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message(format("Bill with id %s", id))
                .timeStamp(now())
                .data(Map.of(BILL, dto))
                .build();
    }

    @PostMapping
    public ResponseDto createBill(
            @RequestBody CreateBillRequestDto createBillRequestDto,
            @AuthenticationPrincipal User user
    ) {
        BillDto dto = billMapper.toDto(
                billService.create(
                        billMapper.toCreateBillRequest(createBillRequestDto),
                        user
                )
        );

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message("Created bill")
                .timeStamp(now())
                .data(Map.of(BILL, dto))
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseDto cancelBill(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal User user
    ) {
        BillDto dto = billMapper.toDto(billService.cancel(id, user));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message("Canceled bills")
                .timeStamp(now())
                .data(Map.of(BILL, dto))
                .build();
    }

    @PutMapping("/{id}/close")
    public ResponseDto closeBill(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal User user
    ) {
        BillDto dto = billMapper.toDto(billService.close(id, user));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message("Closed bill")
                .timeStamp(now())
                .data(Map.of(BILL, dto))
                .build();
    }

}
