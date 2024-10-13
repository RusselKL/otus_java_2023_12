package ru.otus.server.services;

import org.eclipse.jetty.security.AbstractLoginService;
import org.eclipse.jetty.security.RolePrincipal;
import org.eclipse.jetty.security.UserPrincipal;
import org.eclipse.jetty.util.security.Password;
import ru.otus.domain.crm.model.Client;
import ru.otus.domain.crm.service.DBServiceClient;

import java.util.List;
import java.util.Optional;

public class InMemoryLoginServiceImpl extends AbstractLoginService {

    private final DBServiceClient serviceClient;

    public InMemoryLoginServiceImpl(DBServiceClient serviceClient) {
        this.serviceClient = serviceClient;
    }

    @Override
    protected List<RolePrincipal> loadRoleInfo(UserPrincipal userPrincipal) {
        Optional<Client> client = serviceClient.findClientBy("name", userPrincipal.getName());
        return client
                .map(Client::getRoles)
                .map(roles -> roles.stream().map(role -> new RolePrincipal(role.getRole())).toList())
                .orElse(List.of());
    }

    @Override
    protected UserPrincipal loadUserInfo(String login) {
        Optional<Client> client = serviceClient.findClientBy("name", login);
        return client
                .map(c -> new UserPrincipal(c.getName(), new Password(c.getPassword())))
                .orElse(null);
    }
}
