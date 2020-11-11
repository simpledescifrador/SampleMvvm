package com.evil.samplemvvm.data.preferences;

import static com.evil.samplemvvm.data.preferences.PreferenceConstant.SAMPLE_STRING;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class SharedPreferenceManager extends PreferenceHelper {

    public static final String PREFS = "LOCAL_PREFS";

    private final SharedPreferences mSharedPreferences;

    public SharedPreferenceManager(Context context) {
        mSharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }


    @Override
    public LiveData<String> getSampleString() {
        return new MutableLiveData<>(mSharedPreferences.getString(SAMPLE_STRING, ""));
    }


    @Override
    public void saveSampleString(final String str) {
        mSharedPreferences.edit().putString(SAMPLE_STRING, str).apply();
    }
}
