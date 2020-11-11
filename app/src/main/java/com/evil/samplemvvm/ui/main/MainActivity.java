package com.evil.samplemvvm.ui.main;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.evil.samplemvvm.App;
import com.evil.samplemvvm.R;
import com.evil.samplemvvm.data.AppDataManager;
import com.evil.samplemvvm.ui.base.BaseActivity;

public class MainActivity extends BaseActivity<MainViewModel> {

    private class LoadingObserver implements Observer<Boolean> {

        @Override
        public void onChanged(final Boolean loadingState) {
            if (loadingState) {
                showLoading();
            } else {
                hideLoading();
            }
        }
    }

    private TextView mTextView;

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(v -> viewModel.saveSampleString("I clicked"));
        mTextView = findViewById(R.id.tv_hellow);
        viewModel.loadingState().observe(this, new LoadingObserver());
        viewModel.getLiveString().observe(this, liveString -> mTextView.setText(liveString));

        viewModel.getLiveSampleString().observe(this, s -> {
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        });
    }


    @NonNull
    @Override
    protected MainViewModel getViewModel() {
        AppDataManager appDataManager = ((App) getApplication()).getAppDataManager();
        viewModel = new ViewModelProvider(this,
                new MainViewModelFactory(appDataManager))
                .get(MainViewModel.class);
        return viewModel;
    }
}