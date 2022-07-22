package handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import model.User;
import results.Result;
import services.AuthorizeService;
import services.EventService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Logger;

/**
 * The handler for all HTTP requests with a URL path of the form /event or /event/[eventID]. This class has one method,
 * which should parse the HTTP request and return an appropriate response.
 */
public class EventHandler implements Handler {
    public static final Logger logger = Logger.getLogger("EventHandler");

    /**
     * For this class, the HTTP Request should always have an empty body - but the method URL can still provide a
     * parameter, and an authtoken is always required in the header.
     *
     * @param exchange the exchange containing the request from the client and used to send the response
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.entering("EventHandler", "handle");

        int statusCode = HttpURLConnection.HTTP_BAD_REQUEST;
        Result result = new Result();

        if (!exchange.getRequestMethod().equalsIgnoreCase("get")) {
            statusCode = HttpURLConnection.HTTP_BAD_METHOD;
            result.setMessage("Error: Invalid method");
        } else {
            Headers reqHeaders = exchange.getRequestHeaders();
            User user = authorizeUser(reqHeaders);

            if (user != null) {
                EventService service = new EventService();
                result = service.getResult(user.getUsername(), exchange.getRequestURI().toString());

                statusCode = (result.isSuccess()) ? HttpURLConnection.HTTP_OK : HttpURLConnection.HTTP_BAD_REQUEST;
            } else {
                result.setMessage("Error: Invalid auth token");
            }
        }

        exchange.sendResponseHeaders(statusCode, 0);
        writeString(serialize(result), exchange.getResponseBody());
        exchange.getResponseBody().close();
    }

    /**
     * Determines whether or not a user is authorized and if so, generates their User object.
     *
     * @param reqHeaders The HTTP request headers
     * @return null if the provided HTTP request holders do not contain a valid auth token, otherwise, generates
     *      a User object corresponding to the valid authtoken.
     */
    private User authorizeUser(Headers reqHeaders) {
        if (reqHeaders.containsKey("Authorization")) {
            AuthorizeService aService = new AuthorizeService();
            return aService.authorize(reqHeaders.getFirst("Authorization"));
        }
        return null;
    }
}
