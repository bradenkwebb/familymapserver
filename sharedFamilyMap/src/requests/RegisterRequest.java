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
    private String gender;

    /**
     * Creates the Request object
     */
    public RegisterRequest() { }

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}