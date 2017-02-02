package com.student.fahrtenbuchapp.dataSync;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import java.security.SecureRandom;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App extends Application {

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        //byte[] key = new byte[64];
        //new SecureRandom().nextBytes(key);

        RealmConfiguration config = new RealmConfiguration.Builder()
              //.encryptionKey(key)
              .build();
        Realm.setDefaultConfiguration(config);

        //RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
        //        .deleteRealmIfMigrationNeeded()
        //        .build();
        //Realm.deleteRealm(realmConfiguration);

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build()
        );
    }
}
