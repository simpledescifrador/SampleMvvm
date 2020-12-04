package com.jimac.vetclinicapp.ui.main.fragments.pets;

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
import com.jimac.vetclinicapp.ui.main.fragments.pets.PetsViewModel.ViewModelFactory;


public class PetsFragment extends BaseFragment<PetsViewModel> {


    private static PetsFragment INSTANCE = null;

    private PetsViewModel mViewModel;


    public static PetsFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PetsFragment();
        }
        return INSTANCE;
    }

    public PetsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pets, container, false);
    }

    @NonNull
    @Override
    protected PetsViewModel getViewModel() {
        AppDataManager appDataManager = ((App) getActivity().getApplication()).getAppDataManager();
        mViewModel = new ViewModelProvider(this, new ViewModelFactory(appDataManager))
                .get(PetsViewModel.class);
        return mViewModel;
    }
}