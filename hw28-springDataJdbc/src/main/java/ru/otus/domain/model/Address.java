package ru.otus.domain.model;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@ToString
@Table(name = "address")
public class Address {

    @Id
    private final Long id;

    private final String street;

    public Address() {
        this(null);
    }

    public Address(String street) {
        this(null, street);
    }

    @PersistenceCreator
    private Address(Long id, String street) {
        this.id = id;
        this.street = street;
    }

}
