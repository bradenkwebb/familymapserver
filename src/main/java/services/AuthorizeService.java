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

/**
 * A class for determining whether provided authentication information is valid.
 */
public class AuthorizeService implements Service {
    static public final Logger logger = Logger.getLogger("AuthorizeService");

    /**
     * Searches the database for the provided authToken and, if found, returns a User object corresponding to the User
     * to whom the authToken belongs. If not found, returns null.
     *
     * @param authToken the token to validate
     * @return the authenticated User if valid, otherwise null.
     */
    public User authorize(String authToken) {
        logger.entering("AuthorizeService", "authorize");
        Database db = new Database();
        Connection conn;

        try (Connection c = db.getConnection()) {
            conn = c;

            AuthToken token = new AuthTokenDAO(conn).find(authToken);

            if (token != null) {

                return new UserDAO(conn).find(token.getUsername());

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
