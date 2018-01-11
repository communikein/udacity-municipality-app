package it.communikein.udacity_municipality.viewmodel.factory;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import it.communikein.udacity_municipality.data.ComunicappRepository;
import it.communikein.udacity_municipality.viewmodel.HomeViewModel;

public class HomeViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final ComunicappRepository mRepository;

    @Inject
    public HomeViewModelFactory(ComunicappRepository repository) {
        this.mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new HomeViewModel(mRepository);
    }

}
