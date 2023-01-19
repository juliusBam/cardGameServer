package julio.cardGame.cardGameServer.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import julio.cardGame.cardGameServer.http.communication.RequestContext;

import java.sql.SQLException;

public interface CardGameService {

    public String execute(RequestContext requestContext) throws JsonProcessingException, SQLException;

}
