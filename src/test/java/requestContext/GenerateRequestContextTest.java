package requestContext;

import julio.cardGame.cardGameServer.http.communication.ClientExecutor;
import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.communication.RequestParameters;
import julio.cardGame.cardGameServer.http.routing.HttpPath;
import julio.cardGame.cardGameServer.http.routing.HttpVerb;
import julio.cardGame.cardGameServer.http.routing.router.RouteIdentifier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GenerateRequestContextTest {

    private ClientExecutor clientExecutor;

    private RouteIdentifier routeIdentifier;

    @BeforeAll
    void setup() {
        this.clientExecutor = new ClientExecutor(null);
    }

    @Test
    public void testPostSessions() throws IOException {

        //arrange
        String postSessionsString = "POST /sessions HTTP/1.1";
        RouteIdentifier expectedIdentifier = new RouteIdentifier(HttpPath.SESSIONS.getPath(), HttpVerb.POST.getVerb());
        RequestContext requestContext = new RequestContext();

        //act
        this.clientExecutor.parseEndPoint(requestContext, postSessionsString);

        //
        assertEquals(expectedIdentifier, new RouteIdentifier(requestContext.getPath(), requestContext.getHttpVerb()));

    }

    @Test
    public void testGetUserSetParam() throws IOException {

        //arrange
        String expectedUsername = "userName";
        String getUserString = "GET /users/"+ expectedUsername + " HTTP/1.1";
        RouteIdentifier expectedIdentifier = new RouteIdentifier(HttpPath.USERS.getPath(), HttpVerb.GET.getVerb());
        RequestContext requestContext = new RequestContext();

        //act
        this.clientExecutor.parseEndPoint(requestContext, getUserString);

        //assert
        assertEquals(expectedIdentifier, new RouteIdentifier(requestContext.getPath(), requestContext.getHttpVerb()));
        assertEquals(requestContext.fetchParameter(RequestParameters.USERNAME.getParamValue()), expectedUsername);

    }

}
