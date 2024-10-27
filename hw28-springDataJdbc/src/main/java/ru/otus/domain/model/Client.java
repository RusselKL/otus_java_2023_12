package ru.otus.domain.model;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Collections;
import java.util.Set;

@Getter
@ToString
@Table(name = "client")
public class Client implements Cloneable {

    @Id
    private final Long id;

    private final String name;

    private final String password;

    @MappedCollection(idColumn = "client_id")
    private final Set<Role> roles;

    @MappedCollection(idColumn = "client_id")
    private final Address address;

    @MappedCollection(idColumn = "client_id")
    private final Set<Phone> phones;

    public Client() {
        this(null, null, null);
    }

    public Client(String name, String password, Address address) {
        this(null, name, password, Collections.emptySet(), address, Collections.emptySet());
    }

    public Client(String name, String password, Set<Role> roles, Address address, Set<Phone> phones) {
        this(null, name, password, roles, address, phones);
    }

    @PersistenceCreator
    private Client(Long id, String name, String password, Set<Role> roles, Address address, Set<Phone> phones) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.address = address;
        this.roles = roles;
        this.phones = phones;
    }

    @Override
    @SuppressWarnings({"java:S2975", "java:S1182"})
    public Client clone() {
        return new Client(this.id, this.name, this.password, this.roles, this.address, this.phones);
    }

}
