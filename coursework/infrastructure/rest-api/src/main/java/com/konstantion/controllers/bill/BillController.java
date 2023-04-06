package com.konstantion.controllers.bill;

import com.konstantion.bill.BillPort;
import com.konstantion.bill.BillService;
import com.konstantion.controllers.bill.converter.BillMapper;
import com.konstantion.controllers.bill.dto.BillDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bills")
public record BillController(
        BillService billService
) {
    private static final BillMapper billMapper = BillMapper.INSTANCE;

    @GetMapping("/{id}")
    public BillDto getBillById(
            @PathVariable("id") UUID id
    ) {
        return billMapper.toDto(billService.getById(id));
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }
}
