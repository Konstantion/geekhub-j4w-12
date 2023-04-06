package com.konstantion.call;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class Call {
    private UUID id;
    private UUID tableId;
    @Builder.Default
    private List<UUID> waitersId = new ArrayList<>();
    private Purpose purpose;
    private LocalDateTime openedAt;
    private LocalDateTime closedAt;
    private Boolean active;

    public Call() {

    }
}
