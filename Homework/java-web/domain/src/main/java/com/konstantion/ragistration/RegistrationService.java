package com.konstantion.ragistration;

import com.konstantion.user.model.LoginUserRequest;
import com.konstantion.user.model.RegistrationUserRequest;


public interface RegistrationService {
    String register(RegistrationUserRequest registrationUserDto);

    String confirmToken(String token);

    String authenticate(LoginUserRequest loginUserDto);
}
