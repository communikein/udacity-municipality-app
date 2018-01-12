package it.communikein.municipalia.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import it.communikein.municipalia.ui.HomeFragment;
import it.communikein.municipalia.ui.list.pois.PoisFragment;
import it.communikein.municipalia.ui.list.news.NewsFragment;
import it.communikein.municipalia.ui.list.reports.ReportsFragment;

@Module
public abstract class FragmentsBuilderModule {

    @ContributesAndroidInjector
    abstract HomeFragment contributeHomeFragment();

    @ContributesAndroidInjector
    abstract NewsFragment contributeNewsFragment();

    @ContributesAndroidInjector
    abstract PoisFragment contributePoisFragment();

    @ContributesAndroidInjector
    abstract ReportsFragment contributeReportsFragment();

}
