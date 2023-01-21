package julio.cardGame.cardGameServer.services.tradingServices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.battle.cards.CardTypes;
import julio.cardGame.cardGameServer.database.DataTransformation;
import julio.cardGame.cardGameServer.database.DbConnection;
import julio.cardGame.cardGameServer.models.TradeModel;
import julio.cardGame.cardGameServer.repositories.CardRepo;
import julio.cardGame.cardGameServer.repositories.TradeRepo;
import julio.cardGame.cardGameServer.repositories.UserRepo;
import julio.cardGame.cardGameServer.http.communication.*;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;
import julio.cardGame.cardGameServer.services.CardGameService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public class PostTradingService implements CardGameService {
    @Override
    public Response execute(RequestContext requestContext, AuthorizationWrapper authorizationWrapper) throws JsonProcessingException, SQLException {

        if (requestContext.fetchParameter(RequestParameters.SELECTED_TRADE_DEAL.getParamValue()) == null) {

            return this.executePostNewDeal(requestContext, authorizationWrapper.userName);

        } else {

            return this.executeAcceptDeal(requestContext, authorizationWrapper.userName);

        }

    }


    public Response executePostNewDeal(RequestContext requestContext, String userName) throws SQLException, JsonProcessingException {

        TradeModel tradeModel = new ObjectMapper()
                .readValue(requestContext.getBody(), TradeModel.class);

        CardTypes requiredType = DataTransformation.convertIntoCardType(tradeModel.type);

        if (requiredType == null)
            return new Response(DefaultMessages.ERR_JSON_PARSE_TRADE.getMessage(), HttpStatus.BAD_REQUEST);

        UserRepo userRepo = new UserRepo();

        TradeRepo tradeRepo = new TradeRepo();

        CardRepo cardRepo = new CardRepo();

        try (Connection dbConnection = DbConnection.getInstance().connect()) {

            //check if card belongs to user
            if (!userRepo.checkIfOwnsCard(dbConnection, userName, tradeModel.cardToTrade))
                return new Response(DefaultMessages.ERR_CARD_NOT_OWNED.getMessage(), HttpStatus.BAD_REQUEST);

            if (!cardRepo.checkIfCardInDeck(dbConnection, tradeModel.cardToTrade))
                return new Response(DefaultMessages.ERR_CARD_NOT_OWNED.getMessage(), HttpStatus.BAD_REQUEST);

            //execute insert trade deal
            tradeRepo.insertNewTradeDeal(dbConnection, userName, tradeModel, requiredType);

            return new Response(HttpStatus.CREATED.getStatusMessage(), HttpStatus.CREATED);

        }

    }

    public Response executeAcceptDeal(RequestContext requestContext, String userName) throws SQLException {

        try {

            UUID tradeUUID = UUID.fromString(requestContext.fetchParameter(RequestParameters.SELECTED_TRADE_DEAL.getParamValue()));

            UUID cardUUID = UUID.fromString(requestContext.getBody().replace("\"",""));

            try (Connection dbConnection = DbConnection.getInstance().connect()) {

                UserRepo userRepo = new UserRepo();

                //first check if card belongs to user
                if (!userRepo.checkIfOwnsCard(dbConnection, userName, cardUUID)) {
                    return new Response(DefaultMessages.ERR_CARD_NOT_OWNED.getMessage(), HttpStatus.BAD_REQUEST);
                }

                TradeRepo tradeRepo = new TradeRepo();

                //check if the card fulfills the trade criteria
                if (!tradeRepo.checkIfCardFulfillsTradeReq(dbConnection, cardUUID, tradeUUID)) {
                    return new Response(DefaultMessages.ERR_CARD_NOT_VALID.getMessage(), HttpStatus.BAD_REQUEST);
                }

                //check if self trading
                if (tradeRepo.checkIfSelfTrade(dbConnection, tradeUUID, userName)) {
                    return new Response(DefaultMessages.ERR_NO_SELF_TRADE.getMessage(), HttpStatus.BAD_REQUEST);
                }

                CardRepo cardRepo = new CardRepo();

                //start transaction
                dbConnection.setAutoCommit(false);

                try {

                    //card in trade --> logged user ID will become new owner
                    cardRepo.changeOwnershipCardInTrade(dbConnection, userName, tradeUUID);

                    //offered card --> user ID of the trade will become new owner
                    cardRepo.changeOwnershipOfferedCard(dbConnection, tradeUUID, cardUUID);

                    //if both succeeded delete trade deal
                    tradeRepo.deleteTrade(dbConnection, tradeUUID);

                    dbConnection.commit();

                } catch (SQLException e) {
                    dbConnection.rollback();

                    return new Response(e);

                }

                return new Response(HttpStatus.OK.getStatusMessage(), HttpStatus.OK);

            }

        } catch (IllegalArgumentException e) {

            return new Response(DefaultMessages.ERR_INVALID_TRADE_UUID.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }
}
