package com.jimac.vetclinicapp.ui.main.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.jimac.vetclinicapp.App;
import com.jimac.vetclinicapp.R;
import com.jimac.vetclinicapp.data.AppDataManager;
import com.jimac.vetclinicapp.data.models.Appointment;
import com.jimac.vetclinicapp.data.models.Pet;
import com.jimac.vetclinicapp.ui.base.BaseFragment;
import com.jimac.vetclinicapp.ui.main.fragments.adapters.ClientAppointmentsAdapter;
import com.jimac.vetclinicapp.ui.main.fragments.adapters.HomePetsAdapter;
import com.jimac.vetclinicapp.ui.main.fragments.home.HomeViewModel.ViewModelFactory;
import com.jimac.vetclinicapp.ui.main.fragments.home.HomeViewModel.ViewState.LoadPets;
import com.jimac.vetclinicapp.ui.main.fragments.home.HomeViewModel.ViewState.LoadRecentAppointments;
import com.jimac.vetclinicapp.ui.main.fragments.home.HomeViewModel.ViewState.Loading;
import com.jimac.vetclinicapp.ui.pet.registration.PetRegistration;
import com.jimac.vetclinicapp.ui.schedule_appointments.new_appointment.NewAppointmentActivity;
import com.jimac.vetclinicapp.utils.Constants;
import java.util.List;

public class HomeFragment extends BaseFragment<HomeViewModel> {

    private static HomeFragment INSTANCE = null;

    private Button mButtonNewAppointment, mButtonAddPet;

    private RecyclerView mRecyclerViewRecentAppointments, mRecyclerViewPets;

    private TextView mTextViewNoAppointments;

    private HomeViewModel mViewModel;

    public static HomeFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HomeFragment();
        }
        return INSTANCE;
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);
        setUpObservers();
        registerListeners();

        return view;
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case Constants.REQUEST_CODE_ADD_PET:
                    mViewModel.getClientPets();
                    break;
                case Constants.REQUEST_CODE_ADD_APPOINTMENT:
                    mViewModel.getClientAppointments();
                    break;
            }
            mViewModel.getClientAppointments();
        }
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
        mButtonAddPet = view.findViewById(R.id.button_home_addPet);

        //RecyclerView
        mRecyclerViewRecentAppointments = view.findViewById(R.id.recylerView_home_recentAppointments);
        mRecyclerViewPets = view.findViewById(R.id.recyclerView_home_pets);

        //TextView
        mTextViewNoAppointments = view.findViewById(R.id.textView_home_noAppointment);
    }

    private void registerListeners() {
        mButtonNewAppointment.setOnClickListener(
                v -> startActivityForResult(new Intent(getContext(), NewAppointmentActivity.class),
                        Constants.REQUEST_CODE_ADD_APPOINTMENT));
        mButtonAddPet.setOnClickListener(v -> startActivityForResult(new Intent(getContext(), PetRegistration.class),
                Constants.REQUEST_CODE_ADD_PET));
    }

    private void setUpObservers() {
        mViewModel.getClientPets();
        mViewModel.getClientAppointments();
        mViewModel.getViewState().observe(getViewLifecycleOwner(), viewState -> {
            if (viewState instanceof Loading) {
                showLoading();
            } else {
                hideLoading();
            }

            if (viewState instanceof LoadRecentAppointments) {
                List<Appointment> appointmentList = ((LoadRecentAppointments) viewState).getAppointments();

                ClientAppointmentsAdapter appointmentsAdapter = new ClientAppointmentsAdapter(appointmentList);

                mRecyclerViewRecentAppointments.setLayoutManager(new LinearLayoutManager(getContext()));
                mRecyclerViewRecentAppointments.setHasFixedSize(true);
                mRecyclerViewRecentAppointments.setAdapter(appointmentsAdapter);
                mRecyclerViewRecentAppointments.setNestedScrollingEnabled(false);

                if (appointmentList.size() > 0) {
                    mRecyclerViewRecentAppointments.setVisibility(View.VISIBLE);
                    mTextViewNoAppointments.setVisibility(View.GONE);
                } else {
                    mRecyclerViewRecentAppointments.setVisibility(View.INVISIBLE);
                    mTextViewNoAppointments.setVisibility(View.VISIBLE);
                }
            } else if (viewState instanceof LoadPets) {
                List<Pet> appointmentList = ((LoadPets) viewState).getPets();

                HomePetsAdapter petsAdapter = new HomePetsAdapter(appointmentList);

                mRecyclerViewPets.setLayoutManager(
                        new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                mRecyclerViewPets.setHasFixedSize(true);
                mRecyclerViewPets.setNestedScrollingEnabled(false);
                mRecyclerViewPets.setAdapter(petsAdapter);
            }

        });

        mViewModel.getActionState().observe(getViewLifecycleOwner(), viewState -> {

        });
    }
}