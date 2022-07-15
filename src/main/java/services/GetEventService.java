package services;

import requests.GetEventRequest;

/**
 * Implements the /event/[eventID] Web API method.
 */
public class GetEventService implements Service {

    /**
     * Creates the object.
     */
    public GetEventService() {}

    /**
     * Returns the single Event object with the specified ID (if the event is associated with the current user). The
     * current user is determined by the provided authtoken.
     *
     * @param r the Request object containing the user's authtoken
     * @param eventID the event ID to search for in the database.
     */
    public void getEvent(GetEventRequest r, String eventID) {
       // TODO implement GetEventService().getEvent()
    }
}
