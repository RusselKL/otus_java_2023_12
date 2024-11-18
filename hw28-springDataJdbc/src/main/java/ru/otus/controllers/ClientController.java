package ru.otus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.domain.DBServiceClient;

@Controller
public class ClientController {

    private final DBServiceClient dbServiceClient;

    public ClientController(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @GetMapping("/console")
    public String consoleView(Model model) {
        var clientsCount = dbServiceClient.findAll().size();
        model.addAttribute("clientsCount", clientsCount);
        return "console";
    }

}
