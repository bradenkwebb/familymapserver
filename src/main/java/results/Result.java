package results;

public class Result {
    /**
     * Whether or not the requested method attempt was successful.
     */
    boolean isSuccess;
    /**
     * A message to display if an error occurs.
     */
    String message;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
