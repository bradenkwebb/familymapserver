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
     * @return
     */
    public RegisterResult register(RegisterRequest r) {
        logger.entering("RegisterService", "register");

        RegisterResult result = new RegisterResult();
        Database db = new Database();
        Connection conn = null;

        try(Connection c = db.getConnection()){
            conn = c;

            PersonDAO pDao = new PersonDAO(conn);
            UserDAO uDao = new UserDAO(conn);
            AuthTokenDAO aDao = new AuthTokenDAO(conn);

            if (!requestNotNull(r)) {
                result.setSuccess(false);
                result.setMessage("Error: missing or null field");
            } else if (uDao.find(r.getUsername()) != null) {
                result.setSuccess(false);
                result.setMessage("Error: Username already taken");
            } else if ( ! (r.getGender().equalsIgnoreCase("m") ||
                            r.getGender().equalsIgnoreCase("f"))) {
                result.setSuccess(false);
                result.setMessage("Error: Gender field must be either \"f\" or \"m\"");
            } else {

                User user = new User(r.getUsername(), r.getPassword(), r.getEmail(),
                        r.getFirstName(), r.getLastName(), r.getGender(),
                    r.getFirstName() + "_" + r.getLastName() + UUID.randomUUID().toString().substring(0, 6));

                uDao.insert(user);

                // I'm not sure whether or not I still need to update the person ID for the user
                Person person = pDao.generate(user, 4);

                AuthToken token = new AuthToken(UUID.randomUUID().toString(), r.getUsername());
                aDao.insert(token);

                result.setSuccess(true);
                result.setAuthtoken(token.getAuthtoken());
                result.setUsername(user.getUsername());
                result.setPersonID(user.getPersonID());
            }
                // Commit transaction and close the connection
                db.closeConnection(true);
            return result;
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
