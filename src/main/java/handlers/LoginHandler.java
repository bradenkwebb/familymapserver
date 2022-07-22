package handlers;

import com.sun.net.httpserver.HttpExchange;
import requests.LoginRequest;
import results.LoginResult;
import services.LoginService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginHandler implements Handler {

    public static final Logger logger = Logger.getLogger("LoginHandler");

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.entering("LoginHandler", "handle");
        boolean success;

        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {

                InputStream bodyJson = exchange.getRequestBody();

                LoginRequest request = new LoginRequest();
                request = (LoginRequest) deserialize(bodyJson, request.getClass());

                LoginService service = new LoginService();
                LoginResult result = service.login(request);

                success = result.isSuccess();
                if (success) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }

                OutputStream responseBody = exchange.getResponseBody();
                writeString(serialize(result), responseBody);
                responseBody.close();
            } else {
                logger.log(Level.SEVERE, "/user/login called with non-POST method.");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
                exchange.getResponseBody().close();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            exchange.getResponseBody().close();
        }
        logger.exiting("LoginHandler", "handle");
    }

}
