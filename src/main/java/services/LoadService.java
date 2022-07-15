package services;

import requests.LoadRequest;

/**
 * Implements the /load service.
 */
public class LoadService implements Service {

    /**
     * Creates the object.
     */
    public LoadService() {}

    /**
     * Clears all data from the database (just like /clear API) and loads the user, person, and event data from the
     * request body into the database.
     *
     * @param r the LoadRequest object containing the relevant input data.
     */
    public void load(LoadRequest r) {
        //TODO implement LoadService().load()
    }
}
