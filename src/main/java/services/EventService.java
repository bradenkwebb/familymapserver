package services;

import dao.DataAccessException;
import dao.Database;
import dao.EventDAO;
import model.Event;
import results.AllEventsResult;
import results.Result;
import results.SpecificEventResult;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implements the /event Web API method.
 */
public class EventService implements Service {
    public static final Logger logger = Logger.getLogger("AllEventService");

    private final int EVENT_ID_INDEX = 2;

    /**
     * Creates the object.
     */
    public EventService() {}

    /**
     * Searches through the database collecting all events associated to the provided username. Returns all of these
     * events in the form of an AllEventsResult() object if the urlPath doesn't contain a specific eventID or a
     * SpecificEventResult() object if the urlPath does.
     *
     * @param username the associated username that should be attached to any obtained events
     * @param urlPath the HTTP Request URI, which should specify an eventID if only searching for one event
     * @return either an AllEventsResult() object or a SpecificEventResult() object
     */
    public Result getResult(String username, String urlPath) {
        logger.entering("EventService", "getResult");
        Result result = new Result();
        result.setSuccess(false);
        Database db = new Database();
        Connection conn = null;

        try (Connection c = db.getConnection()) {
            conn = c;
            EventDAO eDao = new EventDAO(conn);
            List<Event> events = eDao.getAllFromUser(username);

            if (isSpecificEvent(urlPath)) {
                Event event = getSpecificEvent(getIDFromPath(urlPath), events);
                if (event != null) {
                    result = new SpecificEventResult(event);
                    result.setSuccess(true);
                } else {
                    result.setSuccess(false);
                    result.setMessage("Error: Event not found (at least not for this user)");
                }
            } else {
                result = new AllEventsResult(events);
                result.setSuccess(true);
            }

            db.closeConnection(true);
        } catch (DataAccessException | SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            db.closeConnection(false);
            result.setMessage("Error: " + ex.getMessage());
        }
        return result;
    }

    /**
     * Determines whether or not the given urlPath is requesting all events, or just one event
     *
     * @param urlPath the HTTP Request URI to parse
     * @return true if the number of components delimited by "/" is the number expected for a specific event
     * request, otherwise false
     */
    private boolean isSpecificEvent(String urlPath) {
        String[] parts = urlPath.split("/");
        return parts.length == EVENT_ID_INDEX + 1;
    }

    /**
     * Obtains the requested eventID from the provided urlPath
     *
     * @param urlPath the HTTP Request URI to parse
     * @return the eventID
     */
    private String getIDFromPath(String urlPath) {
        String[] parts = urlPath.split("/");
        return parts[EVENT_ID_INDEX];
    }

    /**
     * Searches through the list of events and returns the requested event if found; otherwise,
     * returns null.
     *
     * @param eventID the ID of the requested event
     * @param events the list of events to search through
     * @return the specific event if found, otherwise null.
     */
    private Event getSpecificEvent(String eventID, List<Event> events) {
        for (Event event : events) {
            if (eventID.equals(event.getEventID())) {
                return event;
            }
        }
        return null;
    }
}
