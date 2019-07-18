package com.example.restauranteur;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.parse.ParseUser;

public class ServerProfileFragment extends Fragment {
    TextView tvId;
    Button viewOrders;

    public ServerProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_server_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        tvId = view.findViewById(R.id.tvId);

        //set the text to the serverId
        ParseUser currentUser = ParseUser.getCurrentUser();
        String serverId = currentUser.getString("serverId");
        tvId.setText(serverId);
    }
}
