package ru.otus.domain.model;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@ToString
@Table(name = "role")
public class Role {

    @Id
    private final Long id;

    private final String role;

    public Role() {
        this(null);
    }

    public Role(String role) {
        this(null, role);
    }

    @PersistenceCreator
    private Role(Long id, String role) {
        this.id = id;
        this.role = role;
    }

}
