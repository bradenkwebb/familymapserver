package results;

/**
 * An object representation of an HTTP /login response body.
 */
public class LoginResult extends Result {

    /**
     * The generated authtoken.
     */
    private String authtoken;

    /**
     * The username for which the authtoken is valid.
     */
    private String username;

    /**
     * The identifier for the Person object that corresponds to the user.
     */
    private String personID;

    /**
     * Creates the Response object. If the request was successful, the fields authtoken, username, and personID should
     * be set, with success=true. If the request was unsuccessful, the message field should be set, with success=false.
     */
    public LoginResult() {
        // TODO implement this constructor
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
