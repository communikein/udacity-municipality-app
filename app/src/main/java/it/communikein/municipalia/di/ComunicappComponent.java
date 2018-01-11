package it.communikein.municipalia.di;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;
import it.communikein.municipalia.ComunicApp;
import it.communikein.municipalia.di.module.MainActivityModule;
import it.communikein.municipalia.di.module.ComunicappModule;
import it.communikein.municipalia.di.module.NewsDetailActivityModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        ComunicappModule.class,
        MainActivityModule.class,
        NewsDetailActivityModule.class})
public interface ComunicappComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        ComunicappComponent build();
    }

    void inject(ComunicApp app);

}
