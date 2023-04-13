package com.konstantion.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toSet;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class User implements UserDetails {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Integer age;
    private String password;
    private Boolean active;
    private LocalDateTime createdAt;
    @Builder.Default
    private Set<Role> roles = new HashSet<>();
    @Builder.Default
    private Set<Permission> permissions = new HashSet<>();

    public User() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> grantedPermissions = permissions
                .stream()
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .collect(toSet());
        Set<GrantedAuthority> grantedRoles = roles
                .stream()
                .map(Enum::name)
                .map(name -> "ROLE_" + name)
                .map(SimpleGrantedAuthority::new)
                .collect(toSet());
        return Stream.of(grantedPermissions, grantedRoles).flatMap(Set::stream)
                .collect(toSet());
    }

    public boolean hasPermission(Role role, Permission permission) {
        if (isNull(role) && isNull(permission)) {
            return false;
        }
        if (isNull(role)) {
            return permissions.contains(permission);
        }
        if (isNull(permission)) {
            return roles.contains(role);
        }

        return roles.contains(role) && permissions.contains(permission);
    }

    public boolean hasNoPermission(Role role, Permission permission) {
        return !hasPermission(role, permission);
    }

    public boolean hasNoPermission(Permission permission) {
        return !hasPermission(null, permission);
    }

    public boolean hasNoPermission(Role role) {
        return !hasPermission(role, null);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
