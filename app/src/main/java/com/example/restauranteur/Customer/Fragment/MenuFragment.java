package com.example.restauranteur.Customer.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
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

public class MenuFragment extends Fragment {

    private MenuAdapter menuAdapter;
    private RecyclerView rvMenu;
    private ArrayList<MenuItem> foodItems;

    public MenuFragment() {
        //required empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        rvMenu = view.findViewById(R.id.rvMenuItems);
        rvMenu.setAdapter(menuAdapter);
        // associate the LayoutManager with the RecyclerView
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvMenu.setLayoutManager(linearLayoutManager);

        String authentication =  REST_CONSUMER_KEY + "&client_secret=" + REST_CONSUMER_SECRET + "&v=20190729";

        String url_search = "https://api.foursquare.com/v2/venues/search?ll=40.7,-74&client_id=" + authentication;
        String url_venue_details =  "https://api.foursquare.com/v2/venues/4ac518eff964a52064ad20e3?client_id=" + authentication;
        String url_menu = "https://api.foursquare.com/v2/venues/4b70720ff964a520e71a2de3/menu?client_id=" + authentication;


        // Instantiate the RequestQueue.

        RequestQueue queue = Volley.newRequestQueue(getContext());
        // Request a string response from the provided URL.
        // final JSONObject obj;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_menu,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject entries = obj.getJSONObject("response").getJSONObject("menu").getJSONObject("menus")
                                    .getJSONArray("items").getJSONObject(0).getJSONObject("entries");
                            JSONArray subMenus = entries.getJSONArray("items");
                            int subMenuNum = Integer.valueOf(entries.getString("count"));

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
                                for (int j = 0; j < foodsCount; j++){
                                    JSONObject foodObject = foods.getJSONObject(j);
                                    MenuItem food = new MenuItem();
                                    food.setName(foodObject.getString("name"));
                                    food.setDescription(foodObject.getString("description"));
                                    food.setHeading(false);
                                    //food.setPrice(foodObject.getString("price"));
                                    foodItems.add(food);
                                    menuAdapter.notifyDataSetChanged();

                                }
                            }
                        } catch(
                    JSONException e)

                    {

                    }

                    // textView.setText("Response is: " + response);
                 }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // textView.setText("That didn't work!");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
}
