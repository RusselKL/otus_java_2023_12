package ru.otus.server.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.domain.crm.service.DBServiceClient;
import ru.otus.server.services.TemplateProcessor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"squid:S1948"})
public class ConsoleServlet extends HttpServlet {

    private static final String CONSOLE_PAGE_TEMPLATE = "console.html";

    private final DBServiceClient dbServiceClient;
    private final TemplateProcessor templateProcessor;

    public ConsoleServlet(TemplateProcessor templateProcessor, DBServiceClient dbServiceClient) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CONSOLE_PAGE_TEMPLATE, paramsMap));
    }
}
