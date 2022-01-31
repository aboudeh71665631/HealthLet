package com.example.firstproject.Navigation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.firstproject.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainNavigation extends AppCompatActivity {
    ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);

        chipNavigationBar = findViewById(R.id.chip_navigation_bar);
        chipNavigationBar.setItemSelected(R.id.Home, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment_container, new HomeFragment()).commit();
        bottomMenu();
    }

    private void bottomMenu() {
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i) {
                    case R.id.Home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.Appointments:
                        fragment = new AppointmentsFragment();
                        break;
                    case R.id.Shop:
                        fragment = new ShopFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment_container, fragment).commit();
            }
        });
    }
}