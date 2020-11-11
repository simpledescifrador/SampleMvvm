package com.evil.samplemvvm.data;

import com.evil.samplemvvm.data.preferences.SharedPreferenceManager;

public class AppDataManager {

    private final SharedPreferenceManager mSharedPreferenceManager;

    public AppDataManager(SharedPreferenceManager sharedPreferenceManager) {
        mSharedPreferenceManager = sharedPreferenceManager;
    }

    public SharedPreferenceManager getSharedPreferenceManager() {
        return mSharedPreferenceManager;
    }
}
