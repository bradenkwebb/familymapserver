package services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.RegisterRequest;
import results.RegisterResult;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterServiceTest {

    private RegisterService registerService;
    private RegisterRequest registerRequest;
    @BeforeEach
    public void setUp() {
        System.out.println("setUp() called.");
        registerService = new RegisterService();
        registerRequest = new RegisterRequest();

        registerRequest.setUsername("bkwebb23");
        registerRequest.setEmail("bw@gmail.com");
        registerRequest.setPassword("password");
        registerRequest.setFirstName("Braden");
        registerRequest.setLastName("Webb");
        registerRequest.setGender("m");
    }

    @AfterEach
    public void tearDown() {
        System.out.println("tearDown() called.");
        new ClearService().clear();
    }

    @Test
    public void registerPass() {
        System.out.println("registerPass() called.");
        RegisterResult result = registerService.register(registerRequest);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(registerRequest.getUsername(), result.getUsername());
    }

    @Test
    public void registerFail() {
        System.out.println("registerFail() called.");
        RegisterResult result = registerService.register(registerRequest);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(registerRequest.getUsername(), result.getUsername());
        assertFalse(registerService.register(registerRequest).isSuccess());
    }

}
