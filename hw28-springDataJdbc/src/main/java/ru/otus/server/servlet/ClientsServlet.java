package ru.otus.server.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.domain.crm.service.DBServiceClient;

import java.io.IOException;

public class ClientsServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(ClientsServlet.class);

    private final DBServiceClient dbServiceClient;
    private final Gson gson;

    public ClientsServlet(DBServiceClient dbServiceClient, Gson gson) {
        this.dbServiceClient = dbServiceClient;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var clients = dbServiceClient.findAll();

        log.info("DBServiceClient all clients {}", clients);

        resp.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = resp.getOutputStream();
        outputStream.print(gson.toJson(clients));
    }

}
