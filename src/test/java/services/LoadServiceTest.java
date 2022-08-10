package services;

import handlers.Handler;
import handlers.LoadHandler;
import model.Event;
import model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.LoadRequest;
import results.Result;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class LoadServiceTest {

    private LoadService loadService;
    private LoadRequest loadRequest;
    private Handler handler;

    @BeforeEach
    public void setUp() {
        System.out.println("setUp() called.");
        loadService = new LoadService();
        handler = new LoadHandler();
        File jsonFile = new File("passoffFiles/LoadData.json");
        try {
            InputStream jsonStream = new FileInputStream(jsonFile);
            loadRequest = (LoadRequest) handler.deserialize(jsonStream, LoadRequest.class);
        } catch (IOException ex) {
            fail("If this is thrown, there was an error reading in/deserializing the json file" +
                    "on which this test depends");
        }
    }

    @Test
    public void loadPass() {
        System.out.println("loadPass() called.");
        Result result = loadService.load(loadRequest);
        assertNotNull(result);
        assertTrue(result.isSuccess());
    }

    @Test
    public void loadFail() {
        System.out.println("loadFail() called.");
        loadRequest.setPersons(new ArrayList<>());
        Result result = loadService.load(loadRequest);
        assertNotNull(result);
        assertTrue(result.isSuccess());
    }

}
