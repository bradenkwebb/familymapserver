package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpHandler;
import requests.Request;
import results.Result;

import java.io.*;
import java.nio.charset.StandardCharsets;

public interface Handler extends HttpHandler {
    default void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
    default String serialize(Result result) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(result);
    }

    default Request deserialize(InputStream bodyStream, Class<? extends Request> classType) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(bodyStream, StandardCharsets.UTF_8));
        Gson gson = new Gson();
        return gson.fromJson(br, classType);
    }
}
