package it.communikein.udacity_municipality.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import it.communikein.udacity_municipality.data.ComunicappRepository;
import it.communikein.udacity_municipality.data.model.News;

public class NewsViewModel extends ViewModel {

    private static final String LOG_TAG = NewsViewModel.class.getSimpleName();

    private LiveData<List<News>> mData;

    @Inject
    public NewsViewModel(ComunicappRepository repository) {

        mData = repository.getObservableAllNews();
    }

    public LiveData<List<News>> getObservableNews() {
        return mData;
    }

}
