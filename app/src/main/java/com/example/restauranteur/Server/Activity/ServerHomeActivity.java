package com.example.restauranteur.Server.Activity;

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
import com.example.restauranteur.LoginActivity;
import com.example.restauranteur.Model.Server;
import com.example.restauranteur.R;
import com.example.restauranteur.Server.Fragment.ServerActiveVisitsFragment;
import com.example.restauranteur.Server.Fragment.ServerProfileFragment;
import com.example.restauranteur.Server.Fragment.ServerRequestsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pixelcan.inkpageindicator.InkPageIndicator;

public class ServerHomeActivity extends AppCompatActivity {

    ImageView logout;
    BottomNavigationView bottomNavigationView;
    FragmentPagerAdapter adapterViewPager;
    androidx.appcompat.widget.Toolbar mActionBarToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        // could be a cool effect to make seem like a picture gallery , need to also comment out getPageWidth()
        // in the adapter
        /*vpPager.setClipToPadding(false);
        vpPager.setPageMargin(12);*/

        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        //viewPager transformation when swiping
        vpPager.setPageTransformer(true, new BackgroundToForegroundTransformer());
        //ViewPager page indicator
        InkPageIndicator inkPageIndicator = (InkPageIndicator) findViewById(R.id.indicator);
        inkPageIndicator.setViewPager(vpPager);

        //implementing fragments
        bottomNavigationView =  findViewById(R.id.bottom_navigation);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.profile:
                        vpPager.setCurrentItem(0);
                        setTitle("Profile");
                        break;
                    case R.id.requests:
                        vpPager.setCurrentItem(1);
                        setTitle("Requests");
                        break;
                    case R.id.action_active:
                        vpPager.setCurrentItem(2);
                        setTitle("Active Visits");
                        break;
                    default:
                        vpPager.setCurrentItem(0);
                        setTitle("Profile");
                        break;
                }
                return false;
            }
        });
        //set default
        bottomNavigationView.setSelectedItemId(R.id.profile);


        logout = findViewById(R.id.ivLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Server.logOut();
                Intent intent = new Intent(ServerHomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS = 3;

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
                    setTitle("Profile");
                    return ServerProfileFragment.newInstance(0, "Profile");
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    setTitle("Requests");
                    return ServerRequestsFragment.newInstance(1, "Requests");
                case 2: // Fragment # 1 - This will show SecondFragment
                    setTitle("Active Visits");
                    return ServerActiveVisitsFragment.newInstance(2, "Active Visits");
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "page" + position;
        }

    }

    public void setTitle(String title) {
        mActionBarToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle(title);
    }


}
