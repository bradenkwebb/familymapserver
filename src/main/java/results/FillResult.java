package responses;

/**
 * An object representation of an HTTP /fill/[username]/{generations} response body.
 */
public class FillResponse implements Response {
    /**
     * Whether or not the server successfully performed the request.
     */
    private boolean success;

    /**
     * A message to provide to the client.
     */
    private String message;

    /**
     * Creates the object.
     */
    public FillResponse() {
        //TODO implement this constructor?
    }

    @Override
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
