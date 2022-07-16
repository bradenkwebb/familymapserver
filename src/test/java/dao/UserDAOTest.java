package dao;

import dao.DataAccessException;
import dao.Database;
import dao.UserDAO;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {
    private Database db;
    private User bestUser;
    private UserDAO uDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        System.out.println("setUp() called.");
        db = new Database();

        bestUser = new User("bkwebb23", "mypass", "bw@gmail.com",
                            "Braden", "Webb", "m", "123");

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

        uDao.insert(bestUser);
        User compareUser = uDao.find(bestUser.getUsername());

        assertNotNull(compareUser);
        assertEquals(bestUser, compareUser);
    }

    @Test
    public void insertFail() throws DataAccessException {
        System.out.println("insertFail() called.");

        uDao.insert(bestUser);
        assertThrows(DataAccessException.class, () -> uDao.insert(bestUser));
    }

    @Test
    public void findPass() throws DataAccessException {
        System.out.println("findPass() called.");

        uDao.insert(bestUser);

        User compareUser = uDao.find(bestUser.getUsername());

        assertNotNull(compareUser);
        assertEquals(bestUser, compareUser);
    }

    @Test
    public void findFail() throws DataAccessException {
        System.out.println("findFail() called.");
        uDao.clear();
        assertNull(uDao.find("zxcv"));
    }

    @Test
    public void clearPass() throws DataAccessException {
        System.out.println("clearPass() called.");

        uDao.insert(bestUser);
        uDao.clear();
        assertDoesNotThrow(() -> uDao.insert(bestUser));
    }
}
