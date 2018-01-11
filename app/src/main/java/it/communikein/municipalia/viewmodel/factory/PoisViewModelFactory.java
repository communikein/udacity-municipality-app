package it.communikein.municipalia.viewmodel.factory;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import it.communikein.municipalia.data.ComunicappRepository;
import it.communikein.municipalia.viewmodel.PoisViewModel;

public class PoisViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final ComunicappRepository mRepository;

    @Inject
    public PoisViewModelFactory(ComunicappRepository repository) {
        this.mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new PoisViewModel(mRepository);
    }

}
