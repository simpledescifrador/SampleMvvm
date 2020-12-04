package com.jimac.vetclinicapp.ui.main.fragments.appointments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import com.jimac.vetclinicapp.App;
import com.jimac.vetclinicapp.R;
import com.jimac.vetclinicapp.data.AppDataManager;
import com.jimac.vetclinicapp.ui.base.BaseFragment;
import com.jimac.vetclinicapp.ui.main.fragments.appointments.AppointmentViewModel.ViewModelFactory;

public class AppointmentFragment extends BaseFragment<AppointmentViewModel> {


    private static AppointmentFragment INSTANCE = null;

    private AppointmentViewModel mViewModel;

    public static AppointmentFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AppointmentFragment();
        }
        return INSTANCE;
    }

    public AppointmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_appointment, container, false);
    }

    @NonNull
    @Override
    protected AppointmentViewModel getViewModel() {
        AppDataManager appDataManager = ((App) getActivity().getApplication()).getAppDataManager();
        mViewModel = new ViewModelProvider(this, new ViewModelFactory(appDataManager))
                .get(AppointmentViewModel.class);
        return mViewModel;
    }
}