package com.konstantion.configuration.domain;

import com.konstantion.user.User;
import com.konstantion.user.UserRawMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class UserBeanConfiguration {
    @Bean
    public User user() {
        return User.builder()
                .id(1L)
                .email("email")
                .uuid(UUID.fromString("d750e56e-b5e8-11ed-8481-00d8611a4231"))
                .password("password")
                .username("username")
                .build();
    }

    @Bean
    public UserRawMapper userRawMapper() {
        return new UserRawMapper();
    }
}
