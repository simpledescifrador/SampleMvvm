package com.jimac.vetclinicapp;

import android.app.Application;
import com.jimac.vetclinicapp.data.AppDataManager;
import com.jimac.vetclinicapp.data.network.NetworkRepository;
import com.jimac.vetclinicapp.data.preferences.SharedPreferenceManager;

public class App extends Application {

    private AppDataManager mAppDataManager;

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(this);
        NetworkRepository networkRepository = new NetworkRepository();
        mAppDataManager = new AppDataManager(sharedPreferenceManager, networkRepository);
    }

    public AppDataManager getAppDataManager() {
        return mAppDataManager;
    }
}
