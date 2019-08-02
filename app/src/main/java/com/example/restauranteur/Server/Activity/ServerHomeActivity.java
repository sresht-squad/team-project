package com.example.restauranteur.Server.Activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
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

import com.example.restauranteur.Model.Server;
import com.example.restauranteur.R;
import com.example.restauranteur.Server.Fragment.ServerActiveVisitsFragment;
import com.example.restauranteur.Server.Fragment.ServerProfileFragment;
import com.example.restauranteur.Server.Fragment.ServerRequestsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ServerHomeActivity extends AppCompatActivity {

    public ImageView logout;
    BottomNavigationView bottomNavigationView;
    FragmentPagerAdapter adapterViewPager;
    androidx.appcompat.widget.Toolbar mActionBarToolbar;

    public View activeNotificationBadge;
    public View requestNotificationBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        final ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        // could be a cool effect to make seem like a picture gallery , need to also comment out getPageWidth()
        // in the adapter
        /*vpPager.setClipToPadding(false);
        vpPager.setPageMargin(12);*/

        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        //viewPager transformation when swiping
        //ViewPager page indicator
        InkPageIndicator inkPageIndicator = (InkPageIndicator) findViewById(R.id.indicator);
        inkPageIndicator.setViewPager(vpPager);

        //implementing fragments
        bottomNavigationView =  findViewById(R.id.bottom_navigation);

        //making Active badge Visible
        addBadgeActiveView(Server.getCurrentServer().getVisits().size());
        refreshActiveBadgeView(Server.getCurrentServer().getVisits().size());

        /*addBadgeRequestView(5);
        refreshRequestBadgeView(5);*/

        //making Request Badge Visible



        /*final FragmentManager fragmentManager = getSupportFragmentManager();

        // define fragments here
        final Fragment profile = new ServerProfileFragment();
        final Fragment requests = new ServerRequestsFragment();*/

        //default vpPager sets the profile as the default page
        vpPager.setCurrentItem(0);
        vpPager.setOnPageChangeListener(new PageChange());

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
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
                //fragmentManager.beginTransaction().replace(R.id.fragment_placeholder, fragment).commit();
                return false;
            }
        });
        //set default
        bottomNavigationView.setSelectedItemId(R.id.profile);

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
        activeNotificationBadge.setVisibility(size > 0? VISIBLE : GONE);
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
                    return ServerProfileFragment.newInstance(0, "Profile");
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return ServerRequestsFragment.newInstance(1, "Requests");
                case 2: // Fragment # 1 - This will show SecondFragment
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

        /*public float getPageWidth (int position) {
            return 0.93f;
        }*/
    }

    public void setTitle(String title) {
        mActionBarToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle(title);
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
