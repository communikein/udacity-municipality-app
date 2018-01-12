package it.communikein.municipalia.data.model;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Map;

public class Event extends News {

    private static final String ARG_DATE_START = "date_start";
    private static final String ARG_DATE_END = "date_end";
    private static final String ARG_LOCATION_LAT = "location_lat";
    private static final String ARG_LOCATION_LNG = "location_lng";

    private long dateStart;
    private long dateEnd;
    private double locationLat;
    private double locationLng;

    public Event(String id, String title, String description, String image, long timestamp,
                 long dateStart, long dateEnd, double locationLat, double locationLng) {
        super(id, title, description, image, timestamp);

        setDateStart(dateStart);
        setDateEnd(dateEnd);
        setLocationLat(locationLat);
        setLocationLng(locationLng);
    }

    public Event(JSONObject json) throws JSONException {
        super(json);

        if (json.has(ARG_DATE_START))
            setDateStart(json.getLong(ARG_DATE_START));
        if (json.has(ARG_DATE_END))
            setDateEnd(json.getLong(ARG_DATE_END));
        if (json.has(ARG_LOCATION_LAT))
            setLocationLat(json.getDouble(ARG_LOCATION_LAT));
        if (json.has(ARG_LOCATION_LNG))
            setLocationLng(json.getDouble(ARG_LOCATION_LNG));
    }

    public Event(String id, Map<String, Object> map) {
        super(id, map);

        if (map.containsKey(ARG_DATE_START))
            setDateStart(((Date) map.get(ARG_DATE_START)).getTime());
        if (map.containsKey(ARG_DATE_END))
            setDateEnd(((Date) map.get(ARG_DATE_END)).getTime());
        if (map.containsKey(ARG_LOCATION_LAT))
            setLocationLat((double) map.get(ARG_LOCATION_LAT));
        if (map.containsKey(ARG_LOCATION_LNG))
            setLocationLng((double) map.get(ARG_LOCATION_LNG));
    }


    public long getDateStart() {
        return dateStart;
    }

    public void setDateStart(long dateStart) {
        this.dateStart = dateStart;
    }

    public long getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(long dateEnd) {
        this.dateEnd = dateEnd;
    }

    public double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(double locationLat) {
        this.locationLat = locationLat;
    }

    public double getLocationLng() {
        return locationLng;
    }

    public void setLocationLng(double locationLng) {
        this.locationLng = locationLng;
    }

    public LatLng getCoords() {
        return new LatLng(getLocationLat(), getLocationLng());
    }

    public void setCoords(LatLng coords) {
        setLocationLat(coords.latitude);
        setLocationLng(coords.longitude);
    }


    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();

        try {
            obj.put(ARG_DATE_START, getDateStart());
            obj.put(ARG_DATE_END, getDateEnd());
            obj.put(ARG_LOCATION_LAT, getLocationLat());
            obj.put(ARG_LOCATION_LNG, getLocationLng());
        } catch (JSONException e){
            obj = new JSONObject();
        }

        return obj;
    }

    @Override
    public String toString() {
        return toJSON().toString();
    }


    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof Event)) return false;

        Event event = (Event) obj;
        return event.getId().equals(this.getId());
    }

    public boolean displayEquals(Object obj) {
        if (! super.displayEquals(obj)) return false;

        Event event = (Event) obj;
        return event.getDateStart() == this.getDateStart() &&
                event.getDateEnd() == this.getDateEnd();
    }
}
