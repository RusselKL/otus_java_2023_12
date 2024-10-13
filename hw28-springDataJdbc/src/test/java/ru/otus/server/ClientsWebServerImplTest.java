package ru.otus.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.domain.base.AbstractHibernateTest;
import ru.otus.domain.crm.model.Address;
import ru.otus.domain.crm.model.Client;
import ru.otus.domain.crm.model.Phone;
import ru.otus.domain.crm.model.Role;
import ru.otus.server.server.ClientsWebServer;
import ru.otus.server.server.ClientsWebServerSimple;
import ru.otus.server.services.TemplateProcessor;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DisplayName("Тест сервера должен")
class ClientsWebServerImplTest extends AbstractHibernateTest {

    private static final int WEB_SERVER_PORT = 8989;
    private static final String WEB_SERVER_URL = "http://localhost:" + WEB_SERVER_PORT + "/";
    private static final String CLIENTS_URL = "clients";

    private static Gson gson;
    private static ClientsWebServer webServer;
    private static HttpClient http;

    private Client savedClient;

    @BeforeEach
    public void setUpServer() throws Exception {
        http = HttpClient.newHttpClient();

        TemplateProcessor templateProcessor = mock(TemplateProcessor.class);

        var admin = new Client(
                null,
                "Ruslan",
                "Ruslan",
                List.of(new Role(null, "admin")),
                new Address(null, "RuslanStreet"),
                List.of(new Phone(null, "13-555-22"), new Phone(null, "14-666-333"))
        );

        savedClient = dbServiceClient.saveClient(admin);

        gson = new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().create();
        webServer = new ClientsWebServerSimple(WEB_SERVER_PORT, dbServiceClient, gson, templateProcessor);
        webServer.start();
    }

    @AfterEach
    public void tearDownServer() throws Exception {
        webServer.stop();
    }

    @DisplayName("доставать из БД всех существующих пользователей")
    @Test
    void shouldReturnCorrectClientsWhenAuthorized() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(WEB_SERVER_URL + CLIENTS_URL))
                .build();
        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(HttpURLConnection.HTTP_OK);
        assertThat(response.body()).contains(gson.toJson(savedClient));
    }
}
