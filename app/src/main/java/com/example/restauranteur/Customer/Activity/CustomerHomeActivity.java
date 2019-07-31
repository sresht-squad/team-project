package com.example.restauranteur.Customer.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ToxicBakery.viewpager.transforms.BackgroundToForegroundTransformer;
import com.example.restauranteur.Customer.Fragment.CustomerMenuFragment;
import com.example.restauranteur.Customer.Fragment.CustomerRequestsFragment;
import com.example.restauranteur.LoginActivity;
import com.example.restauranteur.Model.Customer;
import com.example.restauranteur.Model.Visit;
import com.example.restauranteur.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pixelcan.inkpageindicator.InkPageIndicator;

public class CustomerHomeActivity extends AppCompatActivity {
    ImageView logout;
    BottomNavigationView customerBottomNavigation;
    Visit visit;
    FragmentPagerAdapter adapterViewPager;
    androidx.appcompat.widget.Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        Intent intent = getIntent();
        visit = intent.getParcelableExtra("VISIT");

        final FragmentManager fragmentManager = getSupportFragmentManager();

        customerBottomNavigation = findViewById(R.id.bottom_navigation);

        final Fragment requests = new CustomerRequestsFragment();
        final Fragment menu = new CustomerMenuFragment();

        final ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new CustomerHomeActivity.MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        //viewPager transformation when swiping
        vpPager.setPageTransformer(true, new BackgroundToForegroundTransformer());
        //ViewPager page indicator
        InkPageIndicator inkPageIndicator = (InkPageIndicator) findViewById(R.id.indicator);
        inkPageIndicator.setViewPager(vpPager);


        customerBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;

                switch (menuItem.getItemId()) {
                    case R.id.action_request:
                        vpPager.setCurrentItem(0);
                        break;
                    case R.id.action_menu:
                        vpPager.setCurrentItem(1);
                        break;
                    default:
                        vpPager.setCurrentItem(0);
                        break;
                }
                return true;
            }
        });

        //setdefault
        customerBottomNavigation.setSelectedItemId(R.id.action_request);


        logout = findViewById(R.id.ivLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Customer.logOut();
                Intent intent = new Intent(CustomerHomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void setActionBarTitle(String title) {
        mActionBarToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle(title);
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return CustomerRequestsFragment.newInstance(0, "Request");
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return CustomerMenuFragment.newInstance(1, "Quick Request");
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }



}

}
