package backend.academy.apigateway.service;


import backend.academy.apigateway.security.EndPointRoute;

import java.util.List;

public interface EndPointRouteService {

    List<EndPointRoute> getRoutes();
}
