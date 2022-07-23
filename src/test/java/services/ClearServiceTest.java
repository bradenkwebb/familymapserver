package services;

import dao.DataAccessException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.RegisterRequest;
import results.RegisterResult;

import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {
    private ClearService clearService;
    private FillService fillService;
    private AuthorizeService authorizeService;
    private RegisterService registerService;
    private RegisterRequest registerRequest;
    private RegisterResult registerResult;
    private PersonService personService;


    @BeforeEach
    public void setUp() throws DataAccessException {
        System.out.println("setUp() called.");

        clearService = new ClearService();
        fillService = new FillService();
        authorizeService = new AuthorizeService();
        registerService = new RegisterService();
        personService = new PersonService();

        registerRequest = new RegisterRequest();
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
    }

    @Test
    public void clearPass() {
        System.out.println("clearPass() called.");
        fillService.fill(registerRequest.getUsername(), 6);
        assertTrue(personService.getResult("bkwebb23", "/bkwebb23").isSuccess());
        assertDoesNotThrow(() -> clearService.clear());
        assertTrue(clearService.clear().isSuccess());
    }

    @Test
    public void clearFail() {
        System.out.println("clearFail() called");
        assertDoesNotThrow(() -> clearService.clear());
        clearService.clear();
        assertNull(authorizeService.authorize(registerResult.getAuthtoken()));
    }

}
