package ru.otus.domain.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.domain.cachehw.MyCache;
import ru.otus.domain.core.repository.DataTemplateHibernate;
import ru.otus.domain.core.repository.HibernateUtils;
import ru.otus.domain.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.domain.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.domain.crm.model.Address;
import ru.otus.domain.crm.model.Client;
import ru.otus.domain.crm.model.Phone;
import ru.otus.domain.crm.service.DbServiceClientImpl;

/**
 * VM options -Xmx16m -Xms16m -Xlog:gc=debug
 */
public class DbServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "/domain/hibernate.cfg.xml";

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        ///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        ///
        var myCache = new MyCache<String, Client>();
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate, myCache);

        var clientFirst = dbServiceClient.saveClient(new Client("dbServiceFirst"));
        log.info("current cash size: {}", myCache.size());
        myCache.cacheInfo();

        var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond"));
        log.info("current cash size: {}", myCache.size());
        myCache.cacheInfo();

        var originTimeCache = System.nanoTime();
        var clientSecondSelected = dbServiceClient
                .getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        var timeCache = System.nanoTime() - originTimeCache;
        log.info("Get client from cache time: {}", timeCache); // в кеше есть клиент, достаем его оттуда (почти мгновенно)
        log.info("clientSecondSelected:{}", clientSecondSelected);

        //После создания 3го объекта клиента, gc пойдет чистить хип, так как места уже будет не хватать.
        //Тогда наш кеш тоже почистится
        dbServiceClient.saveClient(new Client(clientSecondSelected.getId(), "dbServiceSecondUpdated"));
        log.info("current cash size: {}", myCache.size()); // будет содержать только последнего клиента
        myCache.cacheInfo();

        var originTimeDB = System.nanoTime();
        var clientFirstDB = dbServiceClient
                .getClient(clientFirst.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientFirst.getId()));
        var timeDB = System.nanoTime() - originTimeDB;
        log.info("Get client from db time: {}", timeDB); // в кеше нет нужного клиента, поэтому идем в базу (выходит гораздо дольше)

        log.info("Cache was faster than DB by: {} times", timeDB / timeCache);
    }
}
