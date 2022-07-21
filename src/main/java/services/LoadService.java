package services;

import dao.*;
import model.Event;
import model.Person;
import model.User;
import requests.LoadRequest;
import results.LoadResult;
import results.Result;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implements the /load service.
 */
public class LoadService implements Service {
    public static final Logger logger = Logger.getLogger("LoadService");

    /**
     * Creates the object.
     */
    public LoadService() {}

    /**
     * Clears all data from the database (just like /clear API) and loads the user, person, and event data from the
     * request body into the database.
     *
     * @param r the LoadRequest object containing the relevant input data.
     */
    public Result load(LoadRequest r) {
        Result result = new Result();
        Database db = new Database();
        Connection conn = null;
        try (Connection c = db.getConnection()) {
            conn = c;
            ClearService clearService = new ClearService();
            clearService.clear(null);
            UserDAO uDao = new UserDAO(conn);
            PersonDAO pDao = new PersonDAO(conn);
            EventDAO eDao = new EventDAO(conn);

            for (User user : r.getUsers()) {
                uDao.insert(user);
            }
            for (Person person : r.getPersons()) {
                pDao.insert(person);
            }
            for (Event event : r.getEvents()) {
                eDao.insert(event);
            }
            result.setMessage(String.format("Successfully added %d users, %d persons, and %d events to the database.",
                                            r.getUsers().size(), r.getPersons().size(), r.getEvents().size()));
            result.setSuccess(true);
            db.closeConnection(true);
            return result;
        } catch(DataAccessException | SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            result.setSuccess(false);
            result.setMessage("Error: " + ex.getMessage() + "; likely due to invalid request");
            db.closeConnection(false);
        }
        return result;
    }
}
