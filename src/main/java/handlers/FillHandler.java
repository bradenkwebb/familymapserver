package handlers;

import com.sun.net.httpserver.HttpExchange;
import results.Result;
import services.FillService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Logger;

public class FillHandler implements Handler {
    public static final Logger logger = Logger.getLogger("FillHandler");

    /** The location to look for the username in the HTTP request URI */
    private static final int USERNAME_INDEX = 2;

    /**
     * The location to look for the parameter corresponding to the number of generations to fill
     * in the HTTP request URI
     */
    private static final int NUM_GEN_INDEX = 3;

    /** The default number of generations to fill, if not told otherwise */
    private static final int DEFAULT_NUM_GEN = 4;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.entering("FillHandler", "handle");
        Result result = new Result();
        int statusCode = HttpURLConnection.HTTP_BAD_REQUEST;

        if (!exchange.getRequestMethod().equalsIgnoreCase("post")) {
            statusCode = HttpURLConnection.HTTP_BAD_METHOD;
            result.setMessage("Error: Invalid request method");
        } else {
            String urlPath = exchange.getRequestURI().toString();
            Integer numGen;

            String[] urlParts = urlPath.split("/");
            String username = urlParts[USERNAME_INDEX];
            numGen = getNumGenerations(urlParts);

            logger.finest(urlPath);
            logger.finest("username: " + username);
            logger.finest("numGen: " + numGen);
            if (numGen == null) {
                result.setMessage("Error: Invalid generations parameter");
            } else {

                // Only fill this result if we have a valid URL
                result = new FillService().fill(username, numGen);

                if (result.isSuccess()) {
                    statusCode = HttpURLConnection.HTTP_OK;
                } else {
                    statusCode = HttpURLConnection.HTTP_INTERNAL_ERROR;
                }
            }
        }

        exchange.sendResponseHeaders(statusCode, 0);
        writeString(serialize(result), exchange.getResponseBody());
        exchange.getResponseBody().close();
    }

    private Integer getNumGenerations(String[] urlParts) {
        int numGen;
        if (urlParts.length > NUM_GEN_INDEX + 1) {
            return null;
        } else if (urlParts.length == NUM_GEN_INDEX + 1) {
            numGen = Integer.parseInt(urlParts[NUM_GEN_INDEX]);
            return (numGen >= 0) ? numGen : null;
        }
        return DEFAULT_NUM_GEN;
    }

}
