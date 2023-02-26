package com.konstantion.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:database.properties")
public class DataBaseConfiguration {
    @Bean
    @Qualifier("postgresDB")
    public DataSource hikariDataSource(
            @Value("${database.url}") String url,
            @Value("${database.username}") String username,
            @Value("${database.password}") String password,
            @Value("${database.driver-class-name}") String driverClassName,
            @Value("${database.max-pool-size}") Integer maxPoolSize
    ) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);

        hikariConfig.setMaximumPoolSize(maxPoolSize);
        hikariConfig.setConnectionTestQuery("SELECT 1");

        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public NamedParameterJdbcTemplate jdbcTemplate(
            @Qualifier("postgresDB") DataSource hikariDataSource
    ) {
        return new NamedParameterJdbcTemplate(hikariDataSource);
    }

    @Bean(initMethod = "migrate")
    public Flyway flyway(@Qualifier("postgresDB") DataSource dataSource,
                         @Value("${flyway.location}") String location) {
        return Flyway.configure()
                .dataSource(dataSource)
                .outOfOrder(true)
                .locations(location)
                .cleanDisabled(true)
                .baselineOnMigrate(true)
                .load();
    }
}
