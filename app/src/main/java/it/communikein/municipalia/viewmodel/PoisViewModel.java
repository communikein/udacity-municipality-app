package it.communikein.municipalia.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import it.communikein.municipalia.data.ComunicappRepository;
import it.communikein.municipalia.data.model.Poi;

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
