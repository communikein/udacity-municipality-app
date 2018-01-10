package it.communikein.udacity_municipality.di.module;


import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.communikein.udacity_municipality.data.ComunicappRepository;

@Module
public class ComunicappModule {

    @Singleton @Provides
    ComunicappRepository provideRepository(FirebaseFirestore firestore) {
        return new ComunicappRepository(firestore);
    }

    @Singleton @Provides
    FirebaseFirestore provideDatabase() {
        return FirebaseFirestore.getInstance();
    }

}
