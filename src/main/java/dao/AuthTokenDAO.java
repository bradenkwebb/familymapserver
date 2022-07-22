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
     * @param conn the familymap database session
     */
    public AuthTokenDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserts a new row, corresponding to an authtoken, into the AuthTokens table of our database.
     *
     * @param auth the authtoken to add to the database.
     * @throws DataAccessException if error occurs
     */
    public void insert(AuthToken auth) throws DataAccessException {
        String sql = "INSERT INTO AuthTokens (authtoken, username) VALUES(?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, auth.getAuthtoken());
            stmt.setString(2, auth.getUsername());

            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DataAccessException("Error occurred while inserting into AuthTokens table");
        }
    }

    /**
     * Checks whether a given token string is in our database's AuthTokens table and if so,
     * returns it as an AuthToken object.
     *
     * @param token the token string to search for.
     * @return the token and associated username as an AuthToken object if found; otherwise null
     * @throws DataAccessException if error occurs
     */
    public AuthToken find(String token) throws DataAccessException {
        String sql = "SELECT * FROM AuthTokens WHERE authtoken = ?;";
        ResultSet rs;
        AuthToken auth;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token);
            rs = stmt.executeQuery();
            if (rs.next()) {
                auth = new AuthToken(rs.getString("authtoken"), rs.getString("username"));
                return auth;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DataAccessException("Error occurred while searching database for authtoken");
        }
        return null;
    }

    /**
     * Clears the AuthTokens table in our database.
     *
     * @throws DataAccessException if error occurs
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Authtokens";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DataAccessException("Error occurred while clearing AuthTokens table.");
        }
    }
}