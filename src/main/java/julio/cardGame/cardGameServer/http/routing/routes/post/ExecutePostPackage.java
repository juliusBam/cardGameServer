package julio.cardGame.cardGameServer.http.routing.routes.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.controllers.AuthenticationController;
import julio.cardGame.cardGameServer.database.db.DbConnection;
import julio.cardGame.cardGameServer.database.repositories.CardRepo;
import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;
import julio.cardGame.cardGameServer.http.routing.routes.Routeable;
import julio.cardGame.cardGameServer.http.communication.DefaultMessages;
import julio.cardGame.cardGameServer.http.communication.HttpStatus;
import julio.cardGame.cardGameServer.database.models.CardDbModel;
import julio.cardGame.cardGameServer.database.models.CardRequestModel;
import julio.cardGame.cardGameServer.services.CardGameService;
import julio.cardGame.cardGameServer.services.PostPackageService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExecutePostPackage implements Routeable {

    private final CardGameService postPackageService;

    public ExecutePostPackage() {
        this.postPackageService = new PostPackageService();
    }

    @Override
    public Response process(RequestContext requestContext) {

        try {

            AuthorizationWrapper authorizationWrapper = AuthenticationController.requireAdmin(requestContext.getHeaders());

            if (authorizationWrapper.response != null)
                return authorizationWrapper.response;

            return new Response(postPackageService.execute(requestContext), HttpStatus.CREATED);

        } catch (JsonProcessingException e) {

            return new Response(DefaultMessages.ERR_JSON_PARSE_PACKAGE.getMessage(), e);

        } catch (SQLException e) {

            return new Response(e);

        }

    }
}
