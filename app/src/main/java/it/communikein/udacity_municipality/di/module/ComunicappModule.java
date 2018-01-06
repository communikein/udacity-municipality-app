package it.communikein.udacity_municipality.di.module;

import android.app.Application;

import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.communikein.udacity_municipality.data.ComunicappRepository;

@Module
public class ComunicappModule {

    @Singleton @Provides
    ComunicappRepository provideRepository(FirebaseFirestore firestore, Application application) {
        return new ComunicappRepository(firestore, application);
    }

    @Singleton @Provides
    FirebaseFirestore provideDatabase() {
        return FirebaseFirestore.getInstance();
    }

}
