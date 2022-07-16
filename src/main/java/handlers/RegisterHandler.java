package handlers;

import com.sun.net.httpserver.HttpExchange;
import requests.RegisterRequest;
import results.RegisterResult;
import services.RegisterService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterHandler implements Handler {
    private static final Logger logger = Logger.getLogger("RegisterHandler");

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.entering("RegisterHandler", "handle");
        boolean success = false;
        try {
            String urlPath = exchange.getRequestURI().toString();
            logger.fine(urlPath);
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                RegisterRequest registerRequest = (RegisterRequest) deserialize(exchange.getRequestBody(), RegisterRequest.class);
                RegisterService service = new RegisterService();
                RegisterResult result = service.register(registerRequest);

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream responseBody = exchange.getResponseBody();
                writeString(serialize(result), responseBody);
                success = true;
            } else {
                logger.info("Method failed");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
                exchange.getResponseBody().close();
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            ex.printStackTrace();
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            exchange.getResponseBody().close();
        }
        if (!success) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            exchange.getResponseBody().close();
        }
        logger.exiting("RegisterHandler", "handle");
    }
}
