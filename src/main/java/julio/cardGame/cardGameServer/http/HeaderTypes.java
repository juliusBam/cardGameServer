package julio.cardGame.cardGameServer.http;

public enum HeaderTypes {

    CONTENT_TYPE("Content-Type"),

    CONTENT_LENGTH("Content-Length");

    private final String headerValue;

    HeaderTypes(String newHeaderValue) {
        this.headerValue = newHeaderValue;
    }

    public String getHeaderValue(){
        return this.headerValue;
    }

}
