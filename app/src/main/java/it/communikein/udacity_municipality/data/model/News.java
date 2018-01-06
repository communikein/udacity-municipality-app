package it.communikein.udacity_municipality.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

@Entity(tableName = "news")
public class News {

    @Ignore private static final String ARG_DATABASE_ID = "arg-database-id";
    @Ignore private static final String ARG_WEB_ID = "arg-web-id";
    @Ignore private static final String ARG_TITLE = "arg_title";
    @Ignore private static final String ARG_DESCRIPTION = "arg_body";
    @Ignore private static final String ARG_TIMESTAMP = "arg_timestamp";

    @PrimaryKey
    private int databaseId;
    private int webId;
    private String title;
    private String description;
    private long timestamp;


    public News(int databaseId, int webId, String title, String description, long timestamp) {
        setDatabaseId(databaseId);
        setWebId(webId);
        setTitle(title);
        setDescription(description);
        setTimestamp(timestamp);
    }

    @Ignore
    public News(JSONObject json) throws JSONException {
        if (json.has(ARG_DATABASE_ID))
            setDatabaseId(json.getInt(ARG_DATABASE_ID));
        if (json.has(ARG_WEB_ID))
            setWebId(json.getInt(ARG_WEB_ID));
        if (json.has(ARG_TITLE))
            setTitle(json.getString(ARG_TITLE));
        if (json.has(ARG_DESCRIPTION))
            setDescription(json.getString(ARG_DESCRIPTION));
        if (json.has(ARG_TIMESTAMP))
            setTimestamp(json.getLong(ARG_TIMESTAMP));
    }



    public int getWebId() { return this.webId; }

    public void setWebId(int webId) {
        this.webId = webId;
    }

    public int getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
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


    @Ignore
    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();

        try {
            obj.put(ARG_DATABASE_ID, getDatabaseId());
            obj.put(ARG_WEB_ID, getWebId());
            obj.put(ARG_TITLE, getTitle());
            obj.put(ARG_DESCRIPTION, getDescription());
            obj.put(ARG_TIMESTAMP, getTimestamp());
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
        if (! (obj instanceof News)) return false;

        News news = (News) obj;
        return news.getWebId() == this.getWebId();
    }

    @Ignore
    public boolean displayEquals(Object obj) {
        if (! (obj instanceof News)) return false;

        News news = (News) obj;
        return news.getTitle().equals(this.getTitle()) &&
                news.getDescription().equals(this.getDescription()) &&
                news.getTimestamp() == this.getTimestamp();
    }
}
