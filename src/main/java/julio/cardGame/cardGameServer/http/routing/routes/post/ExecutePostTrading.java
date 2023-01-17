package julio.cardGame.cardGameServer.http.routing.routes.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.database.db.DataTransformation;
import julio.cardGame.cardGameServer.database.db.DbConnection;
import julio.cardGame.cardGameServer.database.repositories.TradeRepo;
import julio.cardGame.cardGameServer.database.repositories.UserRepo;
import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;
import julio.cardGame.cardGameServer.http.routing.routes.AuthenticatedRoute;
import julio.cardGame.cardGameServer.http.routing.routes.Routeable;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.battle.cards.CardTypes;
import julio.cardGame.cardGameServer.http.communication.DefaultMessages;
import julio.cardGame.cardGameServer.http.communication.HttpStatus;
import julio.cardGame.cardGameServer.http.communication.RequestParameters;
import julio.cardGame.cardGameServer.database.models.TradeModel;

import java.sql.*;
import java.util.UUID;

public class ExecutePostTrading extends AuthenticatedRoute implements Routeable {
    @Override
    public Response process(RequestContext requestContext) {

        AuthorizationWrapper auth;

        try {

            auth = this.requireAuthToken(requestContext.getHeaders());

        } catch (SQLException e) {
            return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (auth.response != null)
            return auth.response;


        if (requestContext.fetchParameter(RequestParameters.SELECTED_TRADE_DEAL.getParamValue()) == null) {

            return this.executePostNewDeal(requestContext, auth.userName);

        } else {

            return this.executeAcceptDeal(requestContext, auth.userName);

        }

    }

    public Response executePostNewDeal(RequestContext requestContext, String userName) {
        try {

            TradeModel tradeModel = new ObjectMapper()
                    .readValue(requestContext.getBody(), TradeModel.class);

            CardTypes requiredType = DataTransformation.convertIntoCardType(tradeModel.type);

            if (requiredType == null)
                throw new IllegalArgumentException();

            try (Connection dbConnection = DbConnection.getInstance().connect()) {

                TradeRepo tradeRepo = new TradeRepo();

                UserRepo userRepo = new UserRepo();

                //check if card belongs to user
                if (!userRepo.checkIfOwnsCard(dbConnection, userName, tradeModel.cardToTrade))
                    return new Response(DefaultMessages.ERR_CARD_NOT_OWNED.getMessage(), HttpStatus.BAD_REQUEST);

                //execute insert trade deal
                tradeRepo.insertNewTradeDeal(dbConnection, userName, tradeModel, requiredType);

                return new Response(HttpStatus.CREATED.getStatusMessage(), HttpStatus.CREATED);

            } catch (SQLException e) {
                return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (JsonProcessingException | IllegalArgumentException e) {

            return new Response(DefaultMessages.ERR_JSON_PARSE_TRADE.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    public Response executeAcceptDeal(RequestContext requestContext, String userName) {

        try {

            UUID tradeUUID = UUID.fromString(requestContext.fetchParameter(RequestParameters.SELECTED_TRADE_DEAL.getParamValue()));

            UUID cardUUID = UUID.fromString(requestContext.getBody().replace("\"",""));

            try (Connection dbConnection = DbConnection.getInstance().connect()) {

                TradeRepo tradeRepo = new TradeRepo();

                UserRepo userRepo = new UserRepo();

                //first check if card belongs to user
                if (!userRepo.checkIfOwnsCard(dbConnection, userName, cardUUID)) {
                    return new Response(DefaultMessages.ERR_CARD_NOT_OWNED.getMessage(), HttpStatus.BAD_REQUEST);
                }

                //check if the card fulfills the trade criteria
                if (!tradeRepo.checkIfCardFulfillsTradeReq(dbConnection, cardUUID, tradeUUID)) {
                    return new Response(DefaultMessages.ERR_CARD_NOT_VALID.getMessage(), HttpStatus.BAD_REQUEST);
                }

                //check if self trading
                if (tradeRepo.checkIfSelfTrade(dbConnection, tradeUUID, userName)) {
                    return new Response(DefaultMessages.ERR_NO_SELF_TRADE.getMessage(), HttpStatus.BAD_REQUEST);
                }

                //start transaction
                dbConnection.setAutoCommit(false);

                try {

                    //card in trade --> logged user ID will become new owner
                    tradeRepo.changeOwnershipCardInTrade(dbConnection, userName, tradeUUID);

                    //offered card --> user ID of the trade will become new owner
                    tradeRepo.changeOwnershipOfferedCard(dbConnection, tradeUUID, cardUUID);

                    //if both succeeded delete trade deal
                    tradeRepo.deleteTrade(dbConnection, tradeUUID);

                    dbConnection.commit();

                } catch (SQLException e) {
                    dbConnection.rollback();
                    return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                }

                return new Response(HttpStatus.OK.getStatusMessage(), HttpStatus.OK);

            } catch (SQLException e) {
                return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (IllegalArgumentException e) {

            return new Response(DefaultMessages.ERR_INVALID_TRADE_UUID.getMessage(), HttpStatus.BAD_REQUEST);

        }
    }

}
