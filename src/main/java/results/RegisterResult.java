package results;

/**
 * An object representation of an HTTP /register response body.
 */
public class RegisterResponse implements Response {

    /**
     * The authtoken generated for this user's login session.
     */
    private String authtoken;

    /**
     * The identifier for the user who called the Register request.
     */
    private String username;

    /**
     * The identifier for the user's corresponding person object.
     */
    private String personID;

    /**
     * Whether or not the register attempt was successful.
     */
    private boolean success;

    /**
     * If the request failed, an error message explaining why.
     */
    private String message;

    /**
     * Creates the Response object. If the request was successful, the fields authtoken, username, and personID should
     * be set, with success=true. If the request was unsuccessful, the message field should be set, with success=false.
     */
    public RegisterResponse() {
        // TODO implement this constructor?
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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
