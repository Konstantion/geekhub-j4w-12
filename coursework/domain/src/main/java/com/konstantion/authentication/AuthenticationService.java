package com.konstantion.authentication;

import com.konstantion.table.model.LoginTableRequest;
import com.konstantion.user.model.LoginUserRequest;

public interface AuthenticationService {
    String authenticate(LoginTableRequest request);

    String authenticate(LoginUserRequest request);
}
