package dao;

import model.Person;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A data access object for accessing, querying, and editing people in the SQL database.
 */
public class PersonDAO {

    /**
     * The connection to the SQLite database.
     */
    private final Connection conn;

    /**
     * Creates the database connection
     *
     * @param conn the familymap database session
     */
    public PersonDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserts a new row, corresponding to a person, into the Persons table of our database.
     *
     * @param person the person to add to the database
     * @throws DataAccessException if an error occurs
     */
    public void insert(Person person) throws DataAccessException {
        String sql = "INSERT INTO Persons (personID, associatedUsername, firstName, lastName, gender)" +
                     " VALUES(?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getAssociatedUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            // TODO what about fatherID, motherID, spouseID?

            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DataAccessException("Error occurred while inserting into Persons table");
        }
    }

    /**
     * Obtains a person from our database's Persons table given the personID.
     *
     * @param personID the primary key to use to locate the person.
     * @return the person as a Person object if found; otherwise null
     * @throws DataAccessException if an error occurs
     */
    public Person find(String personID) throws DataAccessException {
        String sql = "SELECT * FROM Persons WHERE personID = ?;";
        ResultSet rs;
        Person person;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);

            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("personID"), rs.getString("associatedUsername"),
                                    rs.getString("firstName"), rs.getString("lastName"),
                                    rs.getString("gender"));
                // TODO what about fatherID, motherID, spouseID?
                return person;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DataAccessException("Error occurred while searching through person table");
        }
    }

    /**
     * Obtains a list of all of the people who are ancestors of a given person.
     *
     * @param personID the identifier for the person whose ancestors we are searching for.
     * @return a list of all of the people who are ancestors of the specified person.
     * @throws DataAccessException if an error occurs
     */
    public List<Person> getAllAncestors(String personID) throws DataAccessException {
        // TODO implement me!
        return null;
    }

    /**
     * Obtains a list of all of the people who are associated with a given user.
     *
     * @param username the identifier for the user in question.
     * @return a list of all of the people in the user's family tree.
     * @throws DataAccessException if an error occurs
     */
    public List<Person> getAllFamily(String username) throws DataAccessException {
        String sql = "SELECT * FROM Persons WHERE username = ?;";
        ResultSet rs;
        List<Person> family = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                do {
                    family.add(new Person(rs.getString("personID"), rs.getString("associatedUsername"),
                            rs.getString("firstName"), rs.getString("lastName"),
                            rs.getString("gender")));
                } while (rs.next());
                return family;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DataAccessException("Error occurred while trying to get all family from persons table");
        }
    }

    /**
     * Clears the Persons table in our database.
     *
     * @throws DataAccessException if an error occurs
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Persons";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DataAccessException("Error occurred while clearing Persons table");
        }
    }
}