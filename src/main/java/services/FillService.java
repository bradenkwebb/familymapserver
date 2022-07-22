package services;

import dao.*;
import model.Person;
import model.User;
import requests.FillRequest;
import results.Result;

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
    public Result fill(FillRequest r, String username, int generations) {
        logger.entering("FillService", "fill");

        Result result = new Result();
        Database db = new Database();
        Connection conn;

        try (Connection c = db.getConnection()) {
            conn = c;

            UserDAO uDao = new UserDAO(conn);
            PersonDAO pDao = new PersonDAO(conn);
            EventDAO eDao = new EventDAO(conn);

            pDao.clearUser(username);
            eDao.clearUser(username);
            User currUser  = uDao.find(username);
            if (currUser == null) {
                result.setSuccess(false);
                result.setMessage("Error: Invalid username");
            } else if (!validNumGen(generations)) {
                result.setSuccess(false);
                result.setMessage("Error: Invalid generations parameter");
            } else {
                Person userPerson = pDao.generate(currUser, generations);

                currUser.setPersonID(userPerson.getPersonID());
                currUser.setFirstName(userPerson.getFirstName());
                currUser.setLastName(userPerson.getLastName());
                currUser.setGender(userPerson.getGender());
                uDao.update(currUser);

                int personCount = pDao.famSize(currUser.getUsername());
                int eventCount = eDao.getAllFromUser(currUser.getUsername()).size();

                result.setMessage("Successfully added " + personCount + " persons and " +
                        eventCount + " events to the database.");
                result.setSuccess(true);
            }

            db.closeConnection(true);

        } catch (DataAccessException | SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            db.closeConnection(false);
            result.setMessage("Error: " + ex.getMessage());
            result.setSuccess(false);
        }
        return result;
    }
    private boolean validNumGen(int numGen) {
        return numGen >= 0;
    }
}
