package com.konstantion.configuration;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
public class FlywayConfiguration {
    @Bean(initMethod = "migrate")
    @Profile("!test")
    public Flyway flyway(
            DataSource dataSource
    ) {
        return Flyway.configure()
                .dataSource(dataSource)
                .outOfOrder(true)
                .locations("classpath:/db/migration")
                .cleanDisabled(true)
                .baselineOnMigrate(true)
                .load();
    }
}
