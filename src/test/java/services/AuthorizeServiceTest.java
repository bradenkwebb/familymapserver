package services;

import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.RegisterRequest;
import results.RegisterResult;

import static org.junit.jupiter.api.Assertions.*;

public class AuthorizeServiceTest {

    private AuthorizeService authorizeService;
    private RegisterService registerService;
    private ClearService clearService;
    private RegisterRequest registerRequest;
    private RegisterResult registerResult;
    private String validAuthtoken;
    private String invalidAuthtoken;

    @BeforeEach
    public void setUp() {
        System.out.println("setUp() called.");
        clearService = new ClearService();
        clearService.clear();

        authorizeService = new AuthorizeService();

        registerService = new RegisterService();
        registerRequest = new RegisterRequest();
        registerRequest.setFirstName("Braden");
        registerRequest.setLastName("Webb");
        registerRequest.setUsername("bkwebb23");
        registerRequest.setPassword("password");
        registerRequest.setGender("m");
        registerRequest.setEmail("bwebb@gmail.com");
        registerResult = registerService.register(registerRequest);


    }

    @AfterEach
    public void tearDown() {
        System.out.println("tearDown() called.");

    }

    @Test
    public void authorizePass() {
        User user = authorizeService.authorize(registerResult.getAuthtoken());
        assertNotNull(user);
        assertNotNull(user.getUsername());
        assertEquals(registerRequest.getUsername(), user.getUsername());
    }

    @Test
    public void authorizeFail() {
        User user = authorizeService.authorize("invalidAuthToken");
        assertNull(user);
    }

}
