package dao;

import model.User;
import java.sql.*;

public class UserDAO {
    /**
     * The connection to the SQLite database.
     */
    private final Connection conn;

    /**
     * Creates the database connection
     *
     * @param conn the database to connect to
     */
    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserts a new row, corresponding to a user, into the Users table of our database.
     *
     * @param user the user to add to the database
     * @throws DataAccessException
     */
    public void insert(User user) throws DataAccessException {
        // TODO implement me!
    }

    /**
     * Obtains a user from our database's Users table given their username.
     *
     * @param username the primary key to use to locate the user
     * @return the user as a User object if found; otherwise null
     * @throws DataAccessException
     */
    public User find(String username) throws DataAccessException {
        // TODO implement me!
        return null;
    }

    /**
     * Clears the Users table in our database.
     *
     * @throws DataAccessException
     */
    public void clear() throws DataAccessException {
        // TODO implement me!
    }

}