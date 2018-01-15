package it.communikein.municipalia.data.model;

import android.support.annotation.NonNull;

import java.util.Date;
import java.util.Map;

public class News {

    public static final String ARG_ID = "id";
    private static final String ARG_TITLE = "title";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_IMAGE = "image";
    private static final String ARG_TIMESTAMP = "timestamp";

    @NonNull
    private String id;
    private String title;
    private String description;
    private String image;
    private long timestamp;


    public News(@NonNull String id, String title, String description, String image, long timestamp) {
        setId(id);
        setTitle(title);
        setDescription(description);
        setImage(image);
        setTimestamp(timestamp);
    }

    public News(String id, Map<String, Object> map) {
        setId(id);

        if (map.containsKey(ARG_TITLE))
            setTitle((String) map.get(ARG_TITLE));
        if (map.containsKey(ARG_DESCRIPTION))
            setDescription((String) map.get(ARG_DESCRIPTION));
        if (map.containsKey(ARG_IMAGE))
            setImage((String) map.get(ARG_IMAGE));
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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
                news.getImage().equals(this.getImage()) &&
                news.getTimestamp() == this.getTimestamp();
    }
}
