package com.evil.samplemvvm;

import android.app.Application;
import com.evil.samplemvvm.data.AppDataManager;
import com.evil.samplemvvm.data.preferences.SharedPreferenceManager;

public class App extends Application {

    private AppDataManager mAppDataManager;

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(this);
        mAppDataManager = new AppDataManager(sharedPreferenceManager);
    }

    public AppDataManager getAppDataManager() {
        return mAppDataManager;
    }
}
