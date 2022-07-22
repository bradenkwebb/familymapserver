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

        // We assume that the page they're looking for doesn't exit
        int statusCode = HttpURLConnection.HTTP_NOT_FOUND;

        boolean appropriateMethod = exchange.getRequestMethod().equalsIgnoreCase("get");

        File file = getFile(exchange.getRequestURI().toString());
        if (!appropriateMethod) {
            statusCode = HttpURLConnection.HTTP_BAD_METHOD;
        } else if (!file.exists()) {
            file = new File(SOURCE_PATH + "/HTML/404.html");
        } else {
            statusCode = HttpURLConnection.HTTP_OK;
        }

        exchange.sendResponseHeaders(statusCode, 0);
        if (appropriateMethod){
            Files.copy(file.toPath(), exchange.getResponseBody());
        }
        exchange.getResponseBody().close();
    }

    private File getFile(String urlPath) {
        logger.entering("FileHandler", "getFile");
        if (urlPath == null || urlPath.equals("/")) {
            logger.fine("Original urlPath: " + urlPath);
            urlPath = "/index.html";
        }
        String filePath = SOURCE_PATH +  urlPath;

        logger.finer("urlPath: " + urlPath);
        logger.finest("filePath: " + filePath);
        return new File(filePath);
    }

}
