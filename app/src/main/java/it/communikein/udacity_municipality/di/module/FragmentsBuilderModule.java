package it.communikein.udacity_municipality.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import it.communikein.udacity_municipality.ui.list.NewsEventsFragment;

@Module
public abstract class FragmentsBuilderModule {

    @ContributesAndroidInjector
    abstract NewsEventsFragment contributeNewsEventsFragment();

}
