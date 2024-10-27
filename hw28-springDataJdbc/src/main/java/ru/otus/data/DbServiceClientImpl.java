package ru.otus.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.data.repository.ClientRepository;
import ru.otus.domain.DBServiceClient;
import ru.otus.domain.cache.Cache;
import ru.otus.domain.model.Client;
import ru.otus.domain.sessionmanager.TransactionManager;

import java.util.List;
import java.util.Optional;

@Service
public class DbServiceClientImpl implements DBServiceClient {

    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final ClientRepository clientRepository;
    private final TransactionManager transactionManager;
    private final Cache<String, Client> cache;

    public DbServiceClientImpl(
            TransactionManager transactionManager,
            ClientRepository clientRepository,
            Cache<String, Client> cache
    ) {
        this.transactionManager = transactionManager;
        this.clientRepository = clientRepository;
        this.cache = cache;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(() -> {
            var clientCloned = client.clone();
            var savedClient = clientRepository.save(clientCloned);
            log.info("saved client: {}", savedClient);
            cache.put(String.valueOf(savedClient.getId()), savedClient);
            return savedClient;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        var client = Optional.ofNullable(cache.get(String.valueOf(id)));
        if (client.isPresent()) {
            return client;
        }
        return transactionManager.doInReadOnlyTransaction(() -> {
            var clientOptional = clientRepository.findById(id);
            log.info("client: {}", clientOptional);
            return clientOptional;
        });
    }

    @Override
    public Optional<Client> findClientBy(String fieldName, Object fieldValue) {
        return transactionManager.doInReadOnlyTransaction(() -> {
            var client = clientRepository.findByFieldValue(fieldName, fieldValue).stream().findFirst();
            log.info("client where {} equal {}: {}", fieldName, fieldValue, client);
            return client;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(() -> {
            var clientList = clientRepository.findAll();
            log.info("clientList:{}", clientList);
            return clientList;
        });
    }

}
