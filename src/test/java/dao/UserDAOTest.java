package dao;

import model.AuthToken;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {
    private Database db;
    private User braden;
    private User anna;
    private UserDAO uDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        System.out.println("setUp() called.");
        db = new Database();

        braden = new User("bkwebb23", "mypass", "bw@gmail.com",
                            "Braden", "Webb", "m", "123");
        anna = new User("aceverett", "password", "ae@gmail.com",
                        "Anna", "Everett", "f", "321");

        Connection conn = db.getConnection();
        uDao = new UserDAO(conn);

        uDao.clear();
    }

    @AfterEach
    public void tearDown() {
        System.out.println("tearDown() called.");
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        System.out.println("insertPass() called.");

        uDao.insert(braden);
        User compareUser = uDao.find(braden.getUsername());

        assertNotNull(compareUser);
        assertEquals(braden, compareUser);
    }

    @Test
    public void insertFail() throws DataAccessException {
        System.out.println("insertFail() called.");

        uDao.insert(braden);
        assertThrows(DataAccessException.class, () -> uDao.insert(braden));
    }

    @Test
    public void findPass() throws DataAccessException {
        System.out.println("findPass() called.");

        uDao.insert(braden);

        User compareUser = uDao.find(braden.getUsername());

        assertNotNull(compareUser);
        assertEquals(braden, compareUser);
    }

    @Test
    public void findFail() throws DataAccessException {
        System.out.println("findFail() called.");
        uDao.clear();
        assertNull(uDao.find("zxcv"));
    }

    @Test
    public void updatePass() throws DataAccessException {
        uDao.insert(braden);
        uDao.insert(anna);
        assertEquals(braden, uDao.find("bkwebb23"));
        assertDoesNotThrow(() -> uDao.update(braden));
        uDao.update(braden);
        assertEquals(braden, uDao.find("bkwebb23"));
        braden.setPassword("new_password");
        assertNotEquals(braden, uDao.find("bkwebb23"));
        uDao.update(braden);
        braden.setPassword("old_password");
        assertNotEquals(braden, uDao.find("bkwebb23"));
    }

    @Test
    public void updateFail() throws DataAccessException {
        uDao.insert(anna);
        uDao.insert(braden);
        braden.setUsername(anna.getUsername());
        assertThrows(DataAccessException.class, () -> uDao.update(braden));
    }

    @Test
    public void validatePass() throws DataAccessException {
        assertDoesNotThrow(() -> uDao.validate("bkwebb23", "pass"));
        assertFalse(uDao.validate(braden.getUsername(), braden.getPassword()));
        uDao.insert(braden);
        assertTrue(uDao.validate(braden.getUsername(), braden.getPassword()));
        assertFalse(uDao.validate(anna.getUsername(), anna.getPassword()));
    }

    @Test
    public void validateFail() throws DataAccessException {
        uDao.insert(braden);
        assertFalse(uDao.validate(braden.getUsername(), "pass"));
        assertFalse(uDao.validate(anna.getUsername(), anna.getPassword()));
        assertFalse(uDao.validate(anna.getUsername(), braden.getPassword()));
    }

    @Test
    public void clearPass() throws DataAccessException {
        System.out.println("clearPass() called.");

        uDao.insert(braden);
        uDao.clear();
        assertDoesNotThrow(() -> uDao.insert(braden));
    }
}
