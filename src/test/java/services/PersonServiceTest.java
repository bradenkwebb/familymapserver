package services;

import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.RegisterRequest;
import results.*;

import static org.junit.jupiter.api.Assertions.*;

public class PersonServiceTest {

    private PersonService personService;
    private RegisterService registerService;
    private RegisterRequest registerRequest;
    private RegisterResult registerResult;

    @BeforeEach
    public void setUp() {
        personService = new PersonService();
        registerService = new RegisterService();
        registerRequest = new RegisterRequest();

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
    public void getResultPass() {
        System.out.println("getResultPass() called.");
        Result result = personService.getResult(registerResult.getUsername(), "/person");
        assertNotNull(result);
        assertEquals(PeopleResult.class, result.getClass());
        PeopleResult people = (PeopleResult) personService.getResult(registerResult.getUsername(),
                                                                "/person");
        assertEquals(31, people.getData().size());
        Person person = people.getData().get(15);
        PersonResult personResult = (PersonResult) personService.getResult(registerResult.getUsername(),
                                                                    "/person/" + person.getPersonID());
        assertNotNull(personResult);
        assertEquals(person.getPersonID(), personResult.getPersonID());
    }

    @Test
    public void getResultFail() {
        System.out.println("getResultFail() called.");
        Result result = personService.getResult("anna", "/person");
        assertNotNull(result);
        assertEquals(PeopleResult.class, result.getClass());
        PeopleResult people = (PeopleResult) personService.getResult("anna", "/person");
        assertEquals(0, people.getData().size());
        Result x = personService.getResult("bkwebb23", "/person/12345");
        assertNotNull(x);
        assertFalse(x.isSuccess());
    }
}
