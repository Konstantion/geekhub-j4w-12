package com.konstantion.bill;

import com.konstantion.bill.dto.BillDto;
import com.konstantion.bill.dto.CreationBillDto;
import com.konstantion.user.User;

import java.util.UUID;

public interface BillService {
    BillDto create(CreationBillDto cbdto, User user);

    BillDto close(UUID billId, User user);

    BillDto restore(UUID billId, UUID tableId, User user);

    BillDto activate(UUID billId, User user);

    BillDto deactivate(UUID billId, User user);
}
