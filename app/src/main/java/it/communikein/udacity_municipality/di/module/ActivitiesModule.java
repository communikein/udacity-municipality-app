package it.communikein.udacity_municipality.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import it.communikein.udacity_municipality.ui.MainActivity;

@Module
public abstract class ActivitiesModule {

    @ContributesAndroidInjector(modules = FragmentsBuilderModule.class)
    abstract MainActivity contributeMainActivity();

}
