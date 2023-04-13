package com.konstantion.authentication;

import com.konstantion.jwt.JwtService;
import com.konstantion.table.authentication.TableAuthenticationToken;
import com.konstantion.table.model.LoginTableRequest;
import com.konstantion.table.validator.TableValidator;
import com.konstantion.user.authentication.UserAuthenticationToken;
import com.konstantion.user.model.LoginUserRequest;
import com.konstantion.user.validator.UserValidator;
import com.konstantion.utils.validator.ValidationResult;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.konstantion.utils.EntityNameConstants.*;

@Component
public record AuthenticationServiceImpl(
        JwtService jwtService,
        AuthenticationManager authenticationManager,
        TableValidator tableValidator,
        UserValidator userValidator
) implements AuthenticationService {
    @Override
    public String authenticate(LoginTableRequest request) {
        ValidationResult validationResult = tableValidator.validate(request);
        validationResult.validOrTrow();

        TableAuthenticationToken authenticationToken = new TableAuthenticationToken(
                null,
                request.password()
        );

        UserDetails tableDetails = (UserDetails) authenticationManager
                .authenticate(authenticationToken)
                .getPrincipal();

        Map<String, Object> extraClaim = Map.of(ENTITY, TABLE);

        return jwtService.generateToken(extraClaim, tableDetails);
    }

    @Override
    public String authenticate(LoginUserRequest request) {
        ValidationResult validationResult = userValidator.validate(request);
        validationResult.validOrTrow();

        UserAuthenticationToken authenticationToken = new UserAuthenticationToken(
                null,
                request.password()
        );

        UserDetails userDetails = (UserDetails) authenticationManager
                .authenticate(authenticationToken)
                .getPrincipal();

        Map<String, Object> extraClaim = Map.of(ENTITY, USER);

        return jwtService.generateToken(extraClaim, userDetails);
    }
}
