package ru.otus.domain.crm.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "phone")
public class Phone {
    @Id
    @SequenceGenerator(name = "phone_gen", sequenceName = "phone_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phone_gen")
    @Expose
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    @Expose(serialize = false)
    private Client client;

    @Column(name = "number", unique = true)
    @Expose
    private String number;

    public Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }
}
