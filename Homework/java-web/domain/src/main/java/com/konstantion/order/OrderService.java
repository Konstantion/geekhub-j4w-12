package com.konstantion.order;

import com.konstantion.bucket.Bucket;
import com.konstantion.order.dto.OrderDto;
import com.konstantion.user.User;

public interface OrderService {
    OrderDto createOrder(User user, Bucket bucket);
}
