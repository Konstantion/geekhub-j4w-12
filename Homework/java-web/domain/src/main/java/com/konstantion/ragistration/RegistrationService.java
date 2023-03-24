package com.konstantion.ragistration;

import com.konstantion.user.dto.LoginUserDto;
import com.konstantion.user.dto.RegistrationUserDto;


public interface RegistrationService{
    String register(RegistrationUserDto registrationUserDto);

    String confirmToken(String token);

    String authenticate(LoginUserDto loginUserDto);
}
