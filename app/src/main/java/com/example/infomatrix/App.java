package com.example.infomatrix;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App extends Application {

    private static final String DB_NAME = "db.realm";
    private static final Integer SCHEME_VERSION = 2;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration mRealmConfiguration = new RealmConfiguration.Builder()
                .name(DB_NAME)
                .schemaVersion(SCHEME_VERSION)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.getInstance(mRealmConfiguration);
        Realm.setDefaultConfiguration(mRealmConfiguration);
    }

}
