package julio.cardGame.cardGameServer.http;

import java.io.BufferedWriter;
import java.io.IOException;

public interface Sendable {
    public void sendResponse(BufferedWriter bufferedWriter) throws IOException;
}
