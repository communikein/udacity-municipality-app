package it.communikein.udacity_municipality.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import it.communikein.udacity_municipality.data.model.Event;

@Dao
public interface EventDao {

    @Insert
    void addEvent(Event event);

    @Insert
    void bulkInsertEvents(List<Event> entries);

    @Query("DELETE FROM events")
    void deleteAllEvents();

    @Query("DELETE FROM events WHERE web_id = :webId")
    void deleteSingleEvent(int webId);

    @Query("SELECT * FROM events")
    LiveData<List<Event>> getObservableAllEvents();

    @Query("SELECT * FROM events")
    List<Event> getAllEvents();

    @Query("SELECT * FROM events WHERE web_id = :webId")
    Event getSingleEvent(int webId);

    @Query("SELECT * FROM events WHERE web_id = :webId")
    LiveData<Event> getObservableSingleEvent(int webId);

    @Query("SELECT COUNT(web_id) FROM events")
    int getEventsSize();

    @Update
    int updateEvent(Event event);
    
}
