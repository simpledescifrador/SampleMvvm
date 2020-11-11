package com.evil.samplemvvm.ui.main;

import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.evil.samplemvvm.data.AppDataManager;
import com.evil.samplemvvm.ui.base.BaseViewModel;

public class MainViewModel extends BaseViewModel {

    private final MutableLiveData<String> mStringMutableLiveData = new MutableLiveData<>();

    public MainViewModel(AppDataManager appDataManager) {
        super(appDataManager);
        mStringMutableLiveData.postValue("Hello World!");
    }

    public void changeString(String text) {
        mIsLoading.postValue(true);
        new Handler(Looper.myLooper()).postDelayed(() -> {
            mStringMutableLiveData.setValue(text);
            mIsLoading.postValue(false);
        }, LOADING_DELAY);

    }

    public LiveData<String> getLiveSampleString() {
        return getAppDataManager().getSharedPreferenceManager().getSampleString();
    }

    public LiveData<String> getLiveString() {
        return mStringMutableLiveData;
    }


    @Override
    public LiveData<Boolean> loadingState() {
        return mIsLoading;
    }

    public void saveSampleString(String sampleString) {
        getAppDataManager().getSharedPreferenceManager().saveSampleString(sampleString);
    }

}
