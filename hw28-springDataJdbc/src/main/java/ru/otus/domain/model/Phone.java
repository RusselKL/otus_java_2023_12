package ru.otus.domain.model;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@ToString
@Table(name = "phone")
public class Phone {

    @Id
    private final Long id;

    private final String number;

    public Phone() {
        this(null);
    }

    public Phone(String number) {
        this(null, number);
    }

    @PersistenceCreator
    private Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }

}
