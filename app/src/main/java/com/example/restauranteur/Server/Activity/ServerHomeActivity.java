package com.example.restauranteur.Server.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.restauranteur.Model.Server;
import com.example.restauranteur.Model.Visit;
import com.example.restauranteur.R;
import com.example.restauranteur.Server.Fragment.ServerActiveVisitsFragment;
import com.example.restauranteur.Server.Fragment.ServerProfileFragment;
import com.example.restauranteur.Server.Fragment.ServerRequestsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ServerHomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FragmentPagerAdapter adapterViewPager;

    public View activeNotificationBadge;
    public View requestNotificationBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        int defaultFragment = R.id.profile;
        Intent intent = getIntent();
        if (intent.getExtras() != null){
            if (intent.getBooleanExtra("DETAIL", false)) {
                defaultFragment = R.id.action_active;
            }
        }

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        final ViewPager vpPager = findViewById(R.id.vpPager);

        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        //viewPager transformation when swiping
        //ViewPager page indicator
        InkPageIndicator inkPageIndicator = findViewById(R.id.indicator);
        inkPageIndicator.setViewPager(vpPager);

        //implementing fragments
        bottomNavigationView =  findViewById(R.id.bottom_navigation);

        //making Active badge Visible
        List<Visit> visits = Server.getCurrentServer().getVisits();
        if (visits != null) {
            addBadgeActiveView(visits.size());
            refreshActiveBadgeView(visits.size());
        }
        
        vpPager.setCurrentItem(0);
        vpPager.setOnPageChangeListener(new PageChange());

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.profile:
                        vpPager.setCurrentItem(0);
                        return true;
                    case R.id.requests:
                        vpPager.setCurrentItem(1);
                        return true;
                    case R.id.action_active:
                        vpPager.setCurrentItem(2);
                        return true;
                }
                return false;
            }
        });
        //set default
        bottomNavigationView.setSelectedItemId(defaultFragment);

    }

    //connecting badge to textView and setting text
   public void addBadgeActiveView(int notificationSize){
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(2);

        activeNotificationBadge = LayoutInflater.from(this).inflate(R.layout.view_notification_badge, menuView, false);

        TextView numberOfActive = activeNotificationBadge.findViewById(R.id.notificationsBadgeTextView);

        String notificationSizeToString = Integer.toString(notificationSize);

        numberOfActive.setText(notificationSizeToString);
        itemView.addView(activeNotificationBadge);
    }



    //when to make the Active Visits badge visible
     public void refreshActiveBadgeView(int size) {
        activeNotificationBadge.setVisibility(size != 0? VISIBLE : GONE);
    }

    //connecting badge to textView and setting text
    public void addBadgeRequestView (int requestSize){
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(1);

        requestNotificationBadge = LayoutInflater.from(this).inflate(R.layout.view_notification_badge, menuView, false);

        TextView numberOfActive = requestNotificationBadge.findViewById(R.id.notificationsBadgeTextView);

        String notificationSizeToString = Integer.toString(requestSize);

        numberOfActive.setText(notificationSizeToString);
        itemView.addView(requestNotificationBadge);

    }

    //when to make the request badge visible
    public void refreshRequestBadgeView(int numberOfMessages){
        requestNotificationBadge.setVisibility(numberOfMessages > 0? VISIBLE : GONE);
    }


    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 3;

        private MyPagerAdapter(FragmentManager fragmentManager) {
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
                    return ServerProfileFragment.newInstance(0, "Profile");
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return ServerRequestsFragment.newInstance(1, "Requests");
                case 2: // Fragment # 1 - This will show SecondFragment
                    return ServerActiveVisitsFragment.newInstance(2, "Active Visits");
                default:
                    return null;
            }
        }


        //This forces the visits and requests fragments to refresh when notifydatasetchanged is called
        public int getItemPosition(@NonNull Object object) {
            if (object instanceof ServerProfileFragment) {
                return POSITION_UNCHANGED;
            } else {
                return POSITION_NONE;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "page" + position;
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
                    bottomNavigationView.setSelectedItemId(R.id.profile);
                    break;
                case 1:
                    bottomNavigationView.setSelectedItemId(R.id.requests);
                    break;
                case 2:
                    bottomNavigationView.setSelectedItemId(R.id.action_active);
                    break;
            }
        }
        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }


}
