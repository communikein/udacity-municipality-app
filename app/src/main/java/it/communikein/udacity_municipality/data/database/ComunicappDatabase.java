package it.communikein.udacity_municipality.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import it.communikein.udacity_municipality.data.model.Event;
import it.communikein.udacity_municipality.data.model.News;

@Database(
        entities = {Event.class, News.class},
        version = 1,
        exportSchema = false)
@TypeConverters(Converters.class)
public abstract class ComunicappDatabase extends RoomDatabase {

    public static final String NAME = "comunicapp_db";

    public abstract EventDao eventsDao();
    public abstract NewsDao newsDao();

}
