package com.example.restauranteur.Customer.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
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
    FragmentStatePagerAdapter adapterViewPager;
    androidx.appcompat.widget.Toolbar mActionBarToolbar;
    public static ViewPager vpPager;

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

        vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new CustomerHomeActivity.MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        //viewPager transformation when swiping
        vpPager.setPageTransformer(true, new BackgroundToForegroundTransformer());
        //ViewPager page indicator
        InkPageIndicator inkPageIndicator = (InkPageIndicator) findViewById(R.id.indicator);
        inkPageIndicator.setViewPager(vpPager);

        vpPager.setCurrentItem(0);
        vpPager.setOnPageChangeListener(new PageChange());

        customerBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.action_request:
                        vpPager.setCurrentItem(0);
                        return true;
                    case R.id.action_menu:
                        vpPager.setCurrentItem(1);
                        return true;
                }
                return false;
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

    public class MyPagerAdapter extends FragmentStatePagerAdapter {
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
                    Log.i("GET", "ITEM");
                    return CustomerRequestsFragment.newInstance(0, "Request");
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    Log.i("GET", "ITEM");
                    return CustomerMenuFragment.newInstance(1, "Quick Request");
                default:
                    Log.i("GET", "ITEM");
                    return null;
            }
        }

        @Override
        public int getItemPosition(Object object) {
            if (object instanceof CustomerMenuFragment) {
                return POSITION_UNCHANGED;
            } else {
                return POSITION_NONE;
            }
        }


        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
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
                    customerBottomNavigation.setSelectedItemId(R.id.action_request);
                    break;
                case 1:
                    customerBottomNavigation.setSelectedItemId(R.id.action_menu);
                    break;
            }
        }
        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

}
