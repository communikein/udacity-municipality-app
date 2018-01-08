package it.communikein.udacity_municipality.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import it.communikein.udacity_municipality.data.ComunicappRepository;
import it.communikein.udacity_municipality.data.model.Poi;

public class PoisViewModel extends ViewModel {

    private static final String LOG_TAG = PoisViewModel.class.getSimpleName();


    private LiveData<List<Poi>> mData;

    @Inject
    public PoisViewModel(ComunicappRepository repository) {

        mData = repository.getObservableAllPois();
    }

    public LiveData<List<Poi>> getObservableAllPois() {
        return mData;
    }

}
