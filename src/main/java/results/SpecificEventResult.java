package results;

import model.Event;

/**
 * An object representation of an HTTP /event/[eventID] response body.
 */
public class SpecificEventResult extends Result {

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
     * Creates the object.
     */
    public SpecificEventResult() {}
    public SpecificEventResult(Event event) {
        this.associatedUsername = event.getAssociatedUsername();
        this.eventID = event.getEventID();
        this.eventType = event.getEventType();
        this.country = event.getCountry();
        this.city = event.getCity();
        this.latitude = event.getLatitude();
        this.longitude = event.getLongitude();
        this.personID = event.getPersonID();
        this.year = event.getYear();
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
}