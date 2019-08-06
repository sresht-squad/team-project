package com.example.restauranteur.Customer.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

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
    public ViewPager vpPager;
    FragmentPagerAdapter adapterViewPager;
    androidx.appcompat.widget.Toolbar mActionBarToolbar;
    CustomerRequestsFragment myFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        vpPager = (ViewPager) findViewById(R.id.vpPager);
        Intent intent = getIntent();
        visit = intent.getParcelableExtra("VISIT");

        setSupportActionBar((androidx.appcompat.widget.Toolbar) findViewById(R.id.myToolbar));

        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null

        final FragmentManager fragmentManager = getSupportFragmentManager();

        customerBottomNavigation = findViewById(R.id.bottom_navigation);

        final Fragment requests = new CustomerRequestsFragment();
        final Fragment menu = new CustomerMenuFragment();

        final ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new CustomerHomeActivity.MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        
        //ViewPager page indicator
        InkPageIndicator inkPageIndicator = (InkPageIndicator) findViewById(R.id.indicator);
        inkPageIndicator.setViewPager(vpPager);

        vpPager.setCurrentItem(0);
        vpPager.setOnPageChangeListener(new PageChange());

        customerBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.action_menu:
                        vpPager.setCurrentItem(0);
                        return true;
                    case R.id.action_request:
                        vpPager.setCurrentItem(1);
                        return true;
                }
                return false;
            }
        });

        //setdefault
        customerBottomNavigation.setSelectedItemId(R.id.action_menu);

        vpPager.setOffscreenPageLimit(3);

    }

    public void setTitle(String title) {
        getActionBar().setTitle("NOPE");
    }


    public class MyPagerAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS = 2;

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
                    return CustomerMenuFragment.newInstance(0, "Menu");
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return CustomerRequestsFragment.newInstance(1, "Requests");
                default:
                    return CustomerMenuFragment.newInstance(0, "Menu");
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

        @Override
        public int getItemPosition(Object object) {
            if (object instanceof CustomerMenuFragment) {
                return POSITION_UNCHANGED;
            } else {
                return POSITION_NONE;
            }
        }


    }

    public class PageChange implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    customerBottomNavigation.setSelectedItemId(R.id.action_menu);
                    break;
                case 1:
                    customerBottomNavigation.setSelectedItemId(R.id.action_request);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.customer_menu_top, menu);

        MenuItem miLogout = menu.findItem(R.id.ivLogout);
        miLogout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Customer.logOut();
                Intent intent = new Intent(CustomerHomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
