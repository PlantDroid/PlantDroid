package com.example.plantdroid;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.plantdroid.ui.dashboard.DashboardFragment;
import com.example.plantdroid.ui.home.HomeFragment;
import com.example.plantdroid.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.plantdroid.databinding.ActivityBottomNavigationBinding;

public class BottomNavigationActivity extends AppCompatActivity {
    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    private ActivityBottomNavigationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBottomNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_bottom_navigation);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            fragmentManager = getSupportFragmentManager();  //使用fragmentmanager和transaction来实现切换效果
            transaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_notifications:
                    transaction.replace(R.id.content,new NotificationsFragment());  //对应的java class
                    transaction.commit();  //一定不要忘记commit，否则不会显示
                    return true;

                case R.id.navigation_home:
                    transaction.replace(R.id.content,new HomeFragment());  //对应的java class
                    transaction.commit();  //一定不要忘记commit，否则不会显示

                    return true;
                case R.id.navigation_dashboard:
                    transaction.replace(R.id.content,new DashboardFragment());  //对应的java class
                    transaction.commit();  //一定不要忘记commit，否则不会显示

                    return true;
            }
            return false;
        }
    };

}