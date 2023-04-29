package com.konstantion.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration {
    @Bean
    @Profile("!test")
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(
            DataSource hikariDataSource
    ) {
        return new NamedParameterJdbcTemplate(hikariDataSource);
    }
}
