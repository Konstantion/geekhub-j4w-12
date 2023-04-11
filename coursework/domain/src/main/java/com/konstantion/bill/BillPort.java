package com.konstantion.bill;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BillPort {
    Optional<Bill> findById(UUID id);

    Optional<Bill> findByOrderId(UUID id);

    Bill save(Bill bill);

    void delete(Bill bill);

    List<Bill> findAll();
}
