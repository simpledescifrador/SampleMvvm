package com.evil.samplemvvm.data.preferences;

import androidx.lifecycle.LiveData;

public abstract class PreferenceHelper {

    public abstract LiveData<String> getSampleString();

    public abstract void saveSampleString(String str);
}
