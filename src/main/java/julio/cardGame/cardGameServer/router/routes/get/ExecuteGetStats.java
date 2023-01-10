package julio.cardGame.cardGameServer.router.routes.get;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.application.serverLogic.db.DbConnection;
import julio.cardGame.cardGameServer.http.RequestContext;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.cardGameServer.router.AuthorizationWrapper;
import julio.cardGame.cardGameServer.router.AuthenticatedRoute;
import julio.cardGame.cardGameServer.router.Routeable;
import julio.cardGame.common.DefaultMessages;
import julio.cardGame.common.HttpStatus;
import julio.cardGame.common.models.StatsModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExecuteGetStats extends AuthenticatedRoute implements Routeable {
    @Override
    public Response process(RequestContext requestContext) {

        AuthorizationWrapper auth = this.requireAuthToken(requestContext.getHeaders());

        if (auth.response != null)
            return auth.response;

        String sql = """
                    SELECT wins, losses, elo
                        FROM users
                            WHERE "userName"=?;
                """;

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(sql)){

            preparedStatement.setString(1, auth.userName);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                preparedStatement.close();
                return new Response(HttpStatus.BAD_REQUEST.getStatusMessage(), HttpStatus.BAD_REQUEST);
            }

            int wins = resultSet.getInt(1);
            int losses = resultSet.getInt(2);
            int elo = resultSet.getInt(3);
            double winRate = 0;

            if (wins + losses != 0 || wins != 0)
                winRate = (double) (wins / (wins + losses));

            //todo change variables to stored
            StatsModel statsModel = new StatsModel(
                resultSet.getInt(1),
                    resultSet.getInt(2),
                    winRate,
                    resultSet.getInt(3)
            );

            String body = new ObjectMapper()
                    .writeValueAsString(statsModel);

            return new Response(body, HttpStatus.OK);

        } catch (JsonProcessingException e) {
            return new Response(DefaultMessages.ERR_JSON_PARSE_STATS.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (SQLException e) {
            return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
