package handlers;

import com.google.gson.stream.MalformedJsonException;
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

        RegisterResult result = new RegisterResult();
        result.setSuccess(false);
        int statusCode = HttpURLConnection.HTTP_BAD_REQUEST;

        if (exchange.getRequestMethod().equalsIgnoreCase("post")) {

            RegisterRequest registerRequest = (RegisterRequest) deserialize(exchange.getRequestBody(),
                                                                            RegisterRequest.class);
            result = new RegisterService().register(registerRequest);
        } else {
            logger.info("Invalid request method");
            statusCode = HttpURLConnection.HTTP_BAD_METHOD;
            result.setSuccess(false);
            result.setMessage("Error: Invalid request method");
        }

        if (result.isSuccess()) {
            statusCode = HttpURLConnection.HTTP_OK;
        }

        exchange.sendResponseHeaders(statusCode, 0);
        writeString(serialize(result), exchange.getResponseBody());
        exchange.getResponseBody().close();
    }
}
