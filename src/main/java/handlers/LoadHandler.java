package handlers;

import com.sun.net.httpserver.HttpExchange;
import requests.LoadRequest;
import results.Result;
import services.LoadService;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoadHandler implements Handler {
    public static final Logger logger = Logger.getLogger("LoadHandler");

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.entering("LoadHandler", "handl");
        int statusCode = HttpURLConnection.HTTP_INTERNAL_ERROR;
        Result result = new Result();
        boolean success = false;
        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                InputStream bodyJson = exchange.getRequestBody();
                LoadService loadService = new LoadService();
                result = loadService.load((LoadRequest) deserialize(bodyJson, LoadRequest.class));
                success = result.isSuccess();
                if (success) {
                    statusCode = HttpURLConnection.HTTP_OK;
                }
            } else {
                statusCode = HttpURLConnection.HTTP_BAD_METHOD;
                result.setMessage("Error: Invalid method.");
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            result.setSuccess(false);
            result.setMessage("Error: " + ex.getMessage());
        }
        exchange.sendResponseHeaders(statusCode, 0);
        result.setSuccess(success);
        writeString(serialize(result), exchange.getResponseBody());
        exchange.getResponseBody().close();
    }
}
