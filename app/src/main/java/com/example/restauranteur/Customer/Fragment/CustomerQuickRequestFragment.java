package com.example.restauranteur.Customer.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.restauranteur.R;
import com.example.restauranteur.Model.Customer;
import com.example.restauranteur.Model.Visit;
import com.example.restauranteur.Model.Message;
import com.parse.ParseException;
import com.parse.SaveCallback;

public class CustomerQuickRequestFragment extends Fragment {

   private Button getCheck;
   private Button getWater;

    public CustomerQuickRequestFragment(){
        //required empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_quickrequest, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        getCheck = (Button) view.findViewById(R.id.btnCheck);
        getWater = (Button) view.findViewById(R.id.btnWater);
        final Customer c = Customer.getCurrentCustomer();
        final Visit visit = c.getCurrentVisit();

        //sending the waiter request to get the check
        //still need to connect to visit
        getCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String checkMessage = "Ready for Check";

                final Message readyForCheck = new Message();
                readyForCheck.setAuthor(c);
                readyForCheck.setBody(checkMessage);
                readyForCheck.setActive(true);

                readyForCheck.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.i("customerRequestFrag", "check message on parse");
                        visit.addMessage(readyForCheck);
                    }
                });
                visit.addMessage(readyForCheck);
                visit.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.i("VisitMessage", "added message to visit");
                    }
                });

            }
        });

        //sending the waiter a request to get the water
        //still need to connect to visit
        getWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String waterRequest = "Need more water";

                final Message waterMessage = new Message();
                waterMessage.setAuthor(Customer.getCurrentCustomer());
                waterMessage.setBody(waterRequest);
                waterMessage.setActive(true);

                waterMessage.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.i("customerRequestFrag", "Water message on parse");
                        visit.addMessage(waterMessage);
                    }
                });
                visit.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.i("VisitMessage", "added message to visit");
                    }
                });
            }
        });
    }
}
