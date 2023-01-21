package julio.cardGame.cardGameServer.http.routing.routes;

import com.fasterxml.jackson.core.JsonProcessingException;
import julio.cardGame.cardGameServer.controllers.AuthenticationController;
import julio.cardGame.cardGameServer.http.communication.*;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;
import julio.cardGame.cardGameServer.services.CardGameService;

import java.sql.SQLException;

public abstract class ServiceableRoute {
    public ServiceableRoute() {
        this.service = this.initiateCardGameService();
    }

    protected CardGameService service;

    //defined to force children to implement the method and set the CardGameService
    protected abstract CardGameService initiateCardGameService();

    protected Response executeAuthenticatedService(RequestContext requestContext, String jsonErrMsg) {

        try {

            AuthorizationWrapper auth = AuthenticationController.requireAuthToken(requestContext.getHeaders());

            if (auth.response != null)
                return auth.response;

            return this.service.execute(requestContext, auth);

        } catch (SQLException e) {

            return new Response(e);

        } catch (JsonProcessingException e) {

            return new Response(jsonErrMsg == null ? e.getMessage() : jsonErrMsg, e);

        } catch (Exception e) {

            return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    protected Response executeAccessProtectedDataService(RequestContext requestContext, String jsonErrMsg) {

        try {

            //extract the selected user
            String requestedUser = requestContext.fetchParameter(RequestParameters.USERNAME.getParamValue());

            if (requestedUser == null || requestedUser.isEmpty() || requestedUser.isBlank())
                return new Response(DefaultMessages.ERR_NO_USER.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

            //check if admin or requestedUser == logged user
            AuthorizationWrapper authorizationWrapper = AuthenticationController.canAccessUserData(requestContext.getHeaders(), requestedUser);

            if (authorizationWrapper.response != null)
                return authorizationWrapper.response;

            return this.service.execute(requestContext, authorizationWrapper);

        } catch (SQLException e) {

            return new Response(e);

        } catch (JsonProcessingException e) {

            return new Response(jsonErrMsg == null ? e.getMessage() : jsonErrMsg, e);

        } catch (Exception e) {

            return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    protected Response executeAdminService(RequestContext requestContext, String jsonErrMsg) {

        try {

            AuthorizationWrapper auth = AuthenticationController.requireAdmin(requestContext.getHeaders());

            if (auth.response != null)
                return auth.response;

            return this.service.execute(requestContext, auth);

        } catch (SQLException e) {

            return new Response(e);

        } catch (JsonProcessingException e) {

            return new Response(jsonErrMsg == null ? e.getMessage() : jsonErrMsg, e);

        } catch (Exception e) {

            return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    protected Response executeService(RequestContext requestContext, String jsonErrMsg) {

        try {

            return this.service.execute(requestContext, null);

        } catch (SQLException e) {

            return new Response(e);

        } catch (JsonProcessingException e) {

            return new Response(jsonErrMsg == null ? e.getMessage() : jsonErrMsg, e);

        } catch (Exception e) {

            return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

}
