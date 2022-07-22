package dao;

import com.google.gson.Gson;
import model.JsonStringArray;
import model.Person;
import model.User;

import java.io.*;
import java.sql.*;
import java.time.Year;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A data access object for accessing, querying, and editing people in the SQL database.
 */
public class PersonDAO {
    public static final Logger logger = Logger.getLogger("PersonDAO");
    private static final int MAX_AGE = 120;
    private static final JsonStringArray fnames;
    private static final JsonStringArray mnames;
    private static final JsonStringArray snames;

    static {
        try {
            fnames = parse(new File("json/fnames.json"));
            mnames = parse(new File("json/mnames.json"));
            snames = parse(new File("json/snames.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parses a provided JSON file into JsonStringArray objects; i.e., objects containing
     * only one data member, which is an ArrayList<String>()
     *
     * @param file the JSON file to parse
     * @return the serialized array
     * @throws IOException if an error occurs
     */
    private static JsonStringArray parse(File file) throws IOException {
        try(FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            Gson gson = new Gson();
            return gson.fromJson(bufferedReader, JsonStringArray.class);
        }
    }

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
        logger.entering("PersonDAO", "insert");
        String sql = "INSERT INTO Persons (personID, associatedUsername, firstName, lastName, gender, " +
                     "motherID, fatherID, spouseID)" + " VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getAssociatedUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getMotherID());
            stmt.setString(7, person.getFatherID());
            stmt.setString(8, person.getSpouseID());

            logger.finer(stmt.toString());
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
                                    rs.getString("gender"), rs.getString("fatherID"),
                                    rs.getString("motherID"), rs.getString("spouseID"));
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

    /**
     * Obtains a list of all of the people who are associated with a given user.
     *
     * @param username the identifier for the user in question.
     * @return a list of all of the people in the user's family tree.
     * @throws DataAccessException if an error occurs
     */
    public List<Person> getAllFamily(String username) throws DataAccessException {
        String sql = "SELECT * FROM Persons WHERE associatedUsername = ?;";
        ResultSet rs;
        List<Person> family = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while (rs.next()) {
                family.add(new Person(rs.getString("personID"), rs.getString("associatedUsername"),
                        rs.getString("firstName"), rs.getString("lastName"),
                        rs.getString("gender"), rs.getString("fatherID"),
                        rs.getString("motherID"), rs.getString("spouseID")));
            }
            return family;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DataAccessException("Error occurred while trying to get all of " +
                    username + "'s family from persons table");
        }
    }

    /**
     * Deletes all rows of the Persons table with the given associatedUsername.
     *
     * @param username the associatedUsername for which rows should be deleted
     * @throws DataAccessException if an error occurs
     */
    public void clearUser(String username) throws DataAccessException {
        String sql = "DELETE FROM Persons WHERE associatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DataAccessException("Error occurred while clearing " + username + "'s data from Persons table");
        }
    }

    /*
    * Each person, excluding the user, must have at least three events with the following types: birth, marriage, and death.
    *       They may have additional events as well, but we will only be checking for these three.
    * The user’s person object needs to have at least a birth event, and may have additional events, but
    *       we will only be checking for the birth event.
    * Parents must be born at least 13 years before their children.
    * Parents must be at least 13 years old when they are married.
    * Parents must not die before their child is born.
    * Women must not give birth when older than 50 years old.
    * Birth events must be the first event for a person chronologically.
    * Death events must be the last event for a person chronologically.
    * Nobody must die at an age older than 120 years old.
    * Each person in a married couple has their own marriage event. Each event will have a unique event ID,
    *       but both marriage events must have matching years and locations.
    * Event locations may be randomly selected, or you may try to make them more realistic (e.g., many people live
    *       their lives in a relatively small geographical area).

    * */

    /**
     * Given a user that should already be in the database, generates and inserts persons up to the provided number
     * of generations with their respective birth, marriage, and death dates and adds all of that information
     * to the database.
     *
     * @param user the user in whose family tree the information should be generated
     * @param numGenerations the number of generations to generate (with 0 corresponding to only 1 person, the user)
     * @return the Person object corresponding to the user
     * @throws DataAccessException if an error occurs
     */
    public Person generate(User user, int numGenerations) throws DataAccessException {
        int currentYear = Year.now().getValue();
        int birthYear = randomYear(currentYear - MAX_AGE, currentYear);
        // Randomly generate all family members and a person object for the user
        Person person = generate(user.getUsername(), user.getGender(), numGenerations,
                birthYear);

        // Update the user's Person to the correct values
        person.setGender(user.getGender());
        person.setFirstName(user.getFirstName());
        person.setLastName(user.getLastName());

        // Push those updates to the database
        updatePersonByID(person, user.getPersonID());
        return person;
    }

    /**
     * Recursively enerates a person and ancestors for that person up to the provided number of generations, along
     * with their corresponding birth, marriage, and death dates.
     *
     * @param username the username for the user to whom this family tree belongs
     * @param gender the gender of the user
     * @param numGenerations the number of generations of people to generate
     * @param personBirthYear the year the current person was born
     * @return the current Person object
     * @throws DataAccessException if an error occurs when inserting a person or event to the database
     */
    public Person generate(String username, String gender, int numGenerations, int personBirthYear) throws DataAccessException {
        logger.entering("PersonDAO", "generate");
        Person mother;
        Person father;
        String motherID = null;
        String fatherID = null;
        EventDAO eventDAO = new EventDAO(conn);

        if (numGenerations >= 1) {
            // Mothers should be between ages 13 and 50
            mother = generate(username, "f", numGenerations - 1,
                              randomYear(personBirthYear - 50, personBirthYear - 13));
            motherID = mother.getPersonID();
            // Fathers should be between ages 13 and 120
            father = generate(username, "m", numGenerations - 1,
                              randomYear(personBirthYear - MAX_AGE, personBirthYear - 13));
            fatherID = father.getPersonID();
            mother.setSpouseID(fatherID);
            father.setSpouseID(motherID);
            updatePersonByID(mother);
            updatePersonByID(father);
            eventDAO.generateMarriage(username, fatherID, motherID);
            eventDAO.generateDeath(username, fatherID);
            eventDAO.generateDeath(username, motherID);
        }

        String firstName = randomFirstName(gender);
        String lastName = randomLastName(fatherID);
        String personID = firstName + "_" + lastName + UUID.randomUUID().toString().substring(0, 6);

        Person person = new Person(personID, username, firstName, lastName, gender, fatherID, motherID, null);
        insert(person);

        eventDAO.generateBirth(username, personID, personBirthYear);

        return person;
    }

    /**
     * Counts the total number of people in the database.
     * @return the total number of people in the database
     * @throws DataAccessException if an error occurs
     */
    public int totalNumPeople() throws DataAccessException {
        String sql = "SELECT COUNT(*) AS total FROM Persons;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            } else {
                throw new DataAccessException("Number of rows returned was null");
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error occurred while counting the rows of Persons");
        }
    }

    /**
     * Counts the total number of people associated with a given username
     * @param username the username of the person's whose family we're counting
     * @return the total number of people in the family tree
     * @throws DataAccessException if an error occurs
     */
    public int famSize(String username) throws DataAccessException {
        logger.entering("PersonDAO", "famSize");
        String sql = "SELECT COUNT(*) AS total FROM Persons WHERE associatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            logger.finer(stmt.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            } else {
                throw new DataAccessException("Number of rows returned was null");
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error occurred while counting the rows of Persons");
        }
    }

    /**
     * Selects a random first name, stereotypically matching the provided gender.
     *
     * @param gender either "m" or "f", to correspondingly return either a masculine or feminine name
     * @return the selected first name
     */
    private String randomFirstName(String gender) {
        ArrayList<String> names;
        if (gender.equals("f")) {
            names = fnames.getData();
        } else {
            names = mnames.getData();
        }
        return names.get(new Random().nextInt(names.size()));
    }

    /**
     * Selects a random surname from the statically generated lsit
     *
     * @param fatherID I don't actually use this parameter, but in theory it could be used to ensure that
     *                 last names are passed down patriarchally
     * @return the selected surname
     */
    private String randomLastName(String fatherID) {
        ArrayList<String> surnames = snames.getData();
        return surnames.get(new Random().nextInt(surnames.size()));
    }

    /**
     * Updates the attributes of a person based on their personID. Thus, this function cannot
     * be used to update someone's personID.
     *
     * @param person the new version of the person to update
     * @throws DataAccessException if an error occurs
     */
    private void updatePersonByID(Person person) throws DataAccessException {
        updatePersonByID(person, person.getPersonID());
    }

    private void updatePersonByID(Person person, String newID) throws DataAccessException {
        String sql = "UPDATE Persons " + "SET personID = ?, associatedUsername = ?, firstName = ?, lastName = ?, " +
                "gender = ?, motherID = ?, fatherID = ?, spouseID = ? " +
                "WHERE personID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newID);
            stmt.setString(2, person.getAssociatedUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getMotherID());
            stmt.setString(7, person.getFatherID());
            stmt.setString(8, person.getSpouseID());
            stmt.setString(9, person.getPersonID());
            logger.finest(sql);

            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            logger.log(Level.WARNING, ex.getMessage(), ex);
            throw new DataAccessException("Error: An error occurred while updating a person in database");
        }
        new EventDAO(conn).updatePersonIDs(person, newID);
    }

    /**
     * Generates a random integer in the range of [min, max).
     * @param min the lower bound (inclusive)
     * @param max the upper bound (exclusive)
     * @return the year
     */
    private int randomYear(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}