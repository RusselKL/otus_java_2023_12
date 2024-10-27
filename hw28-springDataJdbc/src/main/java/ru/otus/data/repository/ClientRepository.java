package ru.otus.data.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import ru.otus.domain.model.Client;

import java.util.List;

public interface ClientRepository extends ListCrudRepository<Client, Long> {
    @Query("select * from client where :field = :value")
    List<Client> findByFieldValue(String field, Object value);
}
