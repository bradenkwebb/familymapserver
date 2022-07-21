package services;

import dao.*;
import model.Person;
import model.User;
import requests.FillRequest;
import results.FillResult;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implements the /fill/[username]/{generations} service.
 */
public class FillService implements Service {
    public static final Logger logger = Logger.getLogger("FillService");
    /**
     * Creates the object.
     */
    public FillService() {}

    /**
     * Populates the server's database with generated data for the specified username. If there is any data in the
     * database already associated with the given username, it is deleted.
     *
     * @param r the FillRequest object containing information from the request body.
     * @param username the associated username; must already be registered with the server.
     * @param generations the number of generations of ancestors to generate for the user. If not provided, defaults
     *                    to 4.
     */
    public FillResult fill(FillRequest r, String username, int generations) {
        logger.entering("FillService", "fill");

        FillResult result = new FillResult();
        Database db = new Database();
        Connection conn = null;

        try (Connection c = db.getConnection()) {
            conn = c;

            UserDAO uDao = new UserDAO(conn);
            PersonDAO pDao = new PersonDAO(conn);
            EventDAO eDao = new EventDAO(conn);
            AuthTokenDAO aDao = new AuthTokenDAO(conn);

            pDao.clearUser(username);
            User currUser  = uDao.find(username);
            Person userPerson = pDao.generate(currUser, generations);
//            Person userPerson = pDao.generate(username, uDao.find(username).getGender(), generations);
//            User currUser  = uDao.find(username);
            currUser.setPersonID(userPerson.getPersonID());
            currUser.setFirstName(userPerson.getFirstName());
            currUser.setLastName(userPerson.getLastName());
            currUser.setGender(userPerson.getGender());
            uDao.update(currUser);

            int personCount = pDao.famSize(currUser.getUsername());
            int eventCount = eDao.getAllFromUser(currUser.getUsername()).size();

            db.closeConnection(true);

            result.setMessage("Successfully added " + personCount + " new persons and " +
                                eventCount + " events to the database.");
            result.setSuccess(true);

            logger.exiting("FillService", "fill");
            return result;
        } catch (DataAccessException | SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            db.closeConnection(false);
            result.setMessage("Error: " + ex.getMessage());
            result.setSuccess(false);
            logger.exiting("FillService", "fill");
            return result;
        }
    }
}
