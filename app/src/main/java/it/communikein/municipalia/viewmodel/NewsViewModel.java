package it.communikein.municipalia.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import it.communikein.municipalia.data.ComunicappRepository;
import it.communikein.municipalia.data.model.News;

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
