package com.konstantion.bill;

import com.konstantion.bill.model.CreateBillRequest;
import com.konstantion.user.User;

import java.util.UUID;

public interface BillService {
    Bill getById(UUID id);
    Bill create(CreateBillRequest cbdto, User user);

    Bill cancel(UUID billId, User user);

    Bill close(UUID billId, User user);

    Bill activate(UUID billId, User user);
}
