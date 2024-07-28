package ru.otus.domain.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.domain.cachehw.HwCache;
import ru.otus.domain.core.repository.DataTemplate;
import ru.otus.domain.core.sessionmanager.TransactionManager;
import ru.otus.domain.crm.model.Client;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;
    private final HwCache<String, Client> cache;

    public DbServiceClientImpl(
            TransactionManager transactionManager,
            DataTemplate<Client> clientDataTemplate,
            HwCache<String, Client> cache
    ) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
        this.cache = cache;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(session -> {
            var clientCloned = client.clone();
            if (client.getId() == null) {
                var savedClient = clientDataTemplate.insert(session, clientCloned);
                log.info("created client: {}", clientCloned);
                cache.put(String.valueOf(savedClient.getId()), savedClient);
                return savedClient;
            }
            var savedClient = clientDataTemplate.update(session, clientCloned);
            log.info("updated client: {}", savedClient);
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
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientOptional = clientDataTemplate.findById(session, id);
            log.info("client: {}", clientOptional);
            return clientOptional;
        });
    }

    @Override
    public Optional<Client> findClientBy(String fieldName, Object fieldValue) {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var client = clientDataTemplate.findByEntityField(session, fieldName, fieldValue).stream().findFirst();
            log.info("client where {} equal {}: {}", fieldName, fieldValue, client);
            return client;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            log.info("clientList:{}", clientList);
            return clientList;
        });
    }
}
