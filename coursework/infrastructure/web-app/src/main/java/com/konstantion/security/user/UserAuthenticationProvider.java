package com.konstantion.security.user;

import com.konstantion.table.authentication.TableAuthenticationToken;
import com.konstantion.user.User;
import com.konstantion.user.UserPort;
import com.konstantion.user.authentication.UserAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {
    private final UserPort userPort;
    private final PasswordEncoder passwordEncoder;

    public UserAuthenticationProvider(UserPort userPort, PasswordEncoder passwordEncoder) {
        this.userPort = userPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String password = authentication.getCredentials().toString();
        UserDetails userDetails = getUserByPassword(password);
        if (userDetails != null && userDetails.isEnabled()) {
            return new TableAuthenticationToken(
                    userDetails,
                    userDetails.getPassword(),
                    userDetails.getAuthorities()
            );
        } else {
            if (userDetails == null) {
                throw new UsernameNotFoundException("User not found");
            } else if (!userDetails.isEnabled()) {
                throw new DisabledException("User is disabled");
            } else {
                throw new BadCredentialsException("Bad credentials");
            }
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UserAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private User getUserByPassword(String password) {
        List<User> users = userPort.findAll();
        return users.stream()
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .findAny().orElse(null);
    }
}
