package julio.cardGame.cardGameServer.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.database.db.DbConnection;
import julio.cardGame.cardGameServer.database.models.CardDbModel;
import julio.cardGame.cardGameServer.database.models.CardRequestModel;
import julio.cardGame.cardGameServer.database.repositories.CardRepo;
import julio.cardGame.cardGameServer.database.repositories.PackageRepo;
import julio.cardGame.cardGameServer.http.communication.HttpStatus;
import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PostPackageService implements CardGameService {

    @Override
    public Response execute(RequestContext requestContext, AuthorizationWrapper authorizationWrapper) throws JsonProcessingException, SQLException {

        List<CardRequestModel> newPackage = new ObjectMapper()
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

                CardRepo cardRepo = new CardRepo();
                PackageRepo packageRepo = new PackageRepo();

                dbConn.setAutoCommit(false);

                cardRepo.addCards(dbConn, cardsToInsert);
                packageRepo.insertNewPackage(dbConn, cardIDs);

                dbConn.commit();

                return new Response(HttpStatus.CREATED.getStatusMessage(), HttpStatus.CREATED);

                //begin transaction, since we have to execute 2 sqls depending upon each other
            } catch (SQLException e) {
                //ugly catch, but we have to rollback
                dbConn.rollback();
                return new Response(e);

            }
        }
    }
}
