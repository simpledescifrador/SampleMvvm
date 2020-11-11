package com.evil.samplemvvm.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.evil.samplemvvm.data.AppDataManager;

public class MainViewModelFactory implements ViewModelProvider.Factory {

    private final AppDataManager mAppDataManager;

    public MainViewModelFactory(final AppDataManager appDataManager) {
        mAppDataManager = appDataManager;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull final Class<T> modelClass) {
        return (T) new MainViewModel(mAppDataManager);
    }
}
