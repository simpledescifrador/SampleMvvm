package com.jimac.vetclinicapp.ui.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jimac.vetclinicapp.App;
import com.jimac.vetclinicapp.R;
import com.jimac.vetclinicapp.data.AppDataManager;
import com.jimac.vetclinicapp.ui.base.BaseActivity;
import com.jimac.vetclinicapp.ui.main.fragments.appointments.AppointmentFragment;
import com.jimac.vetclinicapp.ui.main.fragments.home.HomeFragment;
import com.jimac.vetclinicapp.ui.main.fragments.pets.PetsFragment;

public class MainActivity extends BaseActivity<MainViewModel> {

    private BottomNavigationView mBottomNavigationView;

    private FrameLayout mFrameLayoutContainer;

    private MainViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setUpObservers();
        registerListeners();
    }

    @NonNull
    @Override
    protected MainViewModel getViewModel() {
        AppDataManager appDataManager = ((App) getApplication()).getAppDataManager();
        mViewModel = new ViewModelProvider(this, new MainViewModel.ViewModelFactory(appDataManager))
                .get(MainViewModel.class);
        return mViewModel;
    }

    private void initViews() {
        setTitle("Home");
        //Bottom Nav
        mBottomNavigationView = findViewById(R.id.bottomNav_home_nav);

        //Frame Layout
        mFrameLayoutContainer = findViewById(R.id.frameLayout_home_container);
    }

    @SuppressLint("NonConstantResourceId")
    private void registerListeners() {
        openFragment(HomeFragment.newInstance());
        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    openFragment(HomeFragment.newInstance());
                    setTitle("Home");
                    return true;
                case R.id.navigation_appointment:
                    openFragment(AppointmentFragment.newInstance("", ""));
                    setTitle("Appointments");
                    return true;
                case R.id.navigation_pets:
                    openFragment(PetsFragment.newInstance("", ""));
                    setTitle("Pets");
                    return true;
            }
            return false;
        });
    }

    private void setUpObservers() {
    }
}