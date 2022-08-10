package handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import model.User;
import results.Result;
import services.AuthorizeService;
import services.PersonService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Logger;

public class PersonHandler implements Handler {
    public static final Logger logger = Logger.getLogger("PersonHandler");

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.entering("PersonHandler", "handle");

        int statusCode = HttpURLConnection.HTTP_BAD_REQUEST;
        Result result = new Result();

        if (exchange.getRequestMethod().equalsIgnoreCase("get")) {
            Headers reqHeaders = exchange.getRequestHeaders();

            User user = authorizeUser(reqHeaders);
            if (user != null) {
                result = new PersonService().getResult(user.getUsername(), exchange.getRequestURI().toString());
            } else {
                result.setMessage("Error: Invalid auth token");
            }
        } else {
            statusCode = HttpURLConnection.HTTP_BAD_METHOD;
            result.setMessage("Error: Invalid request method");
        }

        if (result.isSuccess()) {
            statusCode = HttpURLConnection.HTTP_OK;
        }

        exchange.sendResponseHeaders(statusCode, 0);
        writeString(serialize(result), exchange.getResponseBody());
        exchange.getResponseBody().close();
    }

    private User authorizeUser(Headers reqHeaders) {
        if (reqHeaders.containsKey("Authorization")) {
            AuthorizeService aService = new AuthorizeService();
            return aService.authorize(reqHeaders.getFirst("Authorization"));
        }
        return null;
    }
}
