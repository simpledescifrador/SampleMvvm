package com.evil.samplemvvm.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.evil.samplemvvm.R;

public abstract class BaseFragment<VM extends BaseViewModel> extends Fragment {

    protected VM viewModel;

    private ProgressDialog mProgressDialog;

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        this.viewModel = getViewModel();

        mProgressDialog = new ProgressDialog(getContext());
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
