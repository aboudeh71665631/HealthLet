package com.example.firstproject.DoctorNavigation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.firstproject.Navigation.ShopFragment;
import com.example.firstproject.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class DoctorMainNavigation extends AppCompatActivity {
    ChipNavigationBar chipNavigationBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main_navigation);
        chipNavigationBar = findViewById(R.id.chip_navigation_bar);
        chipNavigationBar.setItemSelected(R.id.Home, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment_container, new DoctorHomeFragment()).commit();
        bottomMenu();
    }

    private void bottomMenu() {
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i) {
                    case R.id.Home:
                        fragment = new DoctorHomeFragment();
                        break;
                    case R.id.Appointments:
                        fragment = new DoctorAppointmentsFragment();
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