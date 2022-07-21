package handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import model.User;
import results.Result;
import services.AuthorizeService;
import services.PersonService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PersonHandler implements Handler {
    public static final Logger logger = Logger.getLogger("PersonHandler");

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.entering("PersonHandler", "handle");
        boolean success = false;
        int statusCode = HttpURLConnection.HTTP_INTERNAL_ERROR;
        Result result = new Result();
        if (exchange.getRequestMethod().equalsIgnoreCase("get")) {
            Headers reqHeaders = exchange.getRequestHeaders();

            if (reqHeaders.containsKey("Authorization")) {
                AuthorizeService authService = new AuthorizeService();
                User user = authService.authorize(reqHeaders.getFirst("Authorization"));

                if (user != null) {

                    PersonService personService = new PersonService();
                    result = personService.getResult(user.getUsername(), exchange.getRequestURI().toString());

                    success = result.isSuccess();
                    statusCode = (success) ? HttpURLConnection.HTTP_OK : HttpURLConnection.HTTP_BAD_REQUEST;

                } else {
                    statusCode = HttpURLConnection.HTTP_BAD_REQUEST;
                    result.setMessage("Error: Invalid auth token");
                }
            } else {
                statusCode = HttpURLConnection.HTTP_BAD_REQUEST;
                result.setMessage("Error: Invalid auth token");
            }
        } else {
            statusCode = HttpURLConnection.HTTP_BAD_METHOD;
            result.setMessage("Error: Invalid request method");
        }

        exchange.sendResponseHeaders(statusCode, 0);
        writeString(serialize(result), exchange.getResponseBody());
        exchange.getResponseBody().close();
    }
}