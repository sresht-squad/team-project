package com.example.restauranteur.Customer.Fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.restauranteur.Model.MenuItem;
import com.example.restauranteur.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.restauranteur.Keys.REST_CONSUMER_KEY;
import static com.example.restauranteur.Keys.REST_CONSUMER_SECRET;

public class CustomerMenuFragment extends Fragment {

    private MenuAdapter menuAdapter;
    private RecyclerView rvMenu;
    private ArrayList<MenuItem> foodItems;
    private String restaurantName;
    private TextView menuName;
    private MenuAdapter menuAdapterSearch;

    public CustomerMenuFragment() {
        //required empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public static CustomerMenuFragment newInstance(int page, String title) {
        CustomerMenuFragment fragmentFirst = new CustomerMenuFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_menu, parent, false);
    }


    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        foodItems = new ArrayList<>();
        menuAdapter = new MenuAdapter(foodItems);
        menuName = view.findViewById(R.id.tvMenuTitle);
        rvMenu = view.findViewById(R.id.rvMenuItems);
        rvMenu.setAdapter(menuAdapter);
        // associate the LayoutManager with the RecyclerView
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvMenu.setLayoutManager(linearLayoutManager);

        String search_authentication = "&client_id=" + REST_CONSUMER_KEY + "&client_secret=" + REST_CONSUMER_SECRET + "&v=20190729";
        String authentication = "?client_id=" + REST_CONSUMER_KEY + "&client_secret=" + REST_CONSUMER_SECRET + "&v=20190729";

        String menuAuthentication = "/menu?client_id=" + REST_CONSUMER_KEY + "&client_secret=" + REST_CONSUMER_SECRET + "&v=20190729";

        String panera = "4b70720ff964a520e71a2de3";
        String oliveGarden = "46d715faf964a5206c4a1fe3";
        String restaurantWithNoMenu = "51c9f997498e17f9e5bfbb52";
        String appleBees = "4c349961a0ced13a7463186e"; //has prices
        String villageInn = "4b53440df964a520e59427e3";


        String restaurant = villageInn;

        String url_search = "https://api.foursquare.com/v2/venues/search_toolbar?ll=40.7,-74" + search_authentication;
        String url_venue_details = "https://api.foursquare.com/v2/venues/" + restaurant + authentication;
        String url_menu = "https://api.foursquare.com/v2/venues/" + restaurant + menuAuthentication;

        RequestQueue queue = Volley.newRequestQueue(getContext());
        getRestaurantName(queue, url_venue_details);
        getMenu(queue, url_menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.customer_menu_top_fragment, menu);

        //lookup the searchview
        android.view.MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        //set hint
        searchView.setQueryHint("Search Menu...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                searchMenu(s);

                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        //on close of the searchview, go back to the full menu
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                searchMenu("");

                return false;
            }
        });


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.ivLogout) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }


    // Instantiate the RequestQueue


    void getMenu(RequestQueue queue, String url_menu) {
        // Request a string response from the provided URL.
        // final JSONObject obj;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_menu,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray menus = obj.getJSONObject("response").getJSONObject("menu").getJSONObject("menus")
                                    .getJSONArray("items");

                            //for all main menus
                            for (int a = 0; a < menus.length(); a++) {
                                JSONObject mainMenu = menus.getJSONObject(a).getJSONObject("entries");

                                JSONArray subMenus = mainMenu.getJSONArray("items");
                                int subMenuNum = Integer.valueOf(mainMenu.getString("count"));

                                //This part gets the menu headings: breakfast, appetizers, etc
                                for (int i = 0; i < subMenuNum; i++) {
                                    JSONObject subMenu = subMenus.getJSONObject(i);
                                    String sectionTitle = subMenu.getString("name");
                                    MenuItem heading = new MenuItem();
                                    heading.setName(sectionTitle);
                                    Log.i("SECTION", sectionTitle);
                                    heading.setHeading(true);
                                    foodItems.add(heading);
                                    menuAdapter.notifyDataSetChanged();

                                    int foodsCount = subMenu.getJSONObject("entries").getInt("count");
                                    JSONArray foods = subMenu.getJSONObject("entries").getJSONArray("items");

                                    //Gets the specific menu items under that heading
                                    for (int j = 0; j < foodsCount; j++) {
                                        JSONObject foodObject = foods.getJSONObject(j);
                                        MenuItem food = new MenuItem();
                                        if (foodObject.has("name")) {
                                            food.setName(foodObject.getString("name"));
                                        }
                                        if (foodObject.has("description")) {
                                            food.setDescription(foodObject.getString("description"));
                                        }
                                        if (foodObject.has("price")) {
                                            food.setPrice(foodObject.getString("price"));
                                        }
                                        food.setHeading(false);
                                        foodItems.add(food);
                                        menuAdapter.notifyDataSetChanged();

                                    }
                                }
                            }
                        } catch (JSONException e) {
                            Log.i("Volley error", "error getting menu data");
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Volley error", "error getting menu data");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    void searchMenu(String query){
        ArrayList<MenuItem> results = new ArrayList<>();
        for (int i = 0; i < foodItems.size(); i++){
            if (foodItems.get(i).getName().contains(query)){
                results.add(foodItems.get(i));
            }
        }
        menuAdapterSearch = new MenuAdapter(results);
        rvMenu.setAdapter(menuAdapterSearch);
    }

    void getRestaurantName(RequestQueue queue, String url_venue){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_venue,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject obj = new JSONObject(response);
                            obj = obj.getJSONObject("response").getJSONObject("venue");
                            restaurantName = obj.getString("name");
                            Log.i("NAME_REST", restaurantName);
                            menuName.setText(restaurantName);
                            // restaurantName = venue.toString();
                        }
                        catch (JSONException e) {
                            Log.i("Volley error", "error getting menu data");
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Volley error", "error getting menu data");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}
