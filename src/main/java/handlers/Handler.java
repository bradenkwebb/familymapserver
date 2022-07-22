package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpHandler;
import requests.Request;
import results.Result;

import java.io.*;
import java.nio.charset.StandardCharsets;

public interface Handler extends HttpHandler {
    /**
     * Writes a String to an OutputStream.
     *
     * @param str the String to write
     * @param os the destination OutputStream
     * @throws IOException if an I/O error occurs
     */
    default void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

    /**
     * Turns a Result() object into a JSON-formatted string.
     *
     * @param result the object to serialize
     * @return the JSON string
     */
    default String serialize(Result result) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(result);
    }

    /**
     * Turns an InputStream of JSON-text into an object of the provided class.
     *
     * @param bodyStream an InputStream, which should be in JSON-format
     * @param classType the type of object to create (should inherit from Request)
     * @return the resulting Object type
     * @throws IOException if an I/O error occurs
     */
    default Request deserialize(InputStream bodyStream, Class<? extends Request> classType) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(bodyStream, StandardCharsets.UTF_8));
        Gson gson = new Gson();
        return gson.fromJson(br, classType);
    }
}
