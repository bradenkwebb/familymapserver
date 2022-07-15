package dao;

import model.Person;
import java.sql.*;

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
     * @param conn the database to connect to?
     */
    public PersonDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserts a new row, corresponding to a person, into the Persons table of our database.
     *
     * @param person the person to add to the database
     * @throws DataAccessException
     */
    public void insert(Person person) throws DataAccessException {
        // TODO implement me!
    }

    /**
     * Obtains a person from our database's Persons table given the personID.
     *
     * @param personID the primary key to use to locate the person.
     * @return the person as a Person object if found; otherwise null
     * @throws DataAccessException
     */
    public Person find(String personID) throws DataAccessException {
        // TODO implement me!
        return null;
    }

    /**
     * Clears the Persons table in our database.
     *
     * @throws DataAccessException
     */
    public void clear() throws DataAccessException {
        //TODO implement me!
    }
}