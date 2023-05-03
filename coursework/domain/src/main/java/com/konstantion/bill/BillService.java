package com.konstantion.bill;

import com.konstantion.bill.model.CreateBillRequest;
import com.konstantion.user.User;

import java.util.List;
import java.util.UUID;

public interface BillService {
    List<Bill> getAll(boolean onlyActive);

    default List<Bill> getAll() {
        return getAll(true);
    }

    Bill getById(UUID id);

    Bill create(CreateBillRequest createBillRequest, User user);

    Bill cancel(UUID billId, User user);

    Bill close(UUID billId, User user);

    Bill activate(UUID billId, User user);

    byte[] getPdfBytesById(UUID id, User user);
}
