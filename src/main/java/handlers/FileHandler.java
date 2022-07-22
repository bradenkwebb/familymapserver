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
        int statusCode = HttpURLConnection.HTTP_NOT_FOUND;
        boolean appropriateMethod = exchange.getRequestMethod().equalsIgnoreCase("get");

        File file = getFile(exchange.getRequestURI().toString());
        if (!appropriateMethod) {
            statusCode = HttpURLConnection.HTTP_BAD_METHOD;
            exchange.sendResponseHeaders(statusCode, 0);
        } else if (!file.exists()) {
            exchange.sendResponseHeaders(statusCode, 0);
            file = new File(SOURCE_PATH + "/HTML/404.html");
        } else {
            statusCode = HttpURLConnection.HTTP_OK;
            exchange.sendResponseHeaders(statusCode, 0);
        }
        if (appropriateMethod){
            Files.copy(file.toPath(), exchange.getResponseBody());
        }

        exchange.getResponseBody().close();
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
