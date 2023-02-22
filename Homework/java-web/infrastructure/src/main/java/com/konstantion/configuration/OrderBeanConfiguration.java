package com.konstantion.configuration;

import com.konstantion.order.DomainOrderService;
import com.konstantion.order.OrderRepository;
import com.konstantion.order.OrderService;
import com.konstantion.order.validator.OrderValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.konstantion")
public class OrderBeanConfiguration {
    @Bean
    public OrderService orderService(OrderRepository orderRepository, OrderValidator orderValidator) {
        return new DomainOrderService(orderRepository, orderValidator);
    }

}
