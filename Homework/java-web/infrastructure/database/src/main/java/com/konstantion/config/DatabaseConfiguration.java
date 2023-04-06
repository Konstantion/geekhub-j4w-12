package com.konstantion.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class DatabaseConfiguration {
    @Bean
    public NamedParameterJdbcTemplate jdbcTemplate(
            DataSource hikariDataSource
    ) {
        return new NamedParameterJdbcTemplate(hikariDataSource);
    }

    @Bean(initMethod = "migrate")
    public Flyway flyway(
            DataSource dataSource,
            @Qualifier("flywayProps") Properties properties
    ) {
        return Flyway.configure()
                .dataSource(dataSource)
                .outOfOrder(true)
                .locations("classpath:/db/migration")
                .cleanDisabled(true)
                .baselineOnMigrate(true)
                .configuration(properties)
                .load();
    }
}
