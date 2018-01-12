package it.communikein.municipalia.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import it.communikein.municipalia.data.ComunicappRepository;
import it.communikein.municipalia.data.model.News;

public class NewsDetailViewModel extends ViewModel {

    private static final String LOG_TAG = NewsDetailViewModel.class.getSimpleName();

    private ComunicappRepository mRepository;

    private LiveData<News> mData;

    @Inject
    public NewsDetailViewModel(ComunicappRepository repository) {
        mRepository = repository;

        mData = null;
    }

    public void setNewsId(String id) {
        mData = mRepository.getObservableNews(id);
    }

    public LiveData<News> getObservableNews() {
        return mData;
    }

}
