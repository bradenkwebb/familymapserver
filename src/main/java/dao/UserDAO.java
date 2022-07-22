package dao;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO {
    public static final Logger logger = Logger.getLogger("UserDAO");

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
     * @throws DataAccessException if an error occurs
     */
    public void insert(User user) throws DataAccessException {
        logger.entering("UserDAO", "insert");
        String sql = "INSERT INTO Users (username, password, email, firstName," +
                     "lastName, gender, personID) VALUES(?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());

            logger.finest(stmt.toString());

            stmt.executeUpdate();
            logger.exiting("UserDAO", "insert");
        } catch (SQLException e) {
            e.printStackTrace();
            logger.exiting("UserDAO", "insert");
            throw new DataAccessException("Error encountered while inserting a user into the database");
        }
    }

    /**
     * Updates the row in the Users table matching the provided user's username to the current state of the user.
     *
     * @param user the user to update
     * @throws DataAccessException if an error occurs
     */
    public void update(User user) throws DataAccessException {
        logger.entering("UserDAO", "insert");
        String sql = "UPDATE Users " + "SET username = ?, password = ?, email = ?, firstName = ?, " +
                     "lastName = ?, gender = ?, personID = ?" + "WHERE username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());
            stmt.setString(8, user.getUsername());

            logger.finest(stmt.toString());

            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            logger.log(Level.WARNING, ex.getMessage(), ex);
            throw new DataAccessException("Error encountered when updating user in database");
        }
    }

    /**
     * Obtains a user from our database's Users table given their username.
     *
     * @param username the primary key to use to locate the user
     * @return the user as a User object if found; otherwise null
     * @throws DataAccessException if an error occurs in searching the database for the username
     */
    public User find(String username) throws DataAccessException {
        User user;
        ResultSet rs;
        String sql = "SELECT * FROM Users WHERE username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()){
                user = new User(rs.getString("username"), rs.getString("password"),
                        rs.getString("email"), rs.getString("firstName"),
                        rs.getString("lastName"), rs.getString("gender"),
                        rs.getString("personID"));
                return user;
            }
            return null;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DataAccessException("Error encountered while finding a user in the database");
        }
    }

    /**
     * Checks whether the user's credentials match.
     *
     * @param username the primary key to search for in the database
     * @param password the value that should be the user's password in the database
     * @return true if values match; false otherwise
     * @throws DataAccessException if an error occurs
     */
    public boolean validate(String username, String password) throws DataAccessException{
        String sql = "SELECT * FROM Users WHERE username=?;";
        ResultSet rs;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return password.equals(rs.getString("password"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DataAccessException("Error occurred while validating user's credentials.");
        }
        return false;
    }

    /**
     * Clears the Users table in our database.
     *
     * @throws DataAccessException if an error occurs
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Users";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the users table");
        }
    }
}