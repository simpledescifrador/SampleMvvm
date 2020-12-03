package com.jimac.vetclinicapp.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.jimac.vetclinicapp.R;

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
        mProgressDialog.dismiss();
    }

    public void hideLoading() {
        mProgressDialog.dismiss();
    }

    public void showLoading(String message) {
        TextView loadingText = mProgressDialog.findViewById(R.id.textView_dialog_loading);
        loadingText.setText(message);
        mProgressDialog.show();
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
