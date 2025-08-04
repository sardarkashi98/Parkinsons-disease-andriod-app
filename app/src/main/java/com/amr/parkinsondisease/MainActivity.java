package com.amr.parkinsondisease;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize ViewPager2 and BottomNavigationView
        viewPager = findViewById(R.id.viewPager);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Reduce BottomNavigationView shadow (elevation)
        bottomNavigationView.setElevation(4f); // Reduced from default (8f)

        // Setup ViewPager2 with fragments
        setupViewPager();

        // Bottom Navigation View Listener
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        // Synchronize ViewPager2 with BottomNavigationView
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }
        });

        // Set Home as the default fragment
        if (savedInstanceState == null) {
            viewPager.setCurrentItem(0, false);
        }
    }

    private void setupViewPager() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new ServicesFragment());
        fragmentList.add(new DietPlanFragment());
        fragmentList.add(new SettingsFragment());

        FragmentAdapter adapter = new FragmentAdapter(this, fragmentList);
        viewPager.setAdapter(adapter);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                int id = item.getItemId();
                if (id == R.id.navigation_home) {
                    viewPager.setCurrentItem(0, true);
                } else if (id == R.id.navigation_services) {
                    viewPager.setCurrentItem(1, true);
                } else if (id == R.id.navigation_diet_plan) {
                    viewPager.setCurrentItem(2, true);
                } else if (id == R.id.navigation_settings) {
                    viewPager.setCurrentItem(3, true);
                }
                return true;
            };
}
