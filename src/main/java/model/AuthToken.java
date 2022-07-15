package model;

import java.util.Objects;

/**
 * An object for representing an authentication token.
 */
public class AuthToken {

    /**
     * The randomized and unique string for verifying a user's identity.
     */
    private String authtoken;

    /**
     * The username corresponding to the user for whom this token verifies identity.
     */
    private String username;

    /**
     * Creates a new authtoken and associates it with a user.
     *
     * @param authtoken the randomized and unique string for verifying a user's identity.
     * @param username he username corresponding to the user for whom this token verifies identity.
     */
    public AuthToken(String authtoken, String username) {
        this.authtoken = authtoken;
        this.username = username;
    }

    public String getAuthtoken() { return authtoken; }

    public void setAuthtoken(String authtoken) { this.authtoken = authtoken; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    /**
     * Determines whether or not the authtoken is equal to the object passed in to it.
     *
     * @param object the object to be compared.
     * @return whether or not the authtoken is equal to the object passed in to it.
     */
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        AuthToken authToken = (AuthToken) object;
        return authtoken.equals(authToken.authtoken) && username.equals(authToken.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authtoken, username);
    }
}