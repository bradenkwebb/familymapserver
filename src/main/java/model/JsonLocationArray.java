package model;

import java.util.ArrayList;

/**
 * An object for representing a JSON array of Locations.
 */
public class JsonLocationArray {
    /**
     * The ArrayList<>() of Locations.
     */
    private ArrayList<Location> data;

    public ArrayList<Location> getData() {
        return data;
    }

    public void setData(ArrayList<Location> data) {
        this.data = data;
    }
}
