package julio.cardGame.cardGameServer.http.routing.routes;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AuthenticatedMappingRoute extends AuthenticatedRoute {

    public final ObjectMapper objectMapper;

    public AuthenticatedMappingRoute() {this.objectMapper = new ObjectMapper();
    }
}
