package services;

import dao.AuthTokenDAO;
import dao.DataAccessException;
import dao.Database;
import dao.UserDAO;
import model.AuthToken;
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
        Connection conn;
        try (Connection c = db.getConnection()) {
            conn = c;

            if(new UserDAO(conn).validate(r.getUsername(), r.getPassword())) {
                String generatedToken = UUID.randomUUID().toString();
                AuthToken token = new AuthToken(generatedToken, r.getUsername());
                new AuthTokenDAO(conn).insert(token);
                String personID = new UserDAO(conn).find(r.getUsername()).getPersonID();

                result.setUsername(r.getUsername());
                result.setAuthToken(generatedToken);
                result.setSuccess(true);
                result.setPersonID(personID);
            } else {
                result.setMessage("Error: Invalid credentials");
            }

            db.closeConnection(true);

        } catch (DataAccessException | SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);

            db.closeConnection(false);
            result.setSuccess(false);
            result.setMessage("Error: An error occurred when logging in;" + e.getMessage());
        }
        return result;
    }
}
