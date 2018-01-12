package it.communikein.municipalia.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import it.communikein.municipalia.data.model.Event;
import it.communikein.municipalia.data.model.News;
import it.communikein.municipalia.data.model.Poi;
import it.communikein.municipalia.data.model.Report;

@Singleton
public class ComunicappRepository {

    private static final String LOG_TAG = ComunicappRepository.class.getSimpleName();

    private FirebaseFirestore firestore;

    private static final String NEWS_COLLECTION = "news";
    private static final String EVENTS_COLLECTION = "events";
    private static final String POIS_COLLECTION = "pois";
    private static final String REPORTS_COLLECTION = "reports";

    private MutableLiveData<List<News>> mNews;
    private MutableLiveData<List<Event>> mEvents;
    private MutableLiveData<List<Poi>> mPois;
    private MutableLiveData<List<Report>> mReports;


    @Inject
    public ComunicappRepository(FirebaseFirestore firestore) {
        this.firestore = firestore;

        mNews = new MutableLiveData<>();
        mEvents = new MutableLiveData<>();
        mPois = new MutableLiveData<>();
        mReports = new MutableLiveData<>();

        Query newsQuery = firestore.collection(NEWS_COLLECTION)
                .orderBy("timestamp", Query.Direction.DESCENDING);
        newsQuery.addSnapshotListener((value, e) -> {
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

        CollectionReference reportsReference = firestore.collection(REPORTS_COLLECTION);
        reportsReference.addSnapshotListener((value, e) -> {
            if (e != null) {
                Log.w(LOG_TAG, "Listen to reports failed.", e);
                return;
            }

            ArrayList<Report> result = new ArrayList<>();
            for (DocumentSnapshot doc : value) {
                Map<String, Object> data = doc.getData();
                String id = doc.getId();

                result.add(new Report(id, data));
            }
            mReports.postValue(result);
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

    public LiveData<List<Report>> getObservableAllReports() {
        return mReports;
    }


    public LiveData<News> getObservableNews(String id) {
        MutableLiveData<News> newsDetail = new MutableLiveData<>();

        DocumentReference reference = firestore.collection(NEWS_COLLECTION).document(id);
        reference.addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) {
                Log.w(LOG_TAG, "Listen to news failed.", e);
                return;
            }

            newsDetail.postValue(new News(id, documentSnapshot.getData()));
        });

        return newsDetail;
    }

    public LiveData<Poi> getObservablePoi(String id) {
        MutableLiveData<Poi> poiDetails = new MutableLiveData<>();

        DocumentReference reference = firestore.collection(POIS_COLLECTION).document(id);
        reference.addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) {
                Log.w(LOG_TAG, "Listen to poi failed.", e);
                return;
            }

            poiDetails.postValue(new Poi(id, documentSnapshot.getData()));
        });

        return poiDetails;
    }

    public LiveData<Report> getObservableReport(String id) {
        MutableLiveData<Report> reportDetails = new MutableLiveData<>();

        DocumentReference reference = firestore.collection(REPORTS_COLLECTION).document(id);
        reference.addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) {
                Log.w(LOG_TAG, "Listen to report failed.", e);
                return;
            }

            reportDetails.postValue(new Report(id, documentSnapshot.getData()));
        });

        return reportDetails;
    }

    public LiveData<Event> getObservableEvent(String id) {
        MutableLiveData<Event> eventDetails = new MutableLiveData<>();

        DocumentReference reference = firestore.collection(EVENTS_COLLECTION).document(id);
        reference.addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) {
                Log.w(LOG_TAG, "Listen to event failed.", e);
                return;
            }

            eventDetails.postValue(new Event(id, documentSnapshot.getData()));
        });

        return eventDetails;
    }
}
