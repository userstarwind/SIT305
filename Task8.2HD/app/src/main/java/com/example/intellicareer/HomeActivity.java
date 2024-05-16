package com.example.intellicareer;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import com.example.intellicareer.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.intellicareer.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.navView.setItemActiveIndicatorColor(getColorStateList(R.color.primary));
        setSupportActionBar(binding.toolbar);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_explore, R.id.navigation_dashboard)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        NavOptions navOptionsHome = new NavOptions.Builder()
                .setPopUpTo(R.id.navigation_home, true)
                .build();

        NavOptions navOptionsExplore = new NavOptions.Builder()
                .setPopUpTo(R.id.navigation_explore, true)
                .build();

        NavOptions navOptionsDashboard = new NavOptions.Builder()
                .setPopUpTo(R.id.navigation_dashboard, true)
                .build();

        binding.navView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    navController.navigate(R.id.navigation_home, null, navOptionsHome);
                    return true;
                case R.id.navigation_explore:
                    navController.navigate(R.id.navigation_explore, null, navOptionsExplore);
                    return true;
                case R.id.navigation_dashboard:
                    navController.navigate(R.id.navigation_dashboard, null, navOptionsDashboard);
                    return true;
            }
            return false;
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

}