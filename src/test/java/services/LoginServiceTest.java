package services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.LoginRequest;
import requests.RegisterRequest;
import results.LoginResult;
import results.RegisterResult;

import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest {

    private LoginService loginService;
    private RegisterService registerService;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private RegisterResult registerResult;

    @BeforeEach
    public void setUp() {
        loginService = new LoginService();
        registerService = new RegisterService();
        registerRequest = new RegisterRequest();
        loginRequest = new LoginRequest();

        registerRequest.setUsername("bkwebb23");
        registerRequest.setEmail("bw@gmail.com");
        registerRequest.setPassword("password");
        registerRequest.setFirstName("Braden");
        registerRequest.setLastName("Webb");
        registerRequest.setGender("m");

        registerResult = registerService.register(registerRequest);
    }

    @AfterEach
    public void tearDown() {
        new ClearService().clear();
    }

    @Test
    public void loginPass() {
        loginRequest.setUsername(registerResult.getUsername());
        loginRequest.setPassword(registerRequest.getPassword());
        LoginResult loginResult = loginService.login(loginRequest);
        assertNotNull(loginResult);
        assertTrue(loginResult.isSuccess());
        assertEquals(registerResult.getPersonID(), loginResult.getPersonID());
    }

    @Test
    public void loginFail() {
        loginRequest.setUsername("b");
        loginRequest.setPassword("w");
        LoginResult loginResult = loginService.login(loginRequest);
        assertNotNull(loginResult);
        assertFalse(loginResult.isSuccess());
    }

}
