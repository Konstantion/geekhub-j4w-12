package com.konstantion.bill;

import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BillRepository {
    Optional<Bill> findById(UUID id);

    Bill save(Bill bill);

    void delete(Bill bill);
}
