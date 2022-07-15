package results;

/**
 * An object representation of an HTTP /login response body.
 */
public class LoginResponse implements Response {

    /**
     * Whether or not the LoginRequest was successful.
     */
    private boolean success;

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
     * If the request failed, a description of the error.
     */
    private String message;

    /**
     * Creates the Response object. If the request was successful, the fields authtoken, username, and personID should
     * be set, with success=true. If the request was unsuccessful, the message field should be set, with success=false.
     */
    public LoginResponse() {
        // TODO implement this constructor
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
