package services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.RegisterRequest;
import results.AllEventsResult;
import results.RegisterResult;
import results.Result;

import static org.junit.jupiter.api.Assertions.*;

public class EventServiceTest {

    private EventService eventService;
    private RegisterResult registerResult;

    @BeforeEach
    public void setUp() {
        System.out.println("setUp() called.");

        eventService = new EventService();
        RegisterService registerService = new RegisterService();

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
    public void getResultPass() {
        assertDoesNotThrow(() -> eventService.getResult(registerResult.getUsername(), "/event"));
        Result eventResult = eventService.getResult(registerResult.getUsername(), "/event");
        assertNotNull(eventResult);
        assertTrue(eventResult.isSuccess());
        assertEquals(AllEventsResult.class, eventResult.getClass());
        assertEquals(91,((AllEventsResult) eventResult).getData().size());
    }

    @Test
    public void getResultFail() {
        Result eventResult = eventService.getResult("aceverett", "/event");
        assertNotNull(eventResult);
        assertEquals(AllEventsResult.class, eventResult.getClass());
        assertEquals(0,((AllEventsResult) eventResult).getData().size());
    }

}
