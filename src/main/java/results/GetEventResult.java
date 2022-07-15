package results;

/**
 * An object representation of an HTTP /event/[eventID] response body.
 */
public class GetEventResponse implements Response {

    /**
     * Whether or not the server successfully implemented the request.
     */
    private boolean success;

    /**
     * The username associated with the event.
     */
    private String associatedUsername;

    /**
     * The unique identifier for the event.
     */
    private String eventID;

    /**
     * The identifier for the person who experienced the event.
     */
    private String personID;

    /**
     * The latitude coordinate where the event took place.
     */
    private float latitude;

    /**
     * The longitude coordinate where the event took place.
     */
    private float longitude;

    /**
     * The country where the event took place.
     */
    private String country;

    /**
     * The city where the event took place.
     */
    private String city;

    /**
     * The type of event (e.g. birth, death, etc.)
     */
    private String eventType;

    /**
     * The year in which the event took place.
     */
    private int year;

    /**
     * If the server failed to execute the request, a message to provide to the client.
     */
    private String message;

    /**
     * Creates the object.
     */
    public GetEventResponse() {
        //TODO implement this constructor
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
