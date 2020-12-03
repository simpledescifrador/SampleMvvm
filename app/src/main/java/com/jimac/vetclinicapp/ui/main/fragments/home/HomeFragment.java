package com.jimac.vetclinicapp.ui.main.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import com.jimac.vetclinicapp.App;
import com.jimac.vetclinicapp.R;
import com.jimac.vetclinicapp.data.AppDataManager;
import com.jimac.vetclinicapp.ui.base.BaseFragment;
import com.jimac.vetclinicapp.ui.main.fragments.home.HomeViewModel.ViewModelFactory;
import com.jimac.vetclinicapp.ui.main.fragments.home.HomeViewModel.ViewState.Loading;
import com.jimac.vetclinicapp.ui.schedule_appointments.new_appointment.NewAppointmentActivity;

public class HomeFragment extends BaseFragment<HomeViewModel> {

    private Button mButtonNewAppointment;

    private HomeViewModel mViewModel;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setUpObservers();
        registerListeners();
    }

    @NonNull
    @Override
    protected HomeViewModel getViewModel() {
        AppDataManager appDataManager = ((App) getActivity().getApplication()).getAppDataManager();
        mViewModel = new ViewModelProvider(requireActivity(), new ViewModelFactory(appDataManager))
                .get(HomeViewModel.class);
        return mViewModel;
    }

    private void initViews(final View view) {
        //Buttons
        mButtonNewAppointment = view.findViewById(R.id.button_home_newAppointment);
    }

    private void registerListeners() {
        mButtonNewAppointment.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), NewAppointmentActivity.class));
        });
    }

    private void setUpObservers() {
        mViewModel.getViewState().observe(getViewLifecycleOwner(), viewState -> {
            if (viewState instanceof Loading) {
                showLoading();
            } else {
                hideLoading();
            }


        });

        mViewModel.getActionState().observe(getViewLifecycleOwner(), viewState -> {

        });
    }
}