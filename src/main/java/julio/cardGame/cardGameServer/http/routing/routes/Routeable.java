package julio.cardGame.cardGameServer.http.routing.routes;

import com.fasterxml.jackson.core.JsonProcessingException;
import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.communication.Response;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public interface Routeable {
    Response process(RequestContext requestContext) throws JsonProcessingException, NoSuchAlgorithmException, SQLException;

}
