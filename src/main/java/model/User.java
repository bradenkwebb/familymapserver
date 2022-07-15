package model;

/**
 * An object for representing a user.
 */
public class User{
    /**
     * The unique ID by which the user will be known.
     */
    private String username;
    /**
     * The user's password.
     */
    private String password;
    /**
     * The user's email address.
     */
    private String email;
    /**
     * The user's first name.
     */
    private String firstName;
    /**
     * The user's surname.
     */
    private String lastName;
    /**
     * The user's gender (either 'f' or 'm').
     */
    private String gender;
    /**
     * The identifier for the Person object to which this user corresponds.
     */
    private String personID;

    /**
     * Creates a user.
     *
     * @param username the unique ID by which the user will be known.
     * @param password the hidden string the user can use to login.
     * @param email the user's email address.
     * @param firstName the user's first name.
     * @param lastName the user's last name.
     * @param gender either 'f' or 'm'.
     * @param personID an identifier for the corresponding Person object.
     */
    public User(String username, String password, String email, String firstName,
                String lastName, String gender, String personID) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password;}

    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getGender() { return gender; }

    public void setGender(String gender) { this.gender = gender; }

    public String getPersonID() { return personID; }

    public void setPersonID(String personID) { this.personID = personID; }
}