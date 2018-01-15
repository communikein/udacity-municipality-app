package it.communikein.municipalia.data.model;

import java.util.Date;
import java.util.Map;

public class Event extends Poi {

    private static final String ARG_DATE_START = "date_start";
    private static final String ARG_DATE_END = "date_end";

    private long dateStart;
    private long dateEnd;

    public Event(String id, String title, String description, String image, long timestamp,
                 long dateStart, long dateEnd, double locationLat, double locationLng) {
        super(id, title, description, image, timestamp, locationLat, locationLng);

        setDateStart(dateStart);
        setDateEnd(dateEnd);
    }

    public Event(String id, Map<String, Object> map) {
        super(id, map);

        if (map.containsKey(ARG_DATE_START))
            setDateStart(((Date) map.get(ARG_DATE_START)).getTime());
        if (map.containsKey(ARG_DATE_END))
            setDateEnd(((Date) map.get(ARG_DATE_END)).getTime());
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
