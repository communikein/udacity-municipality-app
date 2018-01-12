package it.communikein.municipalia.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import it.communikein.municipalia.data.ComunicappRepository;
import it.communikein.municipalia.data.model.Report;


public class ReportsViewModel extends ViewModel {

    private static final String LOG_TAG = ReportsViewModel.class.getSimpleName();

    private LiveData<List<Report>> mData;

    @Inject
    public ReportsViewModel(ComunicappRepository repository) {

        mData = repository.getObservableAllReports();
    }

    public LiveData<List<Report>> getObservableReports() {
        return mData;
    }

}
