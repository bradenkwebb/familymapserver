package model;

import java.util.Objects;

/**
 * An object for representing an event.
 */
public class Event {

    /**
     * A unique identifier for the event.
     */
    private String eventID;

    /**
     * The username for the user whose family tree contains this event.
     */
    private String associatedUsername;

    /**
     * The identifier for the person with whom this event is associated.
     */
    private String personID;

    /**
     * The latitude coordinate for the location of the event.
     */
    private Float latitude;

    /**
     * The longitude coordinate for the location of the event.
     */
    private Float longitude;

    /**
     * The country in which the event took place.
     */
    private String country;

    /**
     * The city in which the event took place.
     */
    private String city;

    /**
     * The type of event.
     */
    private String eventType;

    /**
     * The year in which the event took place.
     */
    private Integer year;

    /**
     * Creates an event from the given parameters.
     *
     * @param eventID the unique identifier for the event
     * @param username the username for the user whose family tree contains this event.
     * @param personID the identifier for the person with whom this event is associated.
     * @param latitude the latitude coordinate at which the event took place.
     * @param longitude the longitude coordinate at which the event took place.
     * @param country the country in which the event took place.
     * @param city the city in which the event took place.
     * @param eventType the type of the event.
     * @param year the year in which the event took place.
     */
    public Event(String eventID, String username, String personID, Float latitude, Float longitude,
                 String country, String city, String eventType, Integer year) {
        this.eventID = eventID;
        this.associatedUsername = username;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public String getEventID() { return eventID; }

    public void setEventID(String eventID) { this.eventID = eventID; }

    public String getAssociatedUsername() { return associatedUsername; }

    public void setAssociatedUsername(String associatedUsername) { this.associatedUsername = associatedUsername; }

    public String getPersonID() { return personID; }

    public void setPersonID(String personID) { this.personID = personID; }

    public Float getLatitude() { return latitude; }

    public void setLatitude(Float latitude) { this.latitude = latitude; }

    public Float getLongitude() { return longitude; }

    public void setLongitude(Float longitude) { this.longitude = longitude; }

    public String getCountry() { return country; }

    public void setCountry(String country) { this.country = country; }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

    public String getEventType() { return eventType; }

    public void setEventType(String eventType) { this.eventType = eventType; }

    public Integer getYear() { return year; }

    public void setYear(Integer year) { this.year = year; }

    /**
     * Determines whether the event is equal to the passed object.
     *
     * @param o the object to compare
     * @return whether the event is equal to the given object
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(eventID, event.eventID) && Objects.equals(associatedUsername, event.associatedUsername) && Objects.equals(personID, event.personID) && Objects.equals(latitude, event.latitude) && Objects.equals(longitude, event.longitude) && Objects.equals(country, event.country) && Objects.equals(city, event.city) && Objects.equals(eventType, event.eventType) && Objects.equals(year, event.year);
    }

}
