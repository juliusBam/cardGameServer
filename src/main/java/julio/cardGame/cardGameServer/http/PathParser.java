package julio.cardGame.cardGameServer.http;

import julio.cardGame.common.RequestParameters;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class PathParser {
    public static void parsePath(RequestContext requestContext, String rawHeader) throws UnsupportedEncodingException {
        String[] splittedPath = rawHeader.split("\\?");

        //first we check if there are query params
        if (splittedPath.length > 1) {
            //
            String queryParams = splittedPath[1];

            for (String param : queryParams.split("&")) {

                String[] keyValuePair = param.split("=");
                String key = URLDecoder.decode(keyValuePair[0], "UTF-8");
                String value = "";
                if (keyValuePair.length > 1) {
                    value = URLDecoder.decode(keyValuePair[1], "UTF-8");
                }

                requestContext.addParameter(key, value);
            }

        }

        //now we check if the users/username is used, because we handle it as a query param
        String[] splittedUrl = splittedPath[0].split("/");
        String firstPath = "/" + splittedUrl[1];
        if (splittedUrl.length > 2 && firstPath.equals(HttpPath.USERS.getPath())) {

            requestContext.addParameter(RequestParameters.USERNAME.getParamValue(),splittedUrl[2]);
            requestContext.setPath(firstPath);

        } else if (splittedUrl.length > 2 && firstPath.equals(HttpPath.TRADINGS.getPath())) {

            requestContext.addParameter(RequestParameters.SELECTED_TRADE_DEAL.getParamValue(), splittedUrl[2]);
            requestContext.setPath(firstPath);

        }else {
            requestContext.setPath(splittedPath[0]);
        }
    }
}
