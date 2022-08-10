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
}
