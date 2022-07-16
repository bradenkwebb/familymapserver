package services;

import dao.*;
import model.AuthToken;
import model.Person;
import model.User;
import requests.RegisterRequest;
import results.RegisterResult;

import javax.xml.crypto.Data;
import java.sql.Connection;
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
        Database db = new Database();
        try {
            db.openConnection();

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
            RegisterResult result = new RegisterResult();
            result.setSuccess(true);
            result.setAuthtoken(newAuthToken.getAuthtoken());
            result.setUsername(newUser.getUsername());
            result.setPersonID(newUser.getPersonID());

            // Commit transaction and close the connection
            db.closeConnection(true);
        } catch (DataAccessException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            ex.printStackTrace();
            db.closeConnection(false);

            RegisterResult result = new RegisterResult();
            result.setSuccess(false);
            result.setMessage("Registration failed; " + ex.getMessage());
            return result;
        }
        return null;
        /*
        Database db = new Database();

        Connection connection = null;
        try(Connection c = db.getConnection()){
            connection = c;

            // Start a transaction
            connection.setAutoCommit(false);
            EventDAO eventDAO = new EventDAO(c);
            eventDAO.clear();
            eventDAO.insert(new Event("1", "bkwebb23", "2", 42.24f, 23.23f,
                            "Slovakia", "Nitra", "Mission", 2018));

            UserDAO userDAO = new UserDAO(c);
            userDAO.clear();
            userDAO.insert(new User("bkwebb23", "mypass", "bw@gmail.com",
                    "Braden", "Webb", "m", "2"));

            connection.commit();

            Event myEvent = eventDAO.find("1");
            System.out.println(myEvent.getAssociatedUsername());

            User myUser = userDAO.find("bkwebb23");
            System.out.println(myUser.getFirstName() + " " + myUser.getLastName());

        }
        catch(SQLException ex) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (java.sql.SQLException ex1) {
                System.out.println("This is the line that threw the exception!");
                System.out.println(ex1.getMessage());
            }

            ex.printStackTrace();
            System.out.println(ex.getMessage());

        }
        catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
         */
    }
}
