package services;

import requests.RegisterRequest;

/**
 * Implements the /user/register Web API method.
 */
public class RegisterService implements Service {

    /**
     * Instantiates a RegisterService object.
     */
    public RegisterService() {}

    /**
     * Creates a new user account, generates 4 generations of ancestor data for the new user, logs the user in,
     * and returns an authtoken
     * @param r the RegisterRequest object to process
     */
    public void register(RegisterRequest r) {
        // TODO implement RegisterService().register()
    }
}
