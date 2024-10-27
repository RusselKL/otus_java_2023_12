package ru.otus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.domain.DBServiceClient;

@Controller
public class ConsoleController {

    private final DBServiceClient dbServiceClient;

    public ConsoleController(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @GetMapping("/console")
    public String consoleView(Model model) {
        var clientsNumber = dbServiceClient.findAll().size();
        model.addAttribute("clientsNumber", clientsNumber);
        return "console";
    }

}
