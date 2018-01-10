package it.communikein.udacity_municipality.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import it.communikein.udacity_municipality.ui.list.pois.PoisFragment;
import it.communikein.udacity_municipality.ui.list.news.NewsFragment;

@Module
public abstract class FragmentsBuilderModule {

    @ContributesAndroidInjector
    abstract NewsFragment contributeNewsFragment();

    @ContributesAndroidInjector
    abstract PoisFragment contributePoisFragment();

}
