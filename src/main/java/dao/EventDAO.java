package dao;

import model.Event;
import java.sql.*;
import java.util.List;

/**
 * A data access object for accessing, querying, and editing events in the SQL database.
 */
public class EventDAO {

    /**
     * The connection to the SQLite database.
     */
    private final Connection conn;

    /**
     * Creates the database connection
     *
     * @param conn the database to connect to?
     */
    public EventDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserts a new row, corresponding to an event, into the Events table of our database.
     *
     * @param event the event to add to the database
     * @throws DataAccessException
     */
    public void insert(Event event) throws DataAccessException {
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO Events (EventID, AssociatedUsername, PersonID, Latitude, Longitude, " +
                "Country, City, EventType, Year) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getAssociatedUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setFloat(4, event.getLatitude());
            stmt.setFloat(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting an event into the database");
        }
    }

    /**
     * Obtains an event from our database's Events table given the eventID.
     *
     * @param eventID the primary key to use to locate the event
     * @return the event as an Event object if found; otherwise null
     * @throws DataAccessException
     */
    public Event find(String eventID) throws DataAccessException {
        Event event;
        ResultSet rs;
        String sql = "SELECT * FROM Events WHERE EventID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("EventID"), rs.getString("AssociatedUsername"),
                        rs.getString("PersonID"), rs.getFloat("Latitude"), rs.getFloat("Longitude"),
                        rs.getString("Country"), rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Year"));
                return event;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding an event in the database");
        }

    }

    /**
     * Obtains a list of all of the events associated with the given user.
     *
     * @param username The username of the user whose associated events we're looking for.
     * @return a list of all of the events associated with the given user.
     * @throws DataAccessException
     */
    public List<Event> getAllFromUser(String username) throws DataAccessException {
        //TODO implement me!
        return null;
    }

    /**
     * Clears the Events table in our database.
     *
     * @throws DataAccessException
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Events";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the event table");
        }
    }
}
