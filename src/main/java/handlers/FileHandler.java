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
    private final String SOURCE_PATH = "web";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.entering("FileHandler", "handle");
        boolean success = false;

        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("get")) {
                File file = getFile(exchange.getRequestURI().toString());

                if (file.exists()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream responseBody = exchange.getResponseBody();
                    Files.copy(file.toPath(), responseBody);
                    responseBody.close();
                    success = true;
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                    Files.copy(new File(SOURCE_PATH + "/HTML/404.html").toPath(), exchange.getResponseBody());
//                    writeString("Page not found", exchange.getResponseBody());
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

    private File getFile(String urlPath) {
        if (urlPath == null || urlPath.equals("/")) {
            logger.fine("Original urlPath: " + urlPath);
            urlPath = "/index.html";
        }
        logger.finer("urlPath: " + urlPath);

        String filePath = SOURCE_PATH +  urlPath;

        logger.finest("filePath: " + filePath);

        return new File(filePath);
    }

}
