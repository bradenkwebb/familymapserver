package dao;

import model.Event;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class EventDAOTest {
    private Database db;
    private Event bestEvent;
    private Event biking;
    private Event bradenBiking;
    private Event stacyBirth;
    private Event jeremyBirth;

    private Event jeremyMarriage;
    private Event stacyMarriage;
    private Event hiking;
    private EventDAO eDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        System.out.println("Setup called.");

        // Here we can set up any classes or variables we will need for each test
        // lets create a new instance of the Database class
        db = new Database();

        // and a new event with random data
        bestEvent = new Event("Biking_123A", "Gale", "Gale123A",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        biking = new Event("Biking_123b", "Gale", "Gale123A",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        hiking = new Event("hiking123", "Gale", "Gale123A",
                35.9f, 140.1f, "Japan", "Ushiku",
                "hiking", 2016);
        bradenBiking = new Event("Biking_123c", "braden", "Gale123A",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);

        stacyBirth = new Event("stacybirth", "hannah", "stacy_seidner",
                12f, 12f, "usa", "ashland", "birth", 1976);
        jeremyBirth = new Event("jeremybirth", "hannah", "jeremy_webb",
                21f, 21f, "usa", "provo", "birth", 1974);
        stacyMarriage = new Event("stacymarriage", "hannah", "stacy_seidner",
                15f, 15f, "usa", "portland", "marriage", 1997);
        jeremyMarriage = new Event("jeremymarriage", "hannah", "jeremy_webb",
                15f, 15f, "usa", "portland", "marriage", 1997);

        // Here, we'll open the connection in preparation for the test case to use it
        Connection conn = db.getConnection();
        //Then we pass that connection to the EventDAO, so it can access the database.
        eDao = new EventDAO(conn);
        //Let's clear the database as well so any lingering data doesn't affect our tests
        eDao.clear();
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Cleanup called.");
        // Here we close the connection to the database file, so it can be opened again later.
        // We will set commit to false because we do not want to save the changes to the database
        // between test cases.
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        System.out.println("insertPass() called.");
        // Start by inserting an event into the database.
        eDao.insert(bestEvent);
        // Let's use a find method to get the event that we just put in back out.
        Event compareTest = eDao.find(bestEvent.getEventID());
        // First lets see if our find method found anything at all. If it did then we know that we got
        // something back from our database.
        assertNotNull(compareTest);
        // Now lets make sure that what we put in is the same as what we got out. If this
        // passes then we know that our insert did put something in, and that it didn't change the
        // data in any way.
        // This assertion works by calling the equals method in the Event class.
        assertEquals(bestEvent, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        System.out.println("insertFail() called.");
        // Let's do this test again, but this time lets try to make it fail.
        // If we call the method the first time the event will be inserted successfully.
        eDao.insert(bestEvent);

        // However, our sql table is set up so that the column "eventID" must be unique, so trying to insert
        // the same event again will cause the insert method to throw an exception, and we can verify this
        // behavior by using the assertThrows assertion as shown below.

        // Note: This call uses a lambda function. A lambda function runs the code that comes after
        // the "()->", and the assertThrows assertion expects the code that ran to throw an
        // instance of the class in the first parameter, which in this case is a DataAccessException.
        assertThrows(DataAccessException.class, () -> eDao.insert(bestEvent));
    }

    @Test
    public void findPass() throws DataAccessException {
        eDao.insert(bestEvent);

        assertNotNull(eDao.find(bestEvent.getEventID()));
        assertEquals(bestEvent, eDao.find(bestEvent.getEventID()));
    }

    @Test
    public void findFail() throws DataAccessException {
        assertNull(eDao.find(bestEvent.getEventID()));
        eDao.insert(bestEvent);
        eDao.insert(biking);

        assertNotNull(eDao.find(biking.getEventID()));
        assertEquals(biking, eDao.find(biking.getEventID()));
    }

    @Test
    public void getAllFromUserPass() throws DataAccessException {
        System.out.println("getAllFromUser() called.");

        eDao.insert(biking);
        assertEquals(0, eDao.getAllFromUser("braden").size());
        eDao.insert(bradenBiking);
        assertEquals(1, eDao.getAllFromUser("braden").size());
        eDao.insert(hiking);
        assertEquals(2, eDao.getAllFromUser("Gale").size());

        eDao.clear();
        eDao.insert(bradenBiking);
        List<Event> events = eDao.getAllFromUser("braden");
        assertEquals("Biking_123c", events.get(0).getEventID());
    }

    @Test
    public void getAllFromUserFail() throws DataAccessException {
        eDao.insert(bestEvent);
        eDao.insert(biking);
        eDao.insert(bradenBiking);
        eDao.insert(hiking);
        List<Event> galesEvents =  eDao.getAllFromUser("Gale");
        List<Event> bradensEvents = eDao.getAllFromUser("braden");

        // Check that the method actually returns a list
        assertNotNull(galesEvents);
        assertNotNull(bradensEvents);

        //Check that the returned list has the correct size
        assertEquals(3, galesEvents.size());
        assertEquals(1, bradensEvents.size());

        // Check that the returned list has Events for elements
        assertNotNull(galesEvents.get(0).getEventID());
        assertNotNull(bradensEvents.get(0).getEventID());

        assertThrows(IndexOutOfBoundsException.class, () -> eDao.getAllFromUser("anna").get(1));
    }

    @Test
    public void clearPass() throws DataAccessException {
        eDao.insert(bestEvent);
        eDao.clear();
        Event event = eDao.find(bestEvent.getEventID());
        assertNull(event);
        assertEquals(0, eDao.getAllFromUser("braden").size());
    }

    @Test
    public void clearUserPass() throws DataAccessException {
        eDao.insert(bestEvent);
        eDao.clearUser(bestEvent.getAssociatedUsername());
        assertNull(eDao.find(bestEvent.getEventID()));
        eDao.insert(bestEvent);
        eDao.insert(bradenBiking);
        eDao.clearUser("Gale");
        assertEquals(1, eDao.getAllFromUser("braden").size());
    }

    @Test
    public void clearUserFail() throws DataAccessException {
        assertDoesNotThrow(() -> eDao.clearUser("anna"));
        eDao.insert(bestEvent);
        assertDoesNotThrow(() -> eDao.clearUser("Gale"));
    }

    @Test
    public void generateBirthPass() throws DataAccessException {
        eDao.generateBirth("bkwebb23", "braden_webb", 1999);
        List<Event> events = eDao.getAllFromUser("bkwebb23");
        assertEquals(1, events.size());
        Event birth = events.get(0);
        assertNotNull(birth);
        assertNotNull(birth.getEventID());
        assertNotNull(birth.getEventType());
        assertEquals("birth", birth.getEventType());
        assertEquals(1999, birth.getYear());
    }

    @Test
    public void generateBirthFail() throws DataAccessException {
        eDao.generateBirth("bkwebb23", "aristotle", -500);
        assertEquals(-500, eDao.getAllFromUser("bkwebb23").get(0).getYear());
    }

    @Test
    public void generateMarriagePass() throws DataAccessException {
        eDao.generateBirth("bkwebb23", "braden", 1999);
        eDao.generateBirth("bkwebb23", "anna", 1999);
        assertDoesNotThrow(() -> eDao.generateMarriage("bkwebb23", "braden", "anna"));
        assertEquals(4, eDao.getAllFromUser("bkwebb23").size());
    }

    @Test
    public void generateMarriageFail() throws DataAccessException {
        assertThrows(DataAccessException.class,
                () -> eDao.generateMarriage("bkwebb23", "braden", "anna"));
        eDao.generateBirth("bkwebb23", "braden", 1800);
        eDao.generateBirth("bkwebb23", "anna", 1999);
        assertThrows(DataAccessException.class,
                () -> eDao.generateMarriage("bkwebb23", "braden", "anna"));
    }

    @Test
    public void generateDeathPass() throws DataAccessException {
        eDao.insert(stacyBirth);
        eDao.insert(jeremyBirth);
        assertDoesNotThrow(() -> eDao.generateDeath("hannah", "stacy_seidner"));
        assertDoesNotThrow(() -> eDao.generateDeath("hannah", "jeremy_webb"));
        eDao.clearUser("hannah");
        eDao.insert(stacyBirth);
        eDao.insert(jeremyBirth);
        eDao.insert(stacyMarriage);
        eDao.insert(jeremyMarriage);
        assertDoesNotThrow(() -> eDao.generateDeath("hannah", "stacy_seidner"));
        assertDoesNotThrow(() -> eDao.generateDeath("hannah", "jeremy_webb"));
    }

    @Test
    public void generateDeathFail() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> eDao.generateDeath("bkwebb23", "braden"));
        eDao.generateBirth("bkwebb23", "braden", 1999);
        assertFalse(0 > eDao.getAllFromUser("bkwebb23").get(0).getYear() - PersonDAO.MAX_AGE);
    }

    @Test
    public void updatePersonIDsPass() throws DataAccessException {
        eDao.insert(biking);
        eDao.insert(stacyBirth);
        eDao.insert(jeremyBirth);
        eDao.insert(stacyMarriage);
        eDao.insert(jeremyMarriage);
        assertEquals(4, eDao.getAllFromUser("hannah").size());
        Person jeremy = new Person("jeremy_webb", "hannah", "jeremy",
                                    "webb", "m");
        assertDoesNotThrow(() -> eDao.updatePersonIDs(jeremy, "jdw"));
        assertEquals(4, eDao.getAllFromUser("hannah").size());
        System.out.println(eDao.getAllFromUser("hannah").size());

        eDao.clearUser("hannah");

        eDao.insert(jeremyBirth);
        eDao.updatePersonIDs(jeremy, "jdw");
        assertEquals("jdw", eDao.getAllFromUser("hannah").get(0).getPersonID());
    }

    @Test
    public void updatePersonIDsFail() throws DataAccessException {
        Person jeremy = new Person("jeremy_webb", "hannah", "jeremy",
                "webb", "m");
        assertDoesNotThrow(() -> eDao.updatePersonIDs(jeremy, jeremy.getPersonID()));
        eDao.insert(jeremyBirth);
        assertDoesNotThrow(() -> eDao.updatePersonIDs(jeremy, jeremy.getPersonID()));
    }
}
