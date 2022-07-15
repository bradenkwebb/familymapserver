package requests;

/**
 * An object representation of an HTTP /login request body.
 */
public class LoginRequest implements Request{

    /**
     * The provided username.
     */
    private String username;

    /**
     * The provided password.
     */
    private String password;

    /**
     * Creates the Request object (and potentially deserializes a provided JSON string into the object?).
     */
    public LoginRequest() {
        // TODO implement this constructor
    }

}
