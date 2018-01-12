package it.communikein.municipalia.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import it.communikein.municipalia.data.ComunicappRepository;
import it.communikein.municipalia.data.model.News;
import it.communikein.municipalia.data.model.Poi;
import it.communikein.municipalia.data.model.Report;

public class HomeViewModel extends ViewModel {

    private static final String LOG_TAG = HomeViewModel.class.getSimpleName();

    private LiveData<List<News>> mNews;
    private LiveData<List<Poi>> mPois;
    private LiveData<List<Report>> mReports;

    @Inject
    public HomeViewModel(ComunicappRepository repository) {
        mNews = repository.getObservableAllNews();
        mPois = repository.getObservableAllPois();
        mReports = repository.getObservableAllReports();
    }

    public LiveData<List<News>> getObservableNews() {
        return mNews;
    }

    public LiveData<List<Poi>> getObservablePois() {
        return mPois;
    }

    public LiveData<List<Report>> getObservableReports() {
        return mReports;
    }

}
