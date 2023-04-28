package com.konstantion.api.configuration;

import com.konstantion.bill.BillService;
import com.konstantion.jwt.JwtService;
import com.konstantion.security.jwt.config.JwtConfig;
import com.konstantion.table.TablePort;
import com.konstantion.table.TableService;
import com.konstantion.user.UserPort;
import com.konstantion.user.UserService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class MockedBeansConfiguration {
    @Bean
    public BillService billService() {
        return mock(BillService.class);
    }
    @Bean
    public TablePort tablePort(){
        return mock(TablePort.class);
    }
    @Bean
    public UserPort userPort(){
        return mock(UserPort.class);
    }
    @Bean
    public JwtService jwtService(){
        return mock(JwtService.class);
    }
    @Bean
    public UserService userService(){
        return mock(UserService.class);
    }
    @Bean
    public TableService tableService(){
        return mock(TableService.class);
    }
}
