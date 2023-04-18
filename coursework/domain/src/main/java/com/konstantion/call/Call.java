package com.konstantion.call;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class Call {
    private UUID id;
    private UUID tableId;
    @Builder.Default
    private Set<UUID> waitersId = new HashSet<>();
    private Purpose purpose;
    private LocalDateTime openedAt;

    public Call() {

    }
}
