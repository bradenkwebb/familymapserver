package services;

import model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.RegisterRequest;
import results.AllEventsResult;
import results.RegisterResult;
import results.Result;

import static org.junit.jupiter.api.Assertions.*;

public class FillServiceTest {

    private FillService fillService;
    private EventService eventService;
    private RegisterService registerService;
    private RegisterRequest registerRequest;
    private RegisterResult registerResult;
    private Result fillResult;


    @BeforeEach
    public void setUp() {
        System.out.println("setUp() called.");

        eventService = new EventService();
        registerService = new RegisterService();
        fillService = new FillService();

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("bkwebb23");
        registerRequest.setPassword("password");
        registerRequest.setEmail("bw@gmail.com");
        registerRequest.setFirstName("Braden");
        registerRequest.setLastName("Webb");
        registerRequest.setGender("m");
        registerResult = registerService.register(registerRequest);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("tearDown() called.");
        new ClearService().clear();
    }

    @Test
    public void fillPass() {
        fillResult = fillService.fill(registerResult.getUsername(), 0);
        assertNotNull(fillResult);
        assertTrue(fillResult.isSuccess());
        AllEventsResult events = (AllEventsResult) eventService.getResult(registerResult.getUsername(), "/events");
        assertEquals(1,events.getData().size());

        fillResult = fillService.fill(registerResult.getUsername(), 1);
        assertNotNull(fillResult);
        assertTrue(fillResult.isSuccess());
        events = (AllEventsResult) eventService.getResult(registerResult.getUsername(), "/events");
        assertEquals(7,events.getData().size());

        fillResult = fillService.fill(registerResult.getUsername(), 2);
        assertNotNull(fillResult);
        assertTrue(fillResult.isSuccess());
        events = (AllEventsResult) eventService.getResult(registerResult.getUsername(), "/events");
        assertEquals(19,events.getData().size());
    }

    @Test
    public void fillFail() {
        fillResult = fillService.fill("aceverett", 0);
        assertFalse(fillResult.isSuccess());

        fillResult = fillService.fill("bkwebb23", -5);
        assertFalse(fillResult.isSuccess());
    }

}
