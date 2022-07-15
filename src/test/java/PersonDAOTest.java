import dao.DataAccessException;
import dao.Database;
import dao.PersonDAO;

import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class PersonDAOTest {
    private Database db;
    private Person bestPerson;
    private PersonDAO pDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        System.out.println("setUp() called.");
        db = new Database();

        bestPerson = new Person("123", "bkwebb23", "Braden",
                        "Webb", "m");

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

        pDao.insert(bestPerson);
        Person comparePerson = pDao.find(bestPerson.getPersonID());

        assertNotNull(comparePerson);
        assertEquals(bestPerson, comparePerson);

        // TODO implement me!
    }

    @Test
    public void insertFail() throws DataAccessException {
        System.out.println("insertFail() called.");

        pDao.insert(bestPerson);
        assertThrows(DataAccessException.class, () -> pDao.insert(bestPerson));

        // TODO implement me!
    }

    @Test
    public void findPass() throws DataAccessException {
        System.out.println("findPass() called.");

        pDao.insert(bestPerson);

        Person comparePerson = pDao.find(bestPerson.getPersonID());

        assertNotNull(comparePerson);
        assertEquals(bestPerson, comparePerson);


        // TODO implement me!
    }

    @Test
    public void findFail() throws DataAccessException {
        System.out.println("findFail() called.");
        System.out.println("THIS ISN'T IMPLEMENTED YET!!!");
        // TODO implement me!
    }

    //TODO implement more of these!!

}
