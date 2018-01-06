package it.communikein.udacity_municipality.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;

import org.json.JSONException;
import org.json.JSONObject;

@Entity(tableName = "events")
public class Event extends News {

    @Ignore private static final String ARG_DATE_START = "arg-date-start";
    @Ignore private static final String ARG_DATE_END = "arg-date-end";
    @Ignore private static final String ARG_LOCATION_LAT = "arg-location-lat";
    @Ignore private static final String ARG_LOCATION_LNG = "arg-location-lng";

    @ColumnInfo(name = "date_start") private long mDateStart;
    @ColumnInfo(name = "date_end") private long mDateEnd;
    @ColumnInfo(name = "location_lat") private long mLocationLat;
    @ColumnInfo(name = "location_lng") private long mLocationLng;

    public Event(int dbId, int webId, String title, String description, long timestamp, long dateStart,
                 long dateEnd, long lat, long lng) {
        super(dbId, webId, title, description, timestamp);

        setDateStart(dateStart);
        setDateEnd(dateEnd);
        setLocationLat(lat);
        setLocationLng(lng);
    }

    @Ignore
    public Event(JSONObject json) throws JSONException {
        super(json);

        if (json.has(ARG_DATE_START))
            setDateStart(json.getLong(ARG_DATE_START));
        if (json.has(ARG_DATE_END))
            setDateEnd(json.getLong(ARG_DATE_END));
        if (json.has(ARG_LOCATION_LAT))
            setLocationLat(json.getLong(ARG_LOCATION_LAT));
        if (json.has(ARG_LOCATION_LNG))
            setLocationLng(json.getLong(ARG_LOCATION_LNG));
    }


    public long getDateStart() {
        return mDateStart;
    }

    public void setDateStart(long dateStart) {
        this.mDateStart = dateStart;
    }

    public long getDateEnd() {
        return mDateEnd;
    }

    public void setDateEnd(long dateEnd) {
        this.mDateEnd = dateEnd;
    }

    public long getLocationLat() {
        return mLocationLat;
    }

    public void setLocationLat(long locationLat) {
        this.mLocationLat = locationLat;
    }

    public long getLocationLng() {
        return mLocationLng;
    }

    public void setLocationLng(long locationLng) {
        this.mLocationLng = locationLng;
    }


    @Ignore
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

    @Ignore
    @Override
    public String toString() {
        return toJSON().toString();
    }


    @Ignore
    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof Event)) return false;

        Event event = (Event) obj;
        return event.getWebId() == this.getWebId();
    }

    @Ignore
    public boolean displayEquals(Object obj) {
        if (! super.displayEquals(obj)) return false;

        Event event = (Event) obj;
        return event.getDateStart() == this.getDateStart() &&
                event.getDateEnd() == this.getDateEnd();
    }
}
