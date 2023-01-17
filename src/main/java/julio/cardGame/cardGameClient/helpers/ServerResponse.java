package julio.cardGame.cardGameClient.helpers;

import julio.cardGame.cardGameServer.http.communication.HttpStatus;

public class ServerResponse {
    private String responseText;
    private HttpStatus responseStatus;
    private ClientAction executedAction;

    public ServerResponse(String responseText, HttpStatus responseStatus, ClientAction executedAction) {
        this.responseText = responseText;
        this.responseStatus = responseStatus;
        this.executedAction = executedAction;
    }

    public HttpStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(HttpStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    public ClientAction getExecutedAction() {
        return executedAction;
    }

    public void setExecutedAction(ClientAction executedAction) {
        this.executedAction = executedAction;
    }
}
