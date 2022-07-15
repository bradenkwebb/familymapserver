package requests;

/**
 * An object representation of an HTTP /register request body.
 */
public class RegisterRequest implements Request {

    /**
     * The provided username.
     */
    private String username;

    /**
     * The provided password.
     */
    private String password;

    /**
     * The provided email address.
     */
    private String email;

    /**
     * The provided first name.
     */
    private String firstName;

    /**
     * The provided surname.
     */
    private String lastName;

    /**
     * The provided gender, either 'f' or 'm'.
     */
    private char gender;

    /**
     * Creates the Request object (and potentially deserializes a provided JSON string into the object?).
     */
    public RegisterRequest() {
        // TODO implement RegisterRequest constructor
        // I think the deserialization should be done by the handler?
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }
}
