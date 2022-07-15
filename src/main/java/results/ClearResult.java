package results;

/**
 * An object representation of an HTTP /clear response body.
 */
public class ClearResponse extends Result {
    /**
     * A message to provide to the client.
     */
    private String message;

    /**
     * Creates the object.
     */
    public ClearResponse() {
        // TODO implement this constructor
    }

    public boolean isSuccess() {
        return super.success;
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
