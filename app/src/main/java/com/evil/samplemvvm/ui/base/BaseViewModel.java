package com.evil.samplemvvm.ui.base;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.evil.samplemvvm.data.AppDataManager;

public abstract class BaseViewModel extends ViewModel {

    protected static final int LOADING_DELAY = 2000;

    protected final MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>(false);

    private final AppDataManager mAppDataManager;

    public BaseViewModel(AppDataManager appDataManager) {
        mAppDataManager = appDataManager;
    }

    public AppDataManager getAppDataManager() {
        return mAppDataManager;
    }

    public abstract LiveData<Boolean> loadingState();
}
