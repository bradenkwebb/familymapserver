package handlers;

import com.sun.net.httpserver.HttpExchange;
import requests.FillRequest;
import results.FillResult;
import services.FillService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FillHandler implements Handler {
    public static final Logger logger = Logger.getLogger("FillHandler");

    /** The location to look for the username in the HTTP request URI */
    private final int USERNAME_INDEX = 2;

    /**
     * The location to look for the parameter corresponding to the number of generations to fill
     * in the HTTP request URI
     */
    private final int NUM_GEN_INDEX = 3;

    /** The default number of generations to fill, if not told otherwise */
    private final int DEFAULT_NUM_GEN = 4;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.entering("FillHandler", "handle");
        boolean success = false;
        FillResult result = new FillResult();
        int statusCode = HttpURLConnection.HTTP_BAD_REQUEST;

        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                String urlPath = exchange.getRequestURI().toString();
                int numGen;

                logger.finest(urlPath);
                String[] parts = urlPath.split("/");
                if (parts.length > NUM_GEN_INDEX + 1) {
                    exchange.sendResponseHeaders(statusCode, 0);
                    exchange.getResponseBody().close();
                    return;
                } else if (parts.length == NUM_GEN_INDEX + 1) {
                    numGen = Integer.parseInt(parts[NUM_GEN_INDEX]);
                    if (numGen < 0) {
                        result.setSuccess(false);
                        result.setMessage("Error: Invalid generations parameter");
                    }
                } else {
                    numGen = DEFAULT_NUM_GEN;
                }

                String username = parts[USERNAME_INDEX];

                logger.finest("username: " + username);
                logger.finest("numGen: " + Integer.toString(numGen));

                FillRequest request = (FillRequest) deserialize(exchange.getRequestBody(), FillRequest.class);
                FillService service = new FillService();
                result = service.fill(request, username, numGen);

                success = result.isSuccess();
                if (success) {
                    statusCode = HttpURLConnection.HTTP_OK;
                } else {
                    statusCode = HttpURLConnection.HTTP_INTERNAL_ERROR;
                }
            } else {
                statusCode = HttpURLConnection.HTTP_BAD_METHOD;
                result.setMessage("Error: Invalid request method");
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            success = false;
            result.setMessage("Error: " + e.getMessage());
        }
        result.setSuccess(success);
        exchange.sendResponseHeaders(statusCode, 0);
        writeString(serialize(result), exchange.getResponseBody());
        exchange.getResponseBody().close();
    }
}
