package ru.otus.domain.crm.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "client")
public class Client implements Cloneable {

    @Id
    @SequenceGenerator(name = "client_gen", sequenceName = "client_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_gen")
    @Column(name = "id")
    @Expose
    private Long id;

    @Column(name = "name")
    @Expose
    private String name;

    @Column(name = "password")
    @Expose
    private String password;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "client")
    @Expose
    private List<Role> roles;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    @Expose
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "client")
    @Expose
    private List<Phone> phones;

    public Client(String name) {
        this.id = null;
        this.name = name;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Client(Long id, String name, Address address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public Client(Long id, String name, String password, Address address) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.address = address;
    }

    public Client(Long id, String name, String password, List<Role> roles, Address address, List<Phone> phones) {
        this(id, name, password, address);
        if (roles != null) {
            this.roles = roles.stream().map(role -> new Role(role.getId(), this, role.getRole())).toList();
        }
        if (phones != null) {
            this.phones = phones.stream().map(phone -> new Phone(phone.getId(), this, phone.getNumber())).toList();
        }
    }

    @Override
    @SuppressWarnings({"java:S2975", "java:S1182"})
    public Client clone() {
        return new Client(this.id, this.name, this.password, this.roles, this.address, this.phones);
    }
}
