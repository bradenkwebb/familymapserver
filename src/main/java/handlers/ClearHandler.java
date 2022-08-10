package handlers;

import com.sun.net.httpserver.HttpExchange;
import results.Result;
import services.ClearService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Logger;

public class ClearHandler implements Handler {

    private static final Logger logger = Logger.getLogger("ClearHandler");

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.entering("ClearHandler", "handle");

        Result result = new ClearService().clear();

        int statusCode = (result.isSuccess()) ? HttpURLConnection.HTTP_OK : HttpURLConnection.HTTP_BAD_REQUEST;

        exchange.sendResponseHeaders(statusCode, 0);
        writeString(serialize(result), exchange.getResponseBody());
        exchange.getResponseBody().close();
    }
}
