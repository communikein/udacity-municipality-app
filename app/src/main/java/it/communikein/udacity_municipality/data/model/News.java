package it.communikein.udacity_municipality.data.model;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Map;

public class News {

    private static final String ARG_ID = "id";
    private static final String ARG_TITLE = "title";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_TIMESTAMP = "timestamp";

    @NonNull
    private String id;
    private String title;
    private String description;
    private long timestamp;


    public News(@NonNull String id, String title, String description, long timestamp) {
        setId(id);
        setTitle(title);
        setDescription(description);
        setTimestamp(timestamp);
    }

    public News(JSONObject json) throws JSONException {
        if (json.has(ARG_ID))
            setId(json.getString(ARG_ID));
        if (json.has(ARG_TITLE))
            setTitle(json.getString(ARG_TITLE));
        if (json.has(ARG_DESCRIPTION))
            setDescription(json.getString(ARG_DESCRIPTION));
        if (json.has(ARG_TIMESTAMP))
            setTimestamp(json.getLong(ARG_TIMESTAMP));
    }

    public News(String id, Map<String, Object> map) {
        setId(id);

        if (map.containsKey(ARG_TITLE))
            setTitle((String) map.get(ARG_TITLE));
        if (map.containsKey(ARG_DESCRIPTION))
            setDescription((String) map.get(ARG_DESCRIPTION));
        if (map.containsKey(ARG_TIMESTAMP))
            setTimestamp(((Date) map.get(ARG_TIMESTAMP)).getTime());
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();

        try {
            obj.put(ARG_ID, getId());
            obj.put(ARG_TITLE, getTitle());
            obj.put(ARG_DESCRIPTION, getDescription());
            obj.put(ARG_TIMESTAMP, getTimestamp());
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
        if (! (obj instanceof News)) return false;

        News news = (News) obj;
        return news.getId().equals(this.getId());
    }

    public boolean displayEquals(Object obj) {
        if (! (obj instanceof News)) return false;

        News news = (News) obj;
        return news.getTitle().equals(this.getTitle()) &&
                news.getDescription().equals(this.getDescription()) &&
                news.getTimestamp() == this.getTimestamp();
    }
}
