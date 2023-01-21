package router.test;

import julio.cardGame.cardGameServer.http.routing.HttpPath;
import julio.cardGame.cardGameServer.http.routing.HttpVerb;
import julio.cardGame.cardGameServer.http.routing.router.FunctionalRouter;
import julio.cardGame.cardGameServer.http.routing.router.RouteIdentifier;
import julio.cardGame.cardGameServer.http.routing.routes.Routeable;
import julio.cardGame.cardGameServer.http.routing.routes.delete.ExecuteDeleteTrading;
import julio.cardGame.cardGameServer.http.routing.routes.get.*;
import julio.cardGame.cardGameServer.http.routing.routes.post.*;
import julio.cardGame.cardGameServer.http.routing.routes.put.ExecutePutActivate;
import julio.cardGame.cardGameServer.http.routing.routes.put.ExecutePutDeactivate;
import julio.cardGame.cardGameServer.http.routing.routes.put.ExecutePutDeck;
import julio.cardGame.cardGameServer.http.routing.routes.put.ExecutePutUser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FetchRoutesParameterTest {

    public static FunctionalRouter functionalRouter;

    @BeforeAll
    public static void initialize() {
        functionalRouter = new FunctionalRouter();
    }

    @ParameterizedTest
    @ArgumentsSource(RoutesArgumentProvider.class)
    public void testArgumentsSource(RouteIdentifier route, Routeable expectedRoute) {
        assertEquals(expectedRoute.getClass(), functionalRouter.findRoute(route).generateRoute().getClass());
    }

    public static class RoutesArgumentProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of(new RouteIdentifier(HttpPath.USERS.getPath(), HttpVerb.POST.getVerb()), new ExecutePostUser()),
                    Arguments.of(new RouteIdentifier(HttpPath.USERS.getPath(), HttpVerb.GET.getVerb()), new ExecuteGetUser()),
                    Arguments.of(new RouteIdentifier(HttpPath.USERS.getPath(), HttpVerb.PUT.getVerb()), new ExecutePutUser()),
                    Arguments.of(new RouteIdentifier(HttpPath.SESSIONS.getPath(), HttpVerb.POST.getVerb()), new ExecutePostSession()),
                    Arguments.of(new RouteIdentifier(HttpPath.PACKAGES.getPath(), HttpVerb.POST.getVerb()), new ExecutePostPackage()),
                    Arguments.of(new RouteIdentifier(HttpPath.TRANSACTIONS_PACKAGES.getPath(), HttpVerb.POST.getVerb()), new ExecutePostTransactionPackage()),
                    Arguments.of(new RouteIdentifier(HttpPath.TRADINGS.getPath(), HttpVerb.GET.getVerb()), new ExecuteGetTrading()),
                    Arguments.of(new RouteIdentifier(HttpPath.TRADINGS.getPath(), HttpVerb.POST.getVerb()), new ExecutePostTrading()),
                    Arguments.of(new RouteIdentifier(HttpPath.TRADINGS.getPath(), HttpVerb.DELETE.getVerb()), new ExecuteDeleteTrading()),
                    Arguments.of(new RouteIdentifier(HttpPath.CARDS.getPath(), HttpVerb.GET.getVerb()), new ExecuteGetCard()),
                    Arguments.of(new RouteIdentifier(HttpPath.DECK.getPath(), HttpVerb.GET.getVerb()), new ExecuteGetDeck()),
                    Arguments.of(new RouteIdentifier(HttpPath.DECK.getPath(), HttpVerb.PUT.getVerb()), new ExecutePutDeck()),
                    Arguments.of(new RouteIdentifier(HttpPath.STATS.getPath(), HttpVerb.GET.getVerb()), new ExecuteGetStats()),
                    Arguments.of(new RouteIdentifier(HttpPath.SCORE.getPath(), HttpVerb.GET.getVerb()), new ExecuteGetScore()),
                    Arguments.of(new RouteIdentifier(HttpPath.BATTLES.getPath(), HttpVerb.POST.getVerb()), new ExecutePostBattle()),
                    Arguments.of(new RouteIdentifier(HttpPath.ACTIVATE.getPath(), HttpVerb.PUT.getVerb()), new ExecutePutActivate()),
                    Arguments.of(new RouteIdentifier(HttpPath.DEACTIVATE.getPath(), HttpVerb.PUT.getVerb()), new ExecutePutDeactivate())
            );
        }
    }

}
