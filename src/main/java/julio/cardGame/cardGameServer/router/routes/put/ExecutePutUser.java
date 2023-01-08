package julio.cardGame.cardGameServer.router.routes.put;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.http.RequestContext;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.cardGameServer.router.Route;
import julio.cardGame.common.DefaultMessages;
import julio.cardGame.common.HttpStatus;
import julio.cardGame.common.RequestParameters;
import julio.cardGame.common.models.UserAdditionalDataModel;

public class ExecutePutUser implements Route {
    @Override
    public Response process(RequestContext requestContext) {

        try {

            UserAdditionalDataModel userModel = new ObjectMapper()
                    .readValue(requestContext.getBody(), UserAdditionalDataModel.class);

            String requestedUser = requestContext.fetchParameter(RequestParameters.USERNAME.getParamValue());

            if (requestedUser == null || requestedUser.isEmpty() || requestedUser.isBlank()) {
                return new Response(DefaultMessages.ERR_NO_USER.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            //todo db logic

            return new Response(HttpStatus.OK.getStatusMessage(), HttpStatus.OK);

        } catch (JsonProcessingException e) {
            return new Response(DefaultMessages.ERR_JSON_PARSE_USER.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
