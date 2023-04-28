package com.konstantion.authentication;

import com.konstantion.authentication.model.AuthenticationResponse;
import com.konstantion.table.model.LoginTableRequest;
import com.konstantion.user.model.LoginUserRequest;

public interface AuthenticationService {
    AuthenticationResponse authenticate(LoginTableRequest request);

    AuthenticationResponse authenticate(LoginUserRequest request);
}
