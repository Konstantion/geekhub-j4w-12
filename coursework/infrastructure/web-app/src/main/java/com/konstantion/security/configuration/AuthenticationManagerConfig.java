package com.konstantion.security.configuration;

import com.konstantion.security.table.TableAuthenticationProvider;
import com.konstantion.security.user.UserAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class AuthenticationManagerConfig {
    @Bean
    public AuthenticationManager authManager(
            HttpSecurity http,
            TableAuthenticationProvider tableAuthenticationProvider,
            UserAuthenticationProvider userAuthenticationProvider
    ) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(tableAuthenticationProvider);
        authenticationManagerBuilder.authenticationProvider(userAuthenticationProvider);
        return authenticationManagerBuilder.build();
    }
}
