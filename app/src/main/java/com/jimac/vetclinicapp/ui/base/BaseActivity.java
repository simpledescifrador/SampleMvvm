package com.jimac.vetclinicapp.ui.base;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.jimac.vetclinicapp.R;

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
        mProgressDialog.dismiss();

    }

    public void hideLoading() {
        mProgressDialog.dismiss();
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout_home_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void showLoading(String message) {
        TextView loadingText = mProgressDialog.findViewById(R.id.textView_dialog_loading);
        loadingText.setText(message);
        mProgressDialog.show();
    }

    public void showLoading() {
        mProgressDialog.show();
    }

    public void showToast(String message, int duration) {
        Toast.makeText(this, message, duration).show();
    }

    /**
     * @return view model instance
     */
    @NonNull
    protected abstract VM getViewModel();

}
