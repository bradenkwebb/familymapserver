package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import requests.LoginRequest;
import requests.Request;
import results.LoginResult;
import results.Result;
import services.LoginService;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public class LoginHandler implements Handler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                InputStream bodyJson = exchange.getRequestBody();

                LoginRequest request = new LoginRequest();
                request = (LoginRequest) deserialize(bodyJson, request.getClass());

                LoginService service = new LoginService();
                LoginResult result = service.login(request);

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                OutputStream responseBody = exchange.getResponseBody();
                writeString(serialize(result), responseBody);
                responseBody.close();
                success = true;
            }
            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange.getResponseBody().close();
            }

        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }

//    public LoginRequest deserialize(InputStream bodyStream) throws IOException {
//        BufferedReader br = new BufferedReader(new InputStreamReader(bodyStream, StandardCharsets.UTF_8));
//        Gson gson = new Gson();
//        return gson.fromJson(br, LoginRequest.class);
//    }

//    public String serialize(Result result) {
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        return gson.toJson(result);
//    }

    /*
    The writeString method shows how to write a String to an OutputStream.
    */
//    private void writeString(String str, OutputStream os) throws IOException {
//        OutputStreamWriter sw = new OutputStreamWriter(os);
//        sw.write(str);
//        sw.flush();
//    }
}
