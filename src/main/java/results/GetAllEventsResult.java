package results;

import model.Event;

import java.util.List;

/**
 * An object representation of an HTTP /event response body.
 */
public class GetAllEventsResult extends Result {
    /**
     * A JSON array containing all of the event objects from the user's family tree.
     */
    private List<Event> data;

    /**
     * If the server was unsuccessful, the message to provide the client.
     */
    private String message;

    /**
     * Creates the object.
     */
    public GetAllEventsResponse() {
        //TODO implement this constructor
    }

    public List<Event> getData() {
        return data;
    }

    public void setData(List<Event> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
