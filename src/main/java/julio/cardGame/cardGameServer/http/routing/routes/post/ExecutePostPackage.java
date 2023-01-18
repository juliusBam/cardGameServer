package julio.cardGame.cardGameServer.http.routing.routes.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.database.db.DbConnection;
import julio.cardGame.cardGameServer.database.repositories.CardRepo;
import julio.cardGame.cardGameServer.http.communication.headers.HeadersValidator;
import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;
import julio.cardGame.cardGameServer.http.routing.routes.AuthenticatedMappingRoute;
import julio.cardGame.cardGameServer.http.routing.routes.AuthenticatedRoute;
import julio.cardGame.cardGameServer.http.routing.routes.Routeable;
import julio.cardGame.cardGameServer.http.communication.DefaultMessages;
import julio.cardGame.cardGameServer.http.communication.HttpStatus;
import julio.cardGame.cardGameServer.database.models.CardDbModel;
import julio.cardGame.cardGameServer.database.models.CardRequestModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExecutePostPackage extends AuthenticatedMappingRoute implements Routeable {

    private final CardRepo cardRepo;

    public ExecutePostPackage() {
        this.cardRepo = new CardRepo();
    }

    @Override
    public Response process(RequestContext requestContext) {

        /*String authToken = HeadersValidator.validateToken(requestContext.getHeaders());

        if (authToken == null || !HeadersValidator.checkAdmin(authToken))
            return new Response(HttpStatus.UNAUTHORIZED.getStatusMessage(), HttpStatus.UNAUTHORIZED);*/

        try {

            AuthorizationWrapper authorizationWrapper = this.requireAdmin(requestContext.getHeaders());

            if (authorizationWrapper.response != null)
                return authorizationWrapper.response;

            List<CardRequestModel> newPackage = this.objectMapper
                    .readValue(requestContext.getBody(), new TypeReference<List<CardRequestModel>>() {
                    });

            List<CardDbModel> cardsToInsert = new ArrayList<>();
            List<UUID> cardIDs = new ArrayList<>();

            //converts the request object to a desired format
            for (CardRequestModel card : newPackage) {
                cardsToInsert.add(new CardDbModel(card));
                cardIDs.add(card.id);
            }

            //dbConnection has to be in a separeted try catch, so that the the rollback can be executed at line 74
            try (Connection dbConn = DbConnection.getInstance().connect()) {

                try {

                    //CardRepo cardRepo = new CardRepo();

                    dbConn.setAutoCommit(false);

                    cardRepo.addCards(dbConn, cardsToInsert);
                    cardRepo.createPackage(dbConn, cardIDs);

                    dbConn.commit();

                    //begin transaction, since we have to execute 2 sqls depending upon each other
                } catch (SQLException e) {
                    try {
                        dbConn.rollback();

                    } catch (SQLException ex) {

                        return new Response(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

                    }

                    return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

                }

                return new Response(HttpStatus.CREATED.getStatusMessage(), HttpStatus.CREATED);

            } catch (SQLException e) {

                return new Response(e);

            }

        } catch (JsonProcessingException e) {
            //throw new RuntimeException(e);
            return new Response(DefaultMessages.ERR_JSON_PARSE_PACKAGE.getMessage(), e);

        } catch (SQLException e) {

            //return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new Response(e);

        }

    }
}
