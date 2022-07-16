package handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.util.logging.Logger;

public class FileHandler implements Handler {

    private static final Logger logger = Logger.getLogger("FileHandler");

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.entering("FileHandler", "handle");
        boolean success = false;

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {
                String urlPath = exchange.getRequestURI().toString();
                if (urlPath == null || urlPath.equals("/")) {
                    logger.fine("Original urlPath: " + urlPath);
                    urlPath = "/index.html";
                }
                logger.finer("urlPath: " + urlPath);

                String sourcePath = "web";
                String filePath = sourcePath +  urlPath;

                logger.finest("filePath: " + filePath);

                File file = new File(filePath);
                if (file.exists()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream responseBody = exchange.getResponseBody();
                    Files.copy(file.toPath(), responseBody);
                    responseBody.close();
                    success = true;
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                    exchange.getResponseBody().close();
                }
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange.getResponseBody().close();
            }
        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }

}
