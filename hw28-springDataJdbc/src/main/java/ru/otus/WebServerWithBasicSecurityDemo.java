package ru.otus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.security.LoginService;
import org.hibernate.cfg.Configuration;
import ru.otus.domain.cachehw.MyCache;
import ru.otus.domain.core.repository.DataTemplateHibernate;
import ru.otus.domain.core.repository.HibernateUtils;
import ru.otus.domain.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.domain.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.domain.crm.model.Address;
import ru.otus.domain.crm.model.Client;
import ru.otus.domain.crm.model.Phone;
import ru.otus.domain.crm.model.Role;
import ru.otus.domain.crm.service.DBServiceClient;
import ru.otus.domain.crm.service.DbServiceClientImpl;
import ru.otus.server.server.ClientsWebServer;
import ru.otus.server.server.ClientsWebServerWithBasicSecurity;
import ru.otus.server.services.InMemoryLoginServiceImpl;
import ru.otus.server.services.TemplateProcessor;
import ru.otus.server.services.TemplateProcessorImpl;

import java.util.List;

public class WebServerWithBasicSecurityDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/server/templates/";

    public static final String HIBERNATE_CFG_FILE = "/domain/hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class, Role.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        ///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        ///
        var myCache = new MyCache<String, Client>();
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate, myCache);

        //Метод для заполнения БД (добавятся 3 клиента, один из них админ)
//        loadDbServiceClient(dbServiceClient);

        Gson gson = new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        LoginService loginService = new InMemoryLoginServiceImpl(dbServiceClient);

        ClientsWebServer clientsWebServer =
                new ClientsWebServerWithBasicSecurity(WEB_SERVER_PORT, loginService, dbServiceClient, gson, templateProcessor);

        clientsWebServer.start();
        clientsWebServer.join();
    }

    private static void loadDbServiceClient(DBServiceClient dbServiceClient) {
        var admin = new Client(
                null,
                "Ruslan",
                "Ruslan",
                List.of(new Role(null, "admin")),
                new Address(null, "RuslanStreet"),
                List.of(new Phone(null, "13-555-22"), new Phone(null, "14-666-333"))
        );
        var client1 = new Client(
                null,
                "Sergey",
                "Sergey",
                List.of(new Role(null, "user")),
                new Address(null, "SergeyStreet"),
                List.of(new Phone(null, "10-155-22"), new Phone(null, "13-626-399"))
        );
        var client2 = new Client(
                null,
                "Aleksey",
                "Aleksey",
                List.of(new Role(null, "user")),
                new Address(null, "AlekseyStreet"),
                List.of(new Phone(null, "13-523-21"), new Phone(null, "19-776-355"))
        );
        dbServiceClient.saveClient(admin);
        dbServiceClient.saveClient(client1);
        dbServiceClient.saveClient(client2);
    }
}
