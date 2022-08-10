package handlers;

import com.sun.net.httpserver.HttpExchange;
import requests.LoginRequest;
import results.LoginResult;
import results.Result;
import services.LoginService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginHandler implements Handler {

    public static final Logger logger = Logger.getLogger("LoginHandler");

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.entering("LoginHandler", "handle");

        Result result = new Result();
        int statusCode = HttpURLConnection.HTTP_BAD_REQUEST;

        if (exchange.getRequestMethod().equalsIgnoreCase("post")) {

            LoginRequest request = (LoginRequest) deserialize(exchange.getRequestBody(), LoginRequest.class);
            result = new LoginService().login(request);

            if (result.isSuccess()) {
                statusCode = HttpURLConnection.HTTP_OK;
            }
        } else {
            logger.log(Level.SEVERE, "/user/login called with non-POST method.");
            statusCode = HttpURLConnection.HTTP_BAD_METHOD;
            result.setMessage("Error: Inappropriate request method");
        }
        logger.exiting("LoginHandler", "handle");

        exchange.sendResponseHeaders(statusCode, 0);
        writeString(serialize(result), exchange.getResponseBody());
        exchange.getResponseBody().close();
    }
}
