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
@Table(name = "role")
public class Role {
    @Id
    @SequenceGenerator(name = "role_gen", sequenceName = "role_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_gen")
    @Column(name = "id")
    @Expose
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    @Expose(serialize = false)
    private Client client;

    @Column(name = "role")
    @Expose
    private String role;

    public Role(Long id, String role) {
        this.id = id;
        this.role = role;
    }
}
