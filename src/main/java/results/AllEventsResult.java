package results;

import model.Event;

import java.util.List;

/**
 * An object representation of an HTTP /event response body.
 */
public class AllEventsResult extends Result {
    /**
     * A JSON array containing all of the event objects from the user's family tree.
     */
    private List<Event> data;

    /**
     * Creates the object.
     */
    public AllEventsResult() {}
    public AllEventsResult(List<Event> events) {
        this.data = events;
    }

    public List<Event> getData() {
        return data;
    }

    public void setData(List<Event> data) {
        this.data = data;
    }
}
