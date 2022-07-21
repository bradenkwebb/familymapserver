package handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import model.User;
import requests.AllEventsRequest;
import results.Result;
import services.AuthorizeService;
import services.EventService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Level;
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
    public void handle(HttpExchange exchange) {
        logger.entering("EventHandler", "handle");

        // Assume failure unless shown otherwise
        boolean success = false;
        int statusCode = HttpURLConnection.HTTP_BAD_REQUEST;
        Result result = new Result();

        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("get")) {
                Headers reqHeaders = exchange.getRequestHeaders();
                if (reqHeaders.containsKey("Authorization")) {
                    AuthorizeService aService = new AuthorizeService();
                    User user = aService.authorize(reqHeaders.getFirst("Authorization"));
                    if (user != null) {
                        EventService service = new EventService();
                        result = service.getResult(user.getUsername(), exchange.getRequestURI().toString());

                        success = result.isSuccess();
                        statusCode = (success) ? HttpURLConnection.HTTP_OK : HttpURLConnection.HTTP_INTERNAL_ERROR;
                    } else {
                        result.setMessage("Error: Invalid auth token");
                    }
                } else {
                    result.setMessage("Error: Invalid auth token");
                }
            } else {
                statusCode = HttpURLConnection.HTTP_BAD_METHOD;
                result.setMessage("Error: Invalid method");
            }

            result.setSuccess(success);

            exchange.sendResponseHeaders(statusCode, 0);
            writeString(serialize(result), exchange.getResponseBody());
            exchange.getResponseBody().close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
}
