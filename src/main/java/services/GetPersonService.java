package services;

import requests.GetPersonRequest;

/**
 * Implements the /person/[personID] service
 */
public class GetPersonService implements Service {

    /**
     * Creates the object.
     */
    public GetPersonService() {}

    /**
     * Returns the single Person object with the specified ID (if the person is associated with the current user). The
     * current user is determined by the provided authtoken
     *
     * @param r the GetPersonRequest object containing the auth token to identify the user.
     * @param personID the person ID to search for in the database.
     */
    public void getPerson(GetPersonRequest r, String personID) {
        // TODO implement GetPersonService().getPerson()
    }
}
