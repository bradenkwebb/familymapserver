package services;

import dao.*;
import model.AuthToken;
import model.Person;
import model.User;
import requests.RegisterRequest;
import results.RegisterResult;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implements the /user/register Web API method.
 */
public class RegisterService implements Service {

    /**
     * Instantiates a RegisterService object.
     */
    public RegisterService() {}

    private static final Logger logger = Logger.getLogger("RegisterService");

    /**
     * Creates a new user account, generates 4 generations of ancestor data for the new user, logs the user in,
     * and returns an authtoken
     *
     * @param r the RegisterRequest object to process
     * @return
     */
    public RegisterResult register(RegisterRequest r) {
        logger.entering("RegisterService", "register");

        RegisterResult result = new RegisterResult();

        Database db = new Database();
        Connection conn = null;
        try(Connection c = db.getConnection()){
            conn = c;
            // Generate new personID, create User object, and add to database
            String generatedID = UUID.randomUUID().toString();
            User newUser = new User(r.getUsername(), r.getPassword(), r.getEmail(), r.getFirstName(),
                    r.getLastName(), r.getGender(), generatedID);
            new UserDAO(db.getConnection()).insert(newUser);

            // Create new Person object, and add to database
            Person newPerson = new Person(generatedID, r.getUsername(), r.getFirstName(), r.getLastName(), r.getGender());
            new PersonDAO(db.getConnection()).insert(newPerson);

            // Generate new AuthToken object and add to database
            AuthToken newAuthToken = new AuthToken(UUID.randomUUID().toString(), r.getUsername());
            new AuthTokenDAO(db.getConnection()).insert(newAuthToken);

            // Create result object

            result.setSuccess(true);
            result.setAuthtoken(newAuthToken.getAuthtoken());
            result.setUsername(newUser.getUsername());
            result.setPersonID(newUser.getPersonID());

            // Commit transaction and close the connection
            db.closeConnection(true);

            logger.exiting("RegisterService", "register");
            return result;
        } catch (DataAccessException | SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            ex.printStackTrace();
            db.closeConnection(false);

            result.setSuccess(false);
            result.setMessage("Registration failed; " + ex.getMessage());

            logger.exiting("RegisterService", "register");
            return result;
        }
    }
}
