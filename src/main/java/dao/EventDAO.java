package dao;

import com.google.gson.Gson;
import model.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A data access object for accessing, querying, and editing events in the SQL database.
 */
public class EventDAO {
    public static final Logger logger = Logger.getLogger("EventDAO");

    static JsonLocationArray locations;

    static {
        try {
            locations = parse(new File("json/locations.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Fills a JsonLocationArray object with the contents of the provided JSON file
     *
     * @param file the JSON file with the data to use
     * @return a potentially null JsonLocationArray object
     * @throws IOException if an error occurs
     */
    private static JsonLocationArray parse(File file) throws IOException {
        try(FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            Gson gson = new Gson();
            return gson.fromJson(bufferedReader, JsonLocationArray.class);
        }
    }

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
     * @throws DataAccessException if error occurs
     */
    public void insert(Event event) throws DataAccessException {
        String sql = "INSERT INTO Events (EventID, AssociatedUsername, PersonID, Latitude, Longitude, " +
                "Country, City, EventType, Year) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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
     * @throws DataAccessException if an error occurs
     */
    public Event find(String eventID) throws DataAccessException {
        Event event = new Event();
        ResultSet rs;
        String sql = "SELECT * FROM Events WHERE EventID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("eventID"), rs.getString("associatedUsername"),
                        rs.getString("personID"), rs.getFloat("latitude"),
                        rs.getFloat("longitude"), rs.getString("country"),
                        rs.getString("city"), rs.getString("eventType"),
                        rs.getInt("year"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding an event in the database");
        }
        return event;
    }

    /**
     * Obtains a list of all of the events associated with the given user.
     *
     * @param username The username of the user whose associated events we're looking for.
     * @return a list of all of the events associated with the given user.
     * @throws DataAccessException if error occurs
     */
    public List<Event> getAllFromUser(String username) throws DataAccessException {
        logger.entering("EventDAO", "getAllFromUser");

        List<Event> events = new ArrayList<>();

        String sql = "SELECT * FROM Events WHERE associatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            logger.finer(stmt.toString());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Event event = new Event(rs.getString("eventID"), rs.getString("associatedUsername"),
                                        rs.getString("personID"), rs.getFloat("latitude"),
                                        rs.getFloat("longitude"), rs.getString("country"),
                                        rs.getString("city"), rs.getString("eventType"),
                                        rs.getInt("year"));
                events.add(event);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DataAccessException("An error occurred while obtaining events from database");
        }
        return events;
    }

    /**
     * Clears the Events table in our database.
     *
     * @throws DataAccessException if error occurs
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

    /**
     * Deletes all events in the database associated with a given username.
     *
     * @param username the associatedUsername to look for
     * @throws DataAccessException if an error occurs
     */
    public void clearUser(String username) throws DataAccessException {
        logger.entering("EventDAO", "clearUser");

        String sql = "DELETE FROM Events WHERE associatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DataAccessException("Error occurred while clearing events from " + username + "'s family tree");
        }
    }

    /**
     * Creates an event corresponding to a birth given an associated username, a personID for whom the event should
     * correspond, and the birth year of their child in the family tree. the birth event generated will be 13 and 50
     * years before their child's birth year.
     *
     * @param username the identifier for the associated user
     * @param personID the identifier for the person whose birth this is
     * @param personBirthYear the year a person
     * @throws DataAccessException if an error occurs
     */
    public void generateBirth(String username, String personID, int personBirthYear) throws DataAccessException {
        Location birthLocation = randomLocation();
        Event birth = new Event(randomID(),username, personID, birthLocation.getLatitude(),
                                birthLocation.getLongitude(), birthLocation.getCountry(),
                                birthLocation.getCity(), "birth", personBirthYear);
        this.insert(birth);
    }

    /**
     * Creates and inserts two marriage events—one for the husband, and one for the wife—that have matching
     * dates and locations
     *
     * @param username the username to associate the marriages too
     * @param husbandID the personID for the husband
     * @param wifeID the personID for the wife
     * @throws DataAccessException if an error occurs
     */
    public void generateMarriage(String username, String husbandID, String wifeID) throws DataAccessException {
        logger.entering("EventDAO", "generateMarriage");

        Integer husbandBirthYear = getBirthYear(husbandID);
        Integer wifeBirthYear = getBirthYear(wifeID);

        if (husbandBirthYear == null || wifeBirthYear == null) {
            throw new DataAccessException("Unable to determine appropriate marriage date without birthyears");
        }

        Location marriageLocation = randomLocation();
        Event marriage = new Event(randomID(), username, husbandID, marriageLocation.getLatitude(),
                                   marriageLocation.getLongitude(), marriageLocation.getCountry(),
                                   marriageLocation.getCity(), "marriage",
                                   randomYear(Math.max(wifeBirthYear, husbandBirthYear) + 13,
                                              Math.min(wifeBirthYear, husbandBirthYear) + 120));
        this.insert(marriage);
        marriage.setEventID(randomID());
        marriage.setPersonID(wifeID);
        this.insert(marriage);
    }

    /**
     * Creates a death event for the person with the provided personID and inserts the event into the database. We
     * check to ensure that the death occurs after any other events with which the person is associated
     *
     * @param username the user to whom this person is associated
     * @param personID the identifier for the person whose death this is
     * @throws DataAccessException if an error occurs
     */
    public void generateDeath(String username, String personID) throws DataAccessException {
        logger.entering("EventDAO", "generateDeath");

        List<Event> personEvents = getAllFromPerson(personID);
        if (personEvents.size() ==0) {
            logger.log(Level.SEVERE, "Person " + personID + " has no events associated with them");
            throw new DataAccessException("Error: Cannot generate death event because person "
                    + personID + " has no events associated with them");
        }
        Event birth = Collections.min(personEvents, Comparator.comparing(Event::getYear));
        Event mostRecent = Collections.max(personEvents, Comparator.comparing(Event::getYear));
        int deathYear = randomYear(mostRecent.getYear(), birth.getYear() + 120);
        Location deathLocation = randomLocation();
        Event deathEvent = new Event(randomID(), username, personID, deathLocation.getLatitude(),
                                     deathLocation.getLongitude(), deathLocation.getCountry(),
                                     deathLocation.getCity(), "death", deathYear);
        this.insert(deathEvent);
    }

    /**
     * Updates every occurrence of the provided person's ID in the Events table with the new ID.
     *
     * @param person the person whose events we are to update
     * @param newID the new personID to update in each event
     * @throws DataAccessException if an error occurs
     */
    public void updatePersonIDs(Person person, String newID) throws DataAccessException {
        if (!person.getPersonID().equals(newID)) {
            String sql = "UPDATE Events" + " SET personID = ? WHERE personID = ?;";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, newID);
                stmt.setString(2, person.getPersonID());
                logger.finest(sql);

                stmt.executeUpdate();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, ex.getMessage(), ex);
                throw new DataAccessException("Error: An error occurred while updating events in the database");
            }
        }
    }

    /**
     * Collects a list of all the events in the Events table associated to a given personID.
     *
     * @param personID the personID we're searching for
     * @return a (potentially empty) List of Events
     * @throws DataAccessException if an error occurs
     */
    private List<Event> getAllFromPerson(String personID) throws DataAccessException {
        logger.entering("EventDAO", "getAllFromPerson");
        List<Event> events = new ArrayList<>();

        String sql = "SELECT * FROM Events WHERE personID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Event event = new Event(rs.getString("eventID"), rs.getString("associatedUsername"),
                        rs.getString("personID"), rs.getFloat("latitude"),
                        rs.getFloat("longitude"), rs.getString("country"),
                        rs.getString("city"), rs.getString("eventType"),
                        rs.getInt("year"));
                events.add(event);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DataAccessException("Error occurred when getting all events for person " + personID);
        }
        return events;
    }

    private Integer getBirthYear(String personID) throws DataAccessException {
        logger.entering("EventDAO", "getBirthYear");
        String sql = "SELECT * FROM Events WHERE eventType = ? AND personID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "birth");
            stmt.setString(2, personID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("year");
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DataAccessException("An error occurred while searching for pre-existing birth dates in events " +
                    "table, in order to generate marriage event.");
        }
        return null;
    }

    private String randomID(){
        return UUID.randomUUID().toString();
    }

    private Location randomLocation() {
        ArrayList<Location> locations = EventDAO.locations.getData();
        return locations.get(new Random().nextInt(locations.size()));
    }
    private int randomYear(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
