package com.konstantion.testcontainers;

import com.konstantion.bill.Bill;
import com.konstantion.call.Call;
import com.konstantion.category.Category;
import com.konstantion.guest.Guest;
import com.konstantion.hall.Hall;
import com.konstantion.order.Order;
import com.konstantion.product.Product;
import com.konstantion.table.Table;
import com.konstantion.user.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

@Configuration
public class RowMappersTestConfiguration {
    @Bean
    public RowMapper<Bill> billRowMapper() {
        return new BeanPropertyRowMapper<>(Bill.class);
    }

    @Bean
    public RowMapper<Order> orderRowMapper() {
        return new BeanPropertyRowMapper<>(Order.class);
    }

    @Bean
    public RowMapper<Call> callRowMapper() {
        return new BeanPropertyRowMapper<>(Call.class);
    }

    @Bean
    public RowMapper<Guest> guestRowMapper() {
        return new BeanPropertyRowMapper<>(Guest.class);
    }

    @Bean
    public RowMapper<Product> productRowMapper() {
        return new BeanPropertyRowMapper<>(Product.class);
    }

    @Bean
    public RowMapper<Category> categoryRowMapper() {
        return new BeanPropertyRowMapper<>(Category.class);
    }

    @Bean
    public RowMapper<Table> tableRowMapper() {
        return new BeanPropertyRowMapper<>(Table.class);
    }

    @Bean
    public RowMapper<User> userRowMapper() {
        return new BeanPropertyRowMapper<>(User.class);
    }

    @Bean
    public RowMapper<Hall> hallRowMapper() {
        return new BeanPropertyRowMapper<>(Hall.class);
    }
}
