package services;

import dao.*;
import model.AuthToken;
import model.Person;
import model.User;
import requests.RegisterRequest;
import results.RegisterResult;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
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
     * @return the results of the registration attempt
     */
    public RegisterResult register(RegisterRequest r) {
        logger.entering("RegisterService", "register");

        RegisterResult result = new RegisterResult();
        Database db = new Database();
        Connection conn;

        try(Connection c = db.getConnection()){
            conn = c;

            UserDAO uDao = new UserDAO(conn);

            if (!requestNotNull(r)) {
                result.setSuccess(false);
                result.setMessage("Error: missing or null field");
            } else if (uDao.find(r.getUsername()) != null) {
                result.setSuccess(false);
                result.setMessage("Error: Username already taken");
            } else if ( ! (r.getGender().equalsIgnoreCase("m") || // If gender is neither "m" nor "f"...
                            r.getGender().equalsIgnoreCase("f"))) {
                result.setSuccess(false);
                result.setMessage("Error: Gender field must be either \"f\" or \"m\"");
            } else {

                // Create and add the new user
                User user = new User(r.getUsername(), r.getPassword(), r.getEmail(),
                        r.getFirstName(), r.getLastName(), r.getGender(),
                    r.getFirstName() + "_" + r.getLastName() + UUID.randomUUID().toString().substring(0, 6));
                uDao.insert(user);

                // Generate a family tree for the user
                new PersonDAO(conn).generate(user, 4);

                // Generate a new authToken for the user's current session
                AuthToken token = new AuthToken(UUID.randomUUID().toString(), r.getUsername());
                new AuthTokenDAO(conn).insert(token);

                result.setSuccess(true);
                result.setAuthtoken(token.getAuthtoken());
                result.setUsername(user.getUsername());
                result.setPersonID(user.getPersonID());
            }

            // Commit transaction and close the connection
            db.closeConnection(true);

        } catch (DataAccessException | SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            ex.printStackTrace();
            db.closeConnection(false);

            result.setSuccess(false);
            result.setMessage("Registration failed; " + ex.getMessage());
        }
        return result;
    }

    private boolean requestNotNull(RegisterRequest r) {

        Set<String> requestFields = new HashSet<>();
        requestFields.add(r.getUsername());
        requestFields.add(r.getPassword());
        requestFields.add(r.getEmail());
        requestFields.add(r.getFirstName());
        requestFields.add(r.getLastName());
        requestFields.add(r.getGender());

        for (String field : requestFields) {
            if (field == null) {
                return false;
            }
        }
        return true;
    }
}
