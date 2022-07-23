package dao;

import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class PersonDAOTest {
    public static final Logger logger = Logger.getLogger("PersonDAOTest");
    private Database db;
    private Person me;
    private Person anna;
    private Person nathan;
    private Person hannah;
    private PersonDAO pDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        System.out.println("setUp() called.");
        db = new Database();

        me = new Person("braden_id", "bkwebb23", "Braden", "Webb",
                    "m", "jeremy_id", "stacy_ID", "anna_id");

        anna = new Person("anna_id", "aceverett", "Anna",
                    "Everett", "f", "craig_id", "sandee_id", "braden_id");

        nathan = new Person("nathan_id", "nwebb77", "Nathan",
                            "Webb", "m");
        hannah = new Person("hannah_id", "nwebb77", "Hannah",
                            "Webb", "f");

        Connection conn = db.getConnection();
        pDao = new PersonDAO(conn);

        pDao.clear();
    }

    @AfterEach
    public void tearDown() {
        System.out.println("tearDown() called.");
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        System.out.println("insertPass() called.");

        pDao.insert(me);
        Person comparePerson = pDao.find(me.getPersonID());

        assertNotNull(comparePerson);
        assertEquals(me, comparePerson);
    }

    @Test
    public void insertFail() throws DataAccessException {
        System.out.println("insertFail() called.");

        pDao.insert(me);
        assertThrows(DataAccessException.class, () -> pDao.insert(me));
    }

    @Test
    public void findPass() throws DataAccessException {
        System.out.println("findPass() called.");

        pDao.insert(me);

        Person comparePerson = pDao.find(me.getPersonID());

        assertNotNull(comparePerson);
        assertEquals(me, comparePerson);
    }

    @Test
    public void findFail() throws DataAccessException {
        System.out.println("findFail() called.");

        pDao.clear();
        assertNull(pDao.find("xzvc"));
    }

    @Test
    public void clearPass() throws DataAccessException {
        System.out.println("clearPass() called.");

        pDao.insert(me);
        pDao.clear();
        assertDoesNotThrow(() -> pDao.insert(me));
    }

    @Test
    public void getAllFamilyPass() throws DataAccessException {
        pDao.insert(me);
        pDao.insert(anna);
        pDao.insert(nathan);
        assertNotNull(pDao.getAllFamily("bkwebb23"));
        assertEquals(1, pDao.getAllFamily("bkwebb23").size());
        assertNotNull(pDao.getAllFamily("aceverett"));
    }

    @Test
    public void getAllFamilyFail() throws DataAccessException {
        assertDoesNotThrow(() -> pDao.getAllFamily("bkwebb23"));
        List<Person> people = pDao.getAllFamily("bkwebb23");
        assertEquals(0, people.size());
    }

    @Test
    public void clearUserPass() throws DataAccessException {
        pDao.insert(me);
        pDao.insert(anna);
        pDao.clearUser(me.getAssociatedUsername());
        assertEquals(1, pDao.totalNumPeople());
    }

    @Test
    public void clearUserFail() throws DataAccessException {
        pDao.insert(me);
        pDao.insert(anna);
        pDao.clearUser(me.getAssociatedUsername());
        System.out.println(pDao.find(me.getPersonID()));
        assertNull(pDao.find(me.getPersonID()));
    }

    @Test
    public void generatePass() throws DataAccessException {
        System.out.println("generatePass() called.");

        assertDoesNotThrow(() -> pDao.generate(me.getAssociatedUsername(), me.getGender(), 0, 1999));
        pDao.clear();
        assertDoesNotThrow(() -> pDao.generate(me.getAssociatedUsername(), me.getGender(), 1, 1999));
        pDao.clear();
        pDao.generate(me.getAssociatedUsername(), me.getGender(), 0, 1999);
        assertEquals(1, pDao.totalNumPeople());

        pDao.clear();
        me = pDao.generate(me.getAssociatedUsername(), me.getGender(), 2, 1999);
        assertNotNull(me.getFatherID());
        String fatherID = me.getFatherID();
        assertNotNull(me.getMotherID());
        String motherID = me.getMotherID();
        assertNotNull(pDao.find(fatherID));
        assertNotNull(pDao.find(motherID));
        assertEquals(7, pDao.totalNumPeople());

        pDao.clear();
        pDao.generate(me.getAssociatedUsername(), me.getGender(), 5, 1999);
        pDao.generate(nathan.getAssociatedUsername(), nathan.getGender(), 5, 2002);
        assertEquals(126, pDao.totalNumPeople());

        // overloaded generate() function
        User user = new User("bwebb1", "pass", "bkwebb23@gmail.com", "Braden",
                            "Webb", "m", UUID.randomUUID().toString());
        assertDoesNotThrow(() -> pDao.generate(user, 4));
        pDao.clear();
        pDao.generate(user, 4);
        assertEquals(31, pDao.totalNumPeople());
    }

    @Test
    public void generateFail() throws DataAccessException {
        pDao.insert(anna);
        assertThrows(DataAccessException.class, () -> pDao.generate("bkwebb23", "m", -3, 1999));
    }

    @Test
    public void totalNumPeoplePass() throws DataAccessException {
        System.out.println("totalNumPeoplePass() called.");

        pDao.clear();
        assertEquals(0, pDao.totalNumPeople());
        pDao.insert(me);
        assertEquals(1, pDao.totalNumPeople());
        me.setPersonID(UUID.randomUUID().toString());
        pDao.insert(me);
        assertEquals(2, pDao.totalNumPeople());
        pDao.clear();
        pDao.generate(me.getAssociatedUsername(), me.getGender(), 5, 1999);
        assertEquals(63, pDao.totalNumPeople());
    }

    @Test
    public void totalNumPeopleFail() throws DataAccessException {
        assertDoesNotThrow(() -> pDao.totalNumPeople());
        pDao.insert(anna);
        assertEquals(1, pDao.totalNumPeople());
        pDao.clearUser("aceverett");
        assertEquals(0, pDao.totalNumPeople());
    }

    @Test
    public void famSizePass() throws DataAccessException {
        assertDoesNotThrow(() -> pDao.famSize("bkwebb23"));
        assertEquals(0, pDao.famSize("bkwebb23"));
        pDao.insert(me);
        pDao.insert(nathan);
        pDao.insert(hannah);
        assertEquals(2, pDao.famSize("nwebb77"));
    }

    @Test
    public void famSizeFail() throws DataAccessException {
        pDao.insert(me);
        pDao.insert(anna);
        pDao.insert(hannah);
        pDao.insert(nathan);
        assertEquals(0, pDao.famSize("stacy"));
        assertEquals(1, pDao.famSize("bkwebb23"));
        pDao.clearUser("bkwebb23");
        assertEquals(0, pDao.famSize("bkwebb23"));
    }
}
