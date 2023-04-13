package com.konstantion.security.table;

import com.konstantion.table.Table;
import com.konstantion.table.TablePort;
import com.konstantion.table.authentication.TableAuthenticationToken;
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
public class TableAuthenticationProvider implements AuthenticationProvider {
    private final TablePort tablePort;
    private final PasswordEncoder passwordEncoder;

    public TableAuthenticationProvider(TablePort tablePort, PasswordEncoder passwordEncoder) {
        this.tablePort = tablePort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String password = authentication.getCredentials().toString();
        UserDetails userDetails = getTableByPassword(password);
        if (userDetails != null && userDetails.isEnabled()) {
            return new TableAuthenticationToken(
                    userDetails,
                    userDetails.getPassword(),
                    userDetails.getAuthorities()
            );
        } else {
            if (userDetails == null) {
                throw new UsernameNotFoundException("Table not found");
            } else if (!userDetails.isEnabled()) {
                throw new DisabledException("Table is disabled");
            } else {
                throw new BadCredentialsException("Bad credentials");
            }
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return TableAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private Table getTableByPassword(String password) {
        List<Table> tables = tablePort.findAll();
        return tables.stream()
                .filter(table -> passwordEncoder.matches(password, table.getPassword()))
                .findAny().orElse(null);
    }
}
