package it.communikein.udacity_municipality.data.database;

import android.arch.persistence.room.TypeConverter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;

/**
 * {@link TypeConverter} for long to {@link Date}
 * <p>
 * This stores the date as a long in the database, but returns it as a {@link Date}
 */
class Converters {

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }


    @TypeConverter
    public static ArrayList<String> fromString(String value) {
        try {
            JSONArray array = new JSONArray(value);
            ArrayList<String> result = new ArrayList<>();

            for (int i = 0; i < array.length(); i++)
                result.add(array.getString(i));

            return result;
        } catch (JSONException e) {
            return null;
        }
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<String> list) {
        JSONArray json = new JSONArray();

        for (String item : list)
            json.put(item);

        return json.toString();
    }

}
