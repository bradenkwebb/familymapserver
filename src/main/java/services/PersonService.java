package services;

import dao.DataAccessException;
import dao.Database;
import dao.PersonDAO;
import model.Person;
import results.PeopleResult;
import results.PersonResult;
import results.Result;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implements the /person Web API method.
 */
public class PersonService implements Service {
    public static final Logger logger = Logger.getLogger("PersonService");
    static final int ID_INDEX = 2;

    /**
     * Creates the object.
     */
    public PersonService() {}

    public Result getResult(String username, String urlPath) {
        logger.entering("PersonService", "getResult");
        Result result = new PersonResult();
        Database db = new Database();
        Connection conn;

        try (Connection c = db.getConnection()) {
            conn = c;
            PersonDAO personDAO = new PersonDAO(conn);
            List<Person> people = personDAO.getAllFamily(username);
            if (isSpecific(urlPath)) {
                result = individual(result, urlPath, people);
            } else {
                result = new PeopleResult(people);
                result.setSuccess(true);
            }
            db.closeConnection(true);
        } catch (DataAccessException | SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            db.closeConnection(false);
            result.setSuccess(false);
            result.setMessage("Error:" + ex.getMessage());
        }
        return result;
    }

    /**
     * Determines whether the given urlPath is requesting all people, or just one person
     *
     * @param urlPath the HTTP Request URI to parse
     * @return true if the number of components delimited by "/" is the number expected for a specific person
     * request, otherwise false
     */
    private boolean isSpecific(String urlPath) {
        String[] parts = urlPath.split("/");
        return parts.length == ID_INDEX + 1;
    }

    private Person getPersonFromList(String personID, List<Person> people) {
        for (Person person : people) {
            if (personID.equals(person.getPersonID())) {
                return person;
            }
        }
        return null;
    }

    private String getIDFromPath(String urlPath) {
        String[] parts = urlPath.split("/");
        return parts[ID_INDEX];
    }

    private Result individual(Result r, String urlPath, List<Person> people) {
        Person person = getPersonFromList(getIDFromPath(urlPath), people);
        if (person != null) {
            r = new PersonResult(person);
            r.setSuccess(true);
        } else {
            r.setSuccess(false);
            r.setMessage("Error: Person not found (at least not in this user's tree)");
        }
        return r;
    }
}
