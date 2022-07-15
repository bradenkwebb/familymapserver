package results;

/**
 * An object representation of an HTTP /load response body.
 */
public class LoadResponse implements Response {
    /**
     * Whether or not the server successfully loaded the data.
     */
    private boolean success;

    /**
     * A message to provide to the client.
     */
    private String message;

    public LoadResponse() {
        // TODO implement this constructor
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
