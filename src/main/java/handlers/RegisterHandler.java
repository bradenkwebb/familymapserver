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

        RegisterResult result = new RegisterResult();
        result.setSuccess(false);
        int statusCode = HttpURLConnection.HTTP_BAD_REQUEST;

        try {
            String urlPath = exchange.getRequestURI().toString();
            logger.finest(urlPath);
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                RegisterRequest registerRequest = (RegisterRequest) deserialize(exchange.getRequestBody(), RegisterRequest.class);
                RegisterService service = new RegisterService();
                result = service.register(registerRequest);

                if (result.isSuccess()) {
                    statusCode = HttpURLConnection.HTTP_OK;
                }
            } else {
                logger.info("Invalid request method");
                statusCode = HttpURLConnection.HTTP_BAD_METHOD;
                result.setSuccess(false);
                result.setMessage("Error: Invalid request method");
            }

        } catch (IOException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            ex.printStackTrace();
            result.setSuccess(false);
            result.setMessage("Error: Unable to parse JSON; likely either a request property is " +
                             "missing or has invalid value");
        }


        exchange.sendResponseHeaders(statusCode, 0);
        OutputStream responseBody = exchange.getResponseBody();
        writeString(serialize(result), responseBody);
        exchange.getResponseBody().close();
    }
}
