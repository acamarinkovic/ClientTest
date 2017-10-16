package org.universaal.nativeandroid.lightclient;

import android.content.ContextWrapper;

import com.pixplicity.easyprefs.library.Prefs;

public class Application extends android.app.Application {

    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }

    public static Application getInstance() {
        return instance;
    }
}
