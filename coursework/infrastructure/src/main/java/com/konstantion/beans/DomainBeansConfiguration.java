package com.konstantion.beans;

import com.konstantion.hall.HallService;
import com.konstantion.hall.HallServiceImp;
import com.konstantion.table.TableService;
import com.konstantion.table.TableServiceImp;
import com.konstantion.user.UserService;
import com.konstantion.user.UserServiceImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.konstantion")
public class DomainBeansConfiguration {
    @Bean
    public HallService hallService(HallServiceImp hallServiceImp) {
        return hallServiceImp;
    }

    @Bean
    public UserService userService(UserServiceImp userServiceImp) {
        return userServiceImp;
    }

    @Bean
    public TableService tableService(TableServiceImp tableServiceImp) {
        return tableServiceImp;
    }
}
