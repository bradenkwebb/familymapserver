package dao;

import model.AuthToken;
import java.sql.*;

/**
 * A data access object for accessing, querying, and editing events in the SQL database.
 */
public class AuthTokenDAO {

    /**
     * The connection to the SQLite database.
     */
    private final Connection conn;

    /**
     * Creates the database connection.
     *
     * @param conn
     */
    public AuthTokenDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserts a new row, corresponding to an authtoken, into the AuthTokens table of our database.
     *
     * @param auth the authtoken to add to the database.
     * @throws DataAccessException
     */
    public void insert(AuthToken auth) throws DataAccessException {
        // TODO implement me
    }

    /**
     * Checks whether a given token string is in our database's AuthTokens table and if so,
     * returns it as an AuthToken object.
     *
     * @param token the token string to search for.
     * @return the token and associated username as an AuthToken object if found; otherwise null
     * @throws DataAccessException
     */
    public AuthToken find(String token) throws DataAccessException {
        // TODO implement me
        return null;
    }

    /**
     * Clears the AuthTokens table in our database.
     *
     * @throws DataAccessException
     */
    public void clear() throws DataAccessException {
        // TODO imlpement me!
    }
}