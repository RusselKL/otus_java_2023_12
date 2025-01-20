package ru.otus.data.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.domain.model.Client;

import java.util.List;

public interface ClientRepository extends ListCrudRepository<Client, Long> {
    @Query("select id, name, password from client c where :field = :value")
    List<Client> findByFieldValue(@Param("field") String field, @Param("value") Object value);
}
