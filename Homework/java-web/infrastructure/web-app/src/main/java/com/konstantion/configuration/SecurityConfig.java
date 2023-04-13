package com.konstantion.configuration;

import com.konstantion.jwt.JwtAuthenticationFilter;
import com.konstantion.jwt.JwtAuthenticationProvider;
import com.konstantion.jwt.JwtAuthorizationFilter;
import com.konstantion.jwt.JwtService;
import com.konstantion.jwt.config.JwtConfig;
import com.konstantion.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserService userService;
    private final JwtConfig jwtConfig;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    private static final String[] SECURITY_WHITELIST = {
            "/static/**",
            "/login",
            "/logout",
            "/*",
            "/web-api/registration/**",
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };


    public SecurityConfig(JwtService jwtService, UserService userService, JwtConfig jwtConfig, BCryptPasswordEncoder passwordEncoder) {
        this.jwtConfig = jwtConfig;
        this.jwtService = jwtService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider(userService, passwordEncoder);
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(jwtAuthenticationProvider());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain jwtSecurityFilterChain(HttpSecurity http) throws Exception {
        var authenticationManager = authenticationManager(http);
        http
                .cors()
                .and()
                .csrf().disable()

                .authorizeHttpRequests()
                .requestMatchers(SECURITY_WHITELIST).permitAll()
                .requestMatchers("/admin-api/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
                .requestMatchers("/super-admin-api/**").hasRole("SUPER_ADMIN")
                .anyRequest().authenticated()

                .and()
                .authenticationManager(authenticationManager)
                .addFilter(new JwtAuthenticationFilter(authenticationManager, jwtService))
                .addFilter(new JwtAuthorizationFilter(authenticationManager, jwtConfig, jwtService, userService))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return web -> web.ignoring().requestMatchers(SECURITY_WHITELIST);
//    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_SUPER_ADMIN > ROLE_ADMIN > ROLE_MODERATOR > ROLE_USER";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    @Bean
    public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy());
        return expressionHandler;
    }
}
