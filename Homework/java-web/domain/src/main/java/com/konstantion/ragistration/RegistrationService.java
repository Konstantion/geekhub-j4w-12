package com.konstantion.ragistration;

import com.konstantion.user.model.LoginUserRequest;
import com.konstantion.user.model.RegistrationUserRequest;
import com.konstantion.user.model.RestoreUserRequest;


public interface RegistrationService {
    String register(RegistrationUserRequest registrationUserDto);

    String confirmToken(String token);

    String authenticate(LoginUserRequest loginUserDto);

    String restorePassword(RestoreUserRequest request);

    String confirmRestore(String  token);
}
