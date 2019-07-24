package com.example.restauranteur.Customer.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.restauranteur.R;
import com.example.restauranteur.Model.Customer;
import com.example.restauranteur.Model.Visit;
import com.example.restauranteur.Model.Message;
import com.parse.ParseException;
import com.parse.SaveCallback;

public class CustomerQuickRequestFragment extends Fragment {

    private CardView cvServerHelp;
    private CardView cvWater;
    private CardView cvCheck;
    private CardView cvToGoBox;

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
        cvServerHelp = (CardView) view.findViewById(R.id.cvServerHelp);
        cvWater = (CardView) view.findViewById(R.id.cvWater);
        cvCheck = (CardView) view.findViewById(R.id.cvCheck);
        cvToGoBox = (CardView) view.findViewById(R.id.cvToGoBox);
        final Customer customer = Customer.getCurrentCustomer();
        final Visit visit = customer.getCurrentVisit();

        cvServerHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serverHelpRequest = "In-person assistance";

                final Message serverHelpMessage = new Message();
                serverHelpMessage.setAuthor(customer);
                serverHelpMessage.setBody(serverHelpRequest);
                serverHelpMessage.setActive(true);

                serverHelpMessage.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.i("customerRequestFrag", "Water message on parse");
                        visit.addMessage(serverHelpMessage);
                        visit.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Log.i("VisitMessage", "added message to visit");
                            }
                        });
                    }
                });
            }
        });

        //sending the waiter a request to get the water
        //still need to connect to visit
        cvWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String waterRequest = "Need more water";

                final Message waterMessage = new Message();
                waterMessage.setAuthor(customer);
                waterMessage.setBody(waterRequest);
                waterMessage.setActive(true);

                waterMessage.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.i("customerRequestFrag", "Water message on parse");
                        visit.addMessage(waterMessage);
                        visit.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Log.i("VisitMessage", "added message to visit");
                            }
                        });
                    }
                });
            }
        });


        //sending the waiter request to get the check
        //still need to connect to visit
        cvCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String checkRequest = "Ready for Check";

                final Message checkMessage = new Message();
                checkMessage.setAuthor(customer);
                checkMessage.setBody(checkRequest);
                checkMessage.setActive(true);

                checkMessage.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.i("customerRequestFrag", "check message on parse");
                        visit.addMessage(checkMessage);
                        visit.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Log.i("VisitMessage", "added message to visit");
                            }
                        });
                    }
                });

            }
        });

        //sending the waiter request to get the check
        //still need to connect to visit
        cvToGoBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toGoBoxRequest = "To-go boxes";

                final Message toGoBoxMessage = new Message();
                toGoBoxMessage.setAuthor(customer);
                toGoBoxMessage.setBody(toGoBoxRequest);
                toGoBoxMessage.setActive(true);

                toGoBoxMessage.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        visit.addMessage(toGoBoxMessage);
                        visit.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Log.i("VisitMessage", "added message to visit");
                            }
                        });
                    }
                });

            }
        });

    }
}
