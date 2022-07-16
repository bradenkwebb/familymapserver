package dao;

import dao.AuthTokenDAO;
import dao.DataAccessException;
import dao.Database;
import model.AuthToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class AuthTokenDAOTest {
    private Database db;
    private AuthToken bestToken;
    private AuthTokenDAO aDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        System.out.println("setUp() called.");
        db = new Database();
        Connection conn = db.getConnection();
        aDao = new AuthTokenDAO(conn);
        aDao.clear();
        bestToken = new AuthToken("123", "bkwebb23");
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        System.out.println("tearDown() called");
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        System.out.println("insertPass() called.");
        aDao.insert(bestToken);
        AuthToken compareToken = aDao.find(bestToken.getAuthtoken());

        assertNotNull(compareToken);
        assertEquals(bestToken, compareToken);
    }

    @Test
    public void insertFail() throws DataAccessException {
        System.out.println("insertFail() called.");
        aDao.insert(bestToken);
        assertThrows(DataAccessException.class, () -> aDao.insert(bestToken));
    }

    @Test
    public void findPass() throws DataAccessException {
        System.out.println("findPas() called.");
        aDao.insert(bestToken);
        AuthToken compareToken = aDao.find(bestToken.getAuthtoken());

        assertNotNull(compareToken);
        assertEquals(bestToken, compareToken);
    }

    @Test
    public void findFail() throws DataAccessException {
        System.out.println("findFail() called.");
        aDao.clear();
        AuthToken token = aDao.find(bestToken.getAuthtoken());
        assertNull(token);
    }

    @Test
    public void clearPass() throws DataAccessException {
        System.out.println("clearPass() called.");
        aDao.insert(bestToken);
        aDao.clear();
        assertNull(aDao.find(bestToken.getAuthtoken()));
        assertDoesNotThrow(() -> aDao.insert(bestToken));
    }
}