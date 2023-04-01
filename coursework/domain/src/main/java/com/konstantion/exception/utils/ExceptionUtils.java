package com.konstantion.exception.utils;

import com.konstantion.exception.BadRequestException;
import com.konstantion.guest.dto.GuestDto;
import com.konstantion.order.Order;
import com.konstantion.order.dto.OrderDto;
import com.konstantion.product.Product;
import com.konstantion.product.dto.ProductDto;
import com.konstantion.table.Table;
import com.konstantion.table.dto.TableDto;

import static java.lang.String.format;

public record ExceptionUtils() {
    public static boolean isActiveOrThrow(Table table) {
        boolean isActive = table.isActive();
        if(!isActive) {
            throw new BadRequestException(format("Table with id %s, isn't active", table.getId()));
        }
        return true;
    }

    public static boolean isActiveOrThrow(TableDto table) {
        boolean isActive = table.active();
        if(!isActive) {
            throw new BadRequestException(format("Table with id %s, isn't active", table.id()));
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

    public static boolean isActiveOrThrow(OrderDto order) {
        boolean isActive = order.active();
        if(!isActive) {
            throw new BadRequestException(format("Order with id %s, isn't active", order.id()));
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

    public static boolean isActiveOrThrow(ProductDto product) {
        boolean isActive = product.active();
        if(!isActive) {
            throw new BadRequestException(format("Product with id %s, isn't active", product.id()));
        }
        return true;
    }

    public static boolean isActiveOrThrow(GuestDto guest) {
        boolean isActive = guest.active();
        if(!isActive) {
            throw new BadRequestException(format("Guest with id %s, isn't active", guest.id()));
        }
        return true;
    }
}
