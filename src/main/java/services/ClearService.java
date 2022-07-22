package services;

import dao.*;
import results.Result;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implements the /clear service.
 */
public class ClearService implements Service {
    private static final Logger logger = Logger.getLogger("ClearService");
    /**
     * Creates the object
     */
    public ClearService() {}

    /**
     * Deletes ALL the data from the database, including user, authtoken, person, and event data.
     *
     * @return a Result() object describing the success or failure of the action.
     */
    public Result clear() {
        Result result = new Result();
        Database db = new Database();
        try (Connection conn = db.openConnection()) {
            new UserDAO(conn).clear();
            new PersonDAO(conn).clear();
            new EventDAO(conn).clear();
            new AuthTokenDAO(conn).clear();
            db.closeConnection(true);
            result.setMessage("Clear succeeded.");
            result.setSuccess(true);
        } catch (DataAccessException | SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            db.closeConnection(false);
            result.setMessage("Error: " + ex.getMessage());
            result.setSuccess(false);
        }
        return result;
    }
}
