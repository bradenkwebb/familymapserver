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

    /**
     * Collects the requested people relevant to the user (either all, 1, or none) and returns them in an appropriate
     * Result object.
     *
     * @param username the user whose family members we're searching for
     * @param urlPath the url path determining whether we're searching for one or multiple people
     * @return Returns either a PersonResult(), PeopleResult(), or Result() object corresponding to whether the provided
     *          urlPath requests an individual or all people in the user's family tree and whether the
     */
    public Result getResult(String username, String urlPath) {
        logger.entering("PersonService", "getResult");
        Result result = new PersonResult();
        Database db = new Database();
        Connection conn;

        try (Connection c = db.getConnection()) {
            conn = c;
            List<Person> people = new PersonDAO(conn).getAllFamily(username);
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

    /**
     * Gets the Person object with matching personID from the provided list of Person objects.
     *
     * @param personID the ID to search for
     * @param people the list of Person objects
     * @return the Person() if found, otherwise null
     */
    private Person getPersonFromList(String personID, List<Person> people) {
        for (Person person : people) {
            if (personID.equals(person.getPersonID())) {
                return person;
            }
        }
        return null;
    }

    /**
     * Determines whether person requested by the provided urlPath is in the list of people, and returns a
     * corresponding Result object.
     *
     * @param r the result reference to update
     * @param urlPath a String that should specify an individual personID
     * @param people a list of all the people in the user's family tree
     * @return a PersonResult() object if the personID is found in the list, otherwise, a failed Result() object
     */
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

    /**
     * Obtains a personID from the provided urlPath.
     *
     * @param urlPath the String from which to extract the personID
     * @return the personID as a String
     */
    private String getIDFromPath(String urlPath) {
        String[] parts = urlPath.split("/");
        return parts[ID_INDEX];
    }
}
