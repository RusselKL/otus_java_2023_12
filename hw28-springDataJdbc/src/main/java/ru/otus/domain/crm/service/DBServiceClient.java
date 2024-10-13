package ru.otus.domain.crm.service;

import ru.otus.domain.crm.model.Client;

import java.util.List;
import java.util.Optional;

public interface DBServiceClient {

    Client saveClient(Client client);

    Optional<Client> getClient(long id);

    Optional<Client> findClientBy(String fieldName, Object fieldValue);

    List<Client> findAll();
}
