package com.evil.samplemvvm.ui.base;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.evil.samplemvvm.R;

public abstract class BaseActivity<VM extends BaseViewModel> extends AppCompatActivity {

    protected VM viewModel;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.viewModel = viewModel == null ? getViewModel() : viewModel;

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.show();

        if (mProgressDialog.getWindow() != null) {
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        mProgressDialog.setContentView(R.layout.dialog_progress);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setCanceledOnTouchOutside(false);


    }

    public void hideLoading() {
        mProgressDialog.dismiss();
    }

    public void showLoading() {
        mProgressDialog.show();
    }

    /**
     * @return view model instance
     */
    @NonNull
    protected abstract VM getViewModel();

}
