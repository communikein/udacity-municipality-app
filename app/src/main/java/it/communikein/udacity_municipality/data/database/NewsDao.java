package it.communikein.udacity_municipality.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import it.communikein.udacity_municipality.data.model.News;

@Dao
public interface NewsDao {

    @Insert
    void addNews(News news);

    @Insert
    void bulkInsertNews(List<News> entries);

    @Query("DELETE FROM news")
    void deleteAllNews();

    @Query("DELETE FROM news WHERE webId = :webId")
    void deleteSingleNews(int webId);

    @Query("SELECT * FROM news")
    LiveData<List<News>> getObservableAllNews();

    @Query("SELECT * FROM news")
    List<News> getAllNews();

    @Query("SELECT * FROM news WHERE webId = :webId")
    News getSingleNews(int webId);

    @Query("SELECT * FROM news WHERE webId = :webId")
    LiveData<News> getObservableSingleNews(int webId);

    @Query("SELECT COUNT(webId) FROM news")
    int getNewsSize();

    @Update
    int updateNews(News news);
}
