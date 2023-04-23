package com.konstantion.category;

import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class Category {
    private UUID id;
    private String name;
    private UUID creatorId;

    public Category() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category category)) return false;
        return Objects.equal(id, category.id) && Objects.equal(name, category.name) && Objects.equal(creatorId, category.creatorId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, creatorId);
    }
}
