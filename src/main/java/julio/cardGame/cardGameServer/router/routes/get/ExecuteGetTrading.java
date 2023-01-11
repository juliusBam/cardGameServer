package julio.cardGame.cardGameServer.router.routes.get;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.application.serverLogic.db.DbConnection;
import julio.cardGame.cardGameServer.http.RequestContext;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.cardGameServer.router.AuthenticatedRoute;
import julio.cardGame.cardGameServer.router.AuthorizationWrapper;
import julio.cardGame.cardGameServer.router.Routeable;
import julio.cardGame.common.DefaultMessages;
import julio.cardGame.common.HttpStatus;
import julio.cardGame.cardGameServer.application.serverLogic.models.TradeViewModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExecuteGetTrading extends AuthenticatedRoute implements Routeable {
    @Override
    public Response process(RequestContext requestContext) {

        //todo fetch from db

        AuthorizationWrapper auth = this.requireAuthToken(requestContext.getHeaders());

        if (auth.response != null)
            return auth.response;

        try (Connection connection = DbConnection.getInstance().connect()) {


            String body = this.fetchTrades(connection);

            return new Response(body, HttpStatus.OK);


        } catch (SQLException e) {
            return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (JsonProcessingException e) {
            return new Response(DefaultMessages.ERR_JSON_PARSE_TRADE.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private String fetchTrades(Connection connection) throws JsonProcessingException, SQLException {

        String sql = """
                        SELECT t.* 
                            FROM public."tradesMetaData" t;
                    """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            List<TradeViewModel> trades = new ArrayList<>();

            while (resultSet.next()) {
                trades.add(
                        new TradeViewModel(
                                resultSet.getString(1),
                                resultSet.getInt(2),
                                resultSet.getString(3),
                                resultSet.getString(4),
                                resultSet.getString(5),
                                resultSet.getDouble(6)
                        )
                );
            }

            if (trades.size() == 0)
                return DefaultMessages.NO_TRADES.getMessage();

            try {

                String body = new ObjectMapper()
                        .writerWithDefaultPrettyPrinter()
                        .writeValueAsString(trades);

                return body;

            } catch (JsonProcessingException e) {
                throw e;
            }

        } catch (SQLException e) {
            throw e;
        }


    }


}
