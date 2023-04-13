package com.konstantion.order;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

class OrderServiceImplTest {
    @Test
    void arraytest() {
        UserDetails userDetails = User.builder()
                .username("user")
                .password("pass")
                .roles("ADMIN")
                .authorities("LOL")
                .build();

        System.out.println(userDetails.getAuthorities());
    }
}