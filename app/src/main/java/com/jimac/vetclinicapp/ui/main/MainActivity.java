package com.jimac.vetclinicapp.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jimac.vetclinicapp.App;
import com.jimac.vetclinicapp.R;
import com.jimac.vetclinicapp.data.AppDataManager;
import com.jimac.vetclinicapp.ui.base.BaseActivity;
import com.jimac.vetclinicapp.ui.main.fragments.appointments.AppointmentFragment;
import com.jimac.vetclinicapp.ui.main.fragments.home.HomeFragment;
import com.jimac.vetclinicapp.ui.main.fragments.pets.PetsFragment;
import com.jimac.vetclinicapp.utils.AppUtils;

public class MainActivity extends BaseActivity<MainViewModel> {

    private BottomNavigationView mBottomNavigationView;

    private DrawerLayout mDrawerLayout;

    private Toolbar mToolbarMain;

    private MainViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainv1);
        initViews();
        setUpObservers();
        registerListeners();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void startActivityForResult(final Intent intent, final int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    @NonNull
    @Override
    protected MainViewModel getViewModel() {
        AppDataManager appDataManager = ((App) getApplication()).getAppDataManager();
        mViewModel = new ViewModelProvider(this, new MainViewModel.ViewModelFactory(appDataManager))
                .get(MainViewModel.class);
        return mViewModel;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initViews() {
        setTitle("Home");
        //Bottom Nav
        mBottomNavigationView = findViewById(R.id.bottomNav_home_nav);

        //Toolbar
        mToolbarMain = findViewById(R.id.toolbar_main);

        //DrawerLayout
        mDrawerLayout = findViewById(R.id.drawerLayout_main);

        setSupportActionBar(mToolbarMain);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_baseline_menu_24));
    }

    @SuppressLint("NonConstantResourceId")
    private void registerListeners() {
        AppUtils.hideKeyboard(this);
        openFragment(HomeFragment.getInstance());
        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    openFragment(HomeFragment.getInstance());
                    setTitle("Home");
                    return true;
                case R.id.navigation_appointment:
                    openFragment(AppointmentFragment.getInstance());
                    setTitle("Appointments");
                    return true;
                case R.id.navigation_pets:
                    openFragment(PetsFragment.getInstance());
                    setTitle("Pets");
                    return true;
            }
            return false;
        });
    }

    private void setUpObservers() {
    }
}