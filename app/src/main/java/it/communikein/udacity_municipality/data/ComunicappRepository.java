package it.communikein.udacity_municipality.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import it.communikein.udacity_municipality.data.model.Event;
import it.communikein.udacity_municipality.data.model.News;
import it.communikein.udacity_municipality.data.model.Poi;

@Singleton
public class ComunicappRepository {

    private static final String LOG_TAG = ComunicappRepository.class.getSimpleName();

    private FirebaseFirestore firestore;

    private static final String NEWS_COLLECTION = "news";
    private static final String EVENTS_COLLECTION = "events";
    private static final String POIS_COLLECTION = "pois";

    private MutableLiveData<List<News>> mNews;
    private MutableLiveData<List<Event>> mEvents;
    private MutableLiveData<List<Poi>> mPois;


    @Inject
    public ComunicappRepository(FirebaseFirestore firestore) {
        this.firestore = firestore;

        mNews = new MutableLiveData<>();
        mEvents = new MutableLiveData<>();
        mPois = new MutableLiveData<>();

        CollectionReference newsReference = firestore.collection(NEWS_COLLECTION);
        newsReference.addSnapshotListener((value, e) -> {
            if (e != null) {
                Log.w(LOG_TAG, "Listen to news failed.", e);
                return;
            }

            ArrayList<News> result = new ArrayList<>();
            for (DocumentSnapshot doc : value) {
                Map<String, Object> data = doc.getData();
                String id = doc.getId();

                result.add(new News(id, data));
            }
            mNews.postValue(result);
        });

        CollectionReference eventsReference = firestore.collection(EVENTS_COLLECTION);
        eventsReference.addSnapshotListener((value, e) -> {
            if (e != null) {
                Log.w(LOG_TAG, "Listen to events failed.", e);
                return;
            }

            ArrayList<Event> result = new ArrayList<>();
            for (DocumentSnapshot doc : value) {
                Map<String, Object> data = doc.getData();
                String id = doc.getId();

                result.add(new Event(id, data));
            }
            mEvents.postValue(result);
        });

        CollectionReference poisReference = firestore.collection(POIS_COLLECTION);
        poisReference.addSnapshotListener((value, e) -> {
            if (e != null) {
                Log.w(LOG_TAG, "Listen to pois failed.", e);
                return;
            }

            ArrayList<Poi> result = new ArrayList<>();
            for (DocumentSnapshot doc : value) {
                Map<String, Object> data = doc.getData();
                String id = doc.getId();

                result.add(new Poi(id, data));
            }
            mPois.postValue(result);
        });
    }


    public LiveData<List<News>> getObservableAllNews() {
        return mNews;
    }

    public LiveData<List<Event>> getObservableAllEvents() {
        return mEvents;
    }

    public LiveData<List<Poi>> getObservableAllPois() {
        return mPois;
    }
}
