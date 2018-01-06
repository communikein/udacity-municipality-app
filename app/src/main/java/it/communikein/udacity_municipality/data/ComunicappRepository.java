package it.communikein.udacity_municipality.data;

import android.app.Application;
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

import it.communikein.udacity_municipality.data.model.News;

@Singleton
public class ComunicappRepository {

    private static final String LOG_TAG = ComunicappRepository.class.getSimpleName();

    private FirebaseFirestore firestore;
    private CollectionReference newsReference;

    private MutableLiveData<List<News>> mNews;


    @Inject
    public ComunicappRepository(FirebaseFirestore firestore, Application application) {
        this.firestore = firestore;

        mNews = new MutableLiveData<>();


        newsReference = firestore.collection("news");
        newsReference.addSnapshotListener((value, e) -> {
            if (e != null) {
                Log.w(LOG_TAG, "Listen failed.", e);
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
    }


    public LiveData<List<News>> getObservableAllNews() {
        return mNews;
    }
}
