package ru.otus.server.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.domain.crm.model.Client;
import ru.otus.domain.crm.service.DBServiceClient;

import java.io.IOException;
import java.util.stream.Collectors;

@SuppressWarnings({"squid:S1948"})
public class ClientApiServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(ClientApiServlet.class);

    private final DBServiceClient dbServiceClient;
    private final Gson gson;

    public ClientApiServlet(DBServiceClient dbServiceClient, Gson gson) {
        this.dbServiceClient = dbServiceClient;
        this.gson = gson;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String body = req.getReader().lines().collect(Collectors.joining());
        var client = gson.fromJson(body, Client.class);
        log.info("Client from request {}", client);
        var savedClient = dbServiceClient.saveClient(client);
        log.info("SavedClient {}", savedClient);

        resp.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = resp.getOutputStream();
        out.print(gson.toJson(savedClient));
    }
}
