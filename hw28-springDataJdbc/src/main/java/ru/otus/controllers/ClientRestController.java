package ru.otus.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.otus.domain.DBServiceClient;
import ru.otus.domain.model.Client;

import java.util.List;

@RestController
public class ClientRestController {
    private static final Logger log = LoggerFactory.getLogger(ClientRestController.class);

    private final DBServiceClient dbServiceClient;

    public ClientRestController(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @GetMapping("/api/client")
    public List<Client> getClients(@RequestParam(required = false) String fieldName, @RequestParam(required = false) Object fieldValue) {
        log.info("DBServiceClient find client by fieldName: {} and value: {}", fieldName, fieldValue);
        if (fieldName != null && fieldValue != null) {
            var clients = dbServiceClient.findClientBy(fieldName, fieldValue);
            log.info("DBServiceClient find client by fieldName: {} and value: {}, client: {}", fieldName, fieldValue, clients);
            return clients;
        }
        var clients = dbServiceClient.findAll();
        log.info("DBServiceClient all clients {}", clients);
        return clients;
    }

    @PostMapping("/api/client")
    public Client createClient(@RequestBody Client client) {
        log.info("Client from request {}", client);
        var savedClient = dbServiceClient.saveClient(client);
        log.info("SavedClient {}", savedClient);
        return savedClient;
    }

}
