package backend.academy.apigateway.service.impl;


import backend.academy.apigateway.security.EndPointRoute;
import backend.academy.apigateway.service.EndPointRouteService;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class EndPointRouteServiceImpl implements EndPointRouteService {


    private List<EndPointRoute> routes;

    public EndPointRouteServiceImpl() {
        routes = new ArrayList<>();
        routes.add(new EndPointRoute("/register", HttpMethod.POST, null));
        routes.add(new EndPointRoute("/login", HttpMethod.POST, null));
        routes.add(new EndPointRoute("/registerAndLogin", HttpMethod.POST, null));
        routes.add(new EndPointRoute("/registerWithRole", HttpMethod.POST, Arrays.asList(new String[]{"ADMIN"})));
        routes.add(new EndPointRoute("/logout", HttpMethod.POST, null));
        routes.add(new EndPointRoute("/deleteUser", HttpMethod.POST, Arrays.asList(new String[]{"ADMIN"})));
        routes.add(new EndPointRoute("/changePassword", HttpMethod.POST, Arrays.asList(new String[]{"ADMIN", "USER"})));
        routes.add(new EndPointRoute("/getRoles", HttpMethod.POST,Arrays.asList(new String[]{"ADMIN"}) ));
        routes.add(new EndPointRoute("/addRoleToUser", HttpMethod.POST,Arrays.asList(new String[]{"ADMIN"}) ));
        routes.add(new EndPointRoute("/deleteRoleFromUser", HttpMethod.POST,Arrays.asList(new String[]{"ADMIN"}) ));
        routes.add(new EndPointRoute("/getAllUsers", HttpMethod.GET,Arrays.asList(new String[]{"ADMIN"}) ));
        routes.add(new EndPointRoute("/getUser", HttpMethod.POST,Arrays.asList(new String[]{"ADMIN"}) ));
        routes.add(new EndPointRoute("/addRole", HttpMethod.POST,Arrays.asList(new String[]{"ADMIN"}) ));
        routes.add(new EndPointRoute("/updateRole", HttpMethod.POST,Arrays.asList(new String[]{"ADMIN"}) ));
        routes.add(new EndPointRoute("/deleteRole", HttpMethod.POST,Arrays.asList(new String[]{"ADMIN"}) ));
        routes.add(new EndPointRoute("/getUsersByRole", HttpMethod.POST,Arrays.asList(new String[]{"ADMIN"}) ));
    }

    public List<EndPointRoute> getRoutes() {
        return new ArrayList<>(routes);
    }
}
