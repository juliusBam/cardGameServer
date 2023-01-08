package julio.cardGame.cardGameClient.services.Actions;

import com.fasterxml.jackson.core.JsonProcessingException;

import javax.security.auth.login.CredentialException;
import java.io.IOException;
import java.net.http.HttpRequest;

public interface Action {
    public HttpRequest executeAction() throws IOException, CredentialException;
}
