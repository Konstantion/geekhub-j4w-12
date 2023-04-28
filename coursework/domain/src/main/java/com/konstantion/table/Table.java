package com.konstantion.table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.konstantion.user.Permission.getDefaultTablePermission;
import static com.konstantion.user.Role.TABLE;
import static java.util.Objects.nonNull;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class Table implements UserDetails {
    private UUID id;
    private String name;
    private Integer capacity;
    private TableType tableType;
    private String password;
    private UUID hallId;
    private UUID orderId;
    @Builder.Default
    private Set<UUID> waitersId = new HashSet<>();
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    public Table() {
    }

    public boolean hasOrder() {
        return nonNull(orderId);
    }

    public void removeOrder() {
        this.orderId = null;
    }

    public boolean hasWaiters() {
        return waitersId.isEmpty();
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> grantedAuthorities = getDefaultTablePermission()
                .stream()
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + TABLE.name()));

        return grantedAuthorities;
    }

    @Override
    public String getUsername() {
        return name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Table table)) return false;
        return com.google.common.base.Objects.equal(id, table.id) && com.google.common.base.Objects.equal(name, table.name) && com.google.common.base.Objects.equal(capacity, table.capacity) && tableType == table.tableType && com.google.common.base.Objects.equal(password, table.password) && com.google.common.base.Objects.equal(hallId, table.hallId) && com.google.common.base.Objects.equal(orderId, table.orderId) && com.google.common.base.Objects.equal(waitersId, table.waitersId) && com.google.common.base.Objects.equal(active, table.active) && com.google.common.base.Objects.equal(createdAt, table.createdAt) && com.google.common.base.Objects.equal(deletedAt, table.deletedAt);
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(id, name, capacity, tableType, password, hallId, orderId, waitersId, active, createdAt, deletedAt);
    }
}
