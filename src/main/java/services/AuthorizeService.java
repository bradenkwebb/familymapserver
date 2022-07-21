package services;

import dao.AuthTokenDAO;
import dao.DataAccessException;
import dao.Database;
import dao.UserDAO;
import model.AuthToken;
import model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthorizeService implements Service {
    static public final Logger logger = Logger.getLogger("AuthorizeService");
    public User authorize(String authToken) {
        Database db = new Database();
        Connection conn = null;

        try (Connection c = db.getConnection()) {
            conn = c;
            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            AuthToken token = aDao.find(authToken);
            if (token != null) {
                UserDAO uDao = new UserDAO(conn);
                return uDao.find(token.getUsername());
            } else {
                return null;
            }
        } catch (DataAccessException | SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            db.closeConnection(false);
        }
        return null;
    }
}
