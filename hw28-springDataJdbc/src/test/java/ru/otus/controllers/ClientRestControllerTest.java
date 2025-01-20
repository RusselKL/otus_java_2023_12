package ru.otus.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.domain.DBServiceClient;
import ru.otus.domain.model.Address;
import ru.otus.domain.model.Client;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("WithContextTest: REST-контроллер клиентов ")
@WebMvcTest(ClientRestController.class)
class ClientRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private DBServiceClient dbServiceClient;

    @DisplayName("должен возвращать всех клиентов")
    @Test
    void shouldReturnAllClients() throws Exception {
        var initialClient = new Client("Ilon", "Mask", new Address("Tesla"));

        given(dbServiceClient.findAll()).willReturn(List.of(initialClient));

        mvc.perform(get("/api/client").accept("application/json; charset=utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Ilon")));
    }

    @DisplayName("должен создать клиента")
    @Test
    void shouldCreateClient() throws Exception {
        var clientToCreate = new Client("Donald", "Trump", new Address("USA"));

        Gson gson = new GsonBuilder().create();
        var json = gson.toJson(clientToCreate);

        given(dbServiceClient.saveClient(any(Client.class))).willReturn(clientToCreate);

        mvc.perform(
                        post("/api/client")
                                .contentType("application/json; charset=utf-8")
                                .content(json)
                                .accept("application/json; charset=utf-8")
                )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Donald")));
    }
}
