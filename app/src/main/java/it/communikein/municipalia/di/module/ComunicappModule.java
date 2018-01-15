package it.communikein.municipalia.di.module;


import android.app.Application;

import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.communikein.municipalia.data.ComunicappRepository;
import it.communikein.municipalia.data.login.LoginFirebase;
import it.communikein.municipalia.data.model.User;

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

    @Singleton @Provides
    LoginFirebase provideLogin(Application application, User user) {
        return new LoginFirebase(application, user);
    }

    @Singleton @Provides
    User provideUser() {
        return new User();
    }

}
