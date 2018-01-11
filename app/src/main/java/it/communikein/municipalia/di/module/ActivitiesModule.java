package it.communikein.municipalia.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import it.communikein.municipalia.ui.MainActivity;

@Module
public abstract class ActivitiesModule {

    @ContributesAndroidInjector(modules = FragmentsBuilderModule.class)
    abstract MainActivity contributeMainActivity();

}
