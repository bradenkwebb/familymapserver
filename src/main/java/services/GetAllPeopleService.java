package services;

import requests.GetAllPeopleRequest;

/**
 * Implements the /person Web API method.
 */
public class GetAllPeopleService implements Service {

    /**
     * Creates the object.
     */
    public GetAllPeopleService() {}

    /**
     * Returns ALL family members of the current user. The current user is determined by the provided authtoken.
     * @param r the GetALLPeopleRequest object containing the user's authtoken.
     */
    public void getAllFamily(GetAllPeopleRequest r) {
        // TODO implement GetAllPeopleService().getAllFamily()
    }
}
