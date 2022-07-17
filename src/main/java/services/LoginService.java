package services;

import dao.AuthTokenDAO;
import dao.DataAccessException;
import dao.Database;
import dao.UserDAO;
import model.AuthToken;
import model.Person;
import requests.LoginRequest;
import results.LoginResult;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implements the /user/login service.
 */
public class LoginService implements Service {
    private static final Logger logger = Logger.getLogger("LoginService");

    /**
     * Creates the object.
     */
    public LoginService() {}

    /**
     * Logs the user in and responds with an authtoken.
     *
     * @param r the LoginRequest object to process.
     */
    public LoginResult login(LoginRequest r) {
        logger.entering("LoginService", "login");
        LoginResult result = new LoginResult();

        Database db = new Database();
        Connection conn = null;
        try (Connection c = db.getConnection()) {
            conn = c;
            boolean valid = new UserDAO(conn).validate(r.getUsername(), r.getPassword());

            if(valid) {
                String generatedToken = UUID.randomUUID().toString();
                AuthToken token = new AuthToken(generatedToken, r.getUsername());
                new AuthTokenDAO(conn).insert(token);
                String personID = new UserDAO(conn).find(r.getUsername()).getPersonID();

                result.setUsername(r.getUsername());
                result.setAuthtoken(generatedToken);
                result.setSuccess(valid);
                result.setPersonID(personID);
            } else {
                result.setSuccess(valid);
                result.setMessage("Invalid credentials");
            }
            db.closeConnection(true);
            return result;
        } catch (DataAccessException | SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            db.closeConnection(false);
        }

        result.setSuccess(false);
        result.setMessage("An error occurred when logging in.");
        logger.exiting("LoginService", "login");
        return result;
    }
}
