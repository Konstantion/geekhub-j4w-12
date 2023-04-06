package com.konstantion.exception.utils;

import com.konstantion.bill.Bill;
import com.konstantion.exception.BadRequestException;
import com.konstantion.exception.NonExistingIdException;
import com.konstantion.guest.Guest;
import com.konstantion.hall.Hall;
import com.konstantion.order.Order;
import com.konstantion.product.Product;
import com.konstantion.table.Table;

import java.util.UUID;
import java.util.function.Supplier;

import static java.lang.String.format;

public record ExceptionUtils() {
    public static boolean isActiveOrThrow(Table table) {
        boolean isActive = table.isActive();
        if(!isActive) {
            throw new BadRequestException(format("Table with id %s, isn't active", table.getId()));
        }
        return true;
    }

    public static boolean isActiveOrThrow(Order order) {
        boolean isActive = order.isActive();
        if(!isActive) {
            throw new BadRequestException(format("Order with id %s, isn't active", order.getId()));
        }
        return true;
    }

    public static boolean isActiveOrThrow(Product product) {
        boolean isActive = product.isActive();
        if(!isActive) {
            throw new BadRequestException(format("Product with id %s, isn't active", product.getId()));
        }
        return true;
    }

    public static boolean isActiveOrThrow(Guest guest) {
        boolean isActive = guest.isActive();
        if(!isActive) {
            throw new BadRequestException(format("Guest with id %s, isn't active", guest.getId()));
        }
        return true;
    }

    public static boolean isActiveOrThrow(Bill bill) {
        boolean isActive = bill.isActive();
        if(!isActive) {
            throw new BadRequestException(format("Bill with id %s, isn't active", bill.getId()));
        }
        return true;
    }

    public static boolean isActiveOrThrow(Hall hall) {
        boolean isActive = hall.isActive();
        if(!isActive) {
            throw new BadRequestException(format("Bill with id %s, isn't active", hall.getId()));
        }
        return true;
    }

    public static <X extends Throwable> Supplier<? extends X> nonExistingIdSupplier(Class target, UUID id) throws X {
        String className = target.getSimpleName();
        return () -> {
          throw new NonExistingIdException(format("%s with id %s, doesn't exist", className, id));
        };
    }
}
