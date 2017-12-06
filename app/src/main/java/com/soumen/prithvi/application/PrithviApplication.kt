package com.soumen.prithvi.application

import android.app.Application
import com.soumen.prithvi.extras.AppCommonValues
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Created by Soumen on 05-12-2017.
 */
class PrithviApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(applicationContext)
        val realmConfiguration = RealmConfiguration.Builder()
                .name(AppCommonValues.CONFIGNAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(realmConfiguration)
    }
}