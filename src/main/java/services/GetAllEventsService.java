package services;

import requests.GetAllEventsRequest;

/**
 * Implements the /event Web API method.
 */
public class GetAllEventsService implements Service {

    /**
     * Creates the object.
     */
    public GetAllEventsService() {}

    /**
     * Returns ALL events for ALL family members of the current user. The current user is determined from the provided
     * authtoken.
     *
     * @param r the Requests object containing the user's authtoken.
     */
    public void getEvents(GetAllEventsRequest r){
        //TODO implment GetAllEventsService().getEvents()
    }
}
