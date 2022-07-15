package services;

import requests.FillRequest;

/**
 * Implements the /fill/[username]/{generations} service.
 */
public class FillService implements Service {
    /**
     * Creates the object.
     */
    public FillService() {}

    /**
     * Populates the server's database with generated data for the specified username. If there is any data in the
     * database already associated with the given username, it is deleted.
     *
     * @param r the FillRequest object containing information from the request body.
     * @param username the associated username; must already be registered with the server.
     * @param generations the number of generations of ancestors to generate for the user. If not provided, defaults
     *                    to 4.
     */
    public void fill(FillRequest r, String username, int generations) {
        // TODO implement FillService.fill()
    }

    /**
     * Calls {@link FillService#fill(FillRequest, String, int)}, with {@code generations = 4}.
     * @see #fill(FillRequest, String, int)
     */
    public void fill(FillRequest r, String username) {
        this.fill(r, username, 4);
    }
}
