package handlers;

import com.sun.net.httpserver.HttpExchange;
import requests.ClearRequest;
import requests.RegisterRequest;
import results.Result;
import services.ClearService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClearHandler implements Handler {

    private static final Logger logger = Logger.getLogger("ClearHandler");

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.entering("ClearHandler", "handle");
        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                ClearRequest request = (ClearRequest) deserialize(exchange.getRequestBody(), RegisterRequest.class);
                ClearService service = new ClearService();
                Result result = service.clear(request);

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream responseBody = exchange.getResponseBody();
                writeString(serialize(result), responseBody);
                exchange.getResponseBody().close();
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
                exchange.getResponseBody().close();
            }
        } catch(IOException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            ex.printStackTrace();
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            exchange.getResponseBody().close();
        }
        logger.exiting("ClearHandler", "handle");
    }
}
