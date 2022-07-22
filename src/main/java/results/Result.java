package results;

public class Result {
    /**
     * Whether the requested method attempt was successful.
     */
    boolean success;
    /**
     * A message to display if an error occurs.
     */
    String message;

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
