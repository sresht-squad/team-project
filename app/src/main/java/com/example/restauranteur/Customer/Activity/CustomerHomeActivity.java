package com.example.restauranteur.Customer.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.restauranteur.Customer.Fragment.CustomerMenuFragment;
import com.example.restauranteur.Customer.Fragment.CustomerRequestsFragment;
import com.example.restauranteur.LoginSignup.LoginActivity;
import com.example.restauranteur.Model.Customer;
import com.example.restauranteur.Model.Visit;
import com.example.restauranteur.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseException;
import com.parse.SaveCallback;
import com.pixelcan.inkpageindicator.InkPageIndicator;

public class CustomerHomeActivity extends AppCompatActivity {
    private BottomNavigationView customerBottomNavigation;
    public ViewPager vpPager;
    public Visit visit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setSupportActionBar((androidx.appcompat.widget.Toolbar) findViewById(R.id.myToolbar));

        //find the viewpager and set the adapter
        vpPager = findViewById(R.id.vpPager);
        FragmentPagerAdapter adapterViewPager = new CustomerHomeActivity.MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        
        //ViewPager page indicator
        InkPageIndicator inkPageIndicator = findViewById(R.id.indicator);
        inkPageIndicator.setViewPager(vpPager);

        vpPager.setCurrentItem(0);
        vpPager.setOnPageChangeListener(new PageChange());

        //find and set the bottom navigation
        customerBottomNavigation = findViewById(R.id.bottom_navigation);
        customerBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.action_menu:
                        vpPager.setCurrentItem(0);
                        setTitleVisibility(true);
                        return true;
                    case R.id.action_request:
                        vpPager.setCurrentItem(1);
                        setTitleVisibility(false);
                        return true;
                }
                return false;
            }
        });

        //setdefault
        customerBottomNavigation.setSelectedItemId(R.id.action_menu);

        vpPager.setOffscreenPageLimit(3);
        final Customer customer = Customer.getCurrentCustomer();
        visit = customer.getCurrentVisit();
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

        MenuItem completeVisit= menu.findItem(R.id.completeVisit);
        completeVisit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                visit.setActive(false);
                visit.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Customer.logOut();
                        Intent intent = new Intent(CustomerHomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    //for setting logo vs title
    public void setTitleVisibility(Boolean logo){
        ImageView ivLogo = findViewById(R.id.ivLogo);
        TextView tvOrders = findViewById(R.id.tvOrders);
        if(logo){
            tvOrders.setVisibility(View.GONE);
            ivLogo.setVisibility(View.VISIBLE);
        }
        else{
            ivLogo.setVisibility(View.GONE);
            tvOrders.setVisibility(View.VISIBLE);
        }
    }

    //for changing logo visibility from fragment
    public void setLogoVisibility(Boolean visible){
        ImageView ivLogo = findViewById(R.id.ivLogo);
        if (visible){
            ivLogo.setVisibility(View.VISIBLE);
        }
        else{
            ivLogo.setVisibility(View.GONE);
        }
    }


    public class MyPagerAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS = 2;

        MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages, must be implemented
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return CustomerMenuFragment.newInstance(0, "Menu");
                case 1:
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
        public int getItemPosition(@NonNull Object object) {
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
}
