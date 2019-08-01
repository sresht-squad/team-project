package com.example.restauranteur.Server.Fragment;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.restauranteur.Customer.Activity.CustomerHomeActivity;
import com.example.restauranteur.LoginActivity;
import com.example.restauranteur.R;
import com.example.restauranteur.Model.Server;

public class ServerProfileFragment extends Fragment implements NfcAdapter.CreateNdefMessageCallback{
    TextView tvId;
    EditText etTableNum;
    private String title;
    private int page;
    private Button btnLogout;

    public ServerProfileFragment() {
        // Required empty public constructor
    }

    public static ServerProfileFragment newInstance(int page, String title) {
        ServerProfileFragment fragmentFirst = new ServerProfileFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_server_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){

        tvId = view.findViewById(R.id.tvId);
        etTableNum = view.findViewById(R.id.etTableNum);
        btnLogout = view.findViewById(R.id.btnLogout);

        //check whether the phone is able to send via NFC
        NfcAdapter mAdapter = NfcAdapter.getDefaultAdapter(getContext());
        //no adapter, no NFC available on phone
        if (mAdapter == null) {
            Toast.makeText(getContext(), "This device does not have NFC capability.", Toast.LENGTH_LONG).show();
            return;
        }
        //if adapter exists but is not enabled, phone does not have NFC enabled in settings
        if (!mAdapter.isEnabled()) {
            Toast.makeText(getContext(), "Please enable NFC via Settings.", Toast.LENGTH_LONG).show();
        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Server.logOut();
                final Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        mAdapter.setNdefPushMessageCallback(this, getActivity());

        //set the text to the serverId
        Server currentServer = Server.getCurrentServer();
        String serverId = currentServer.getString("username");
        tvId.setText(serverId);
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
        //set the message to be the ServerID text view
        String message1 = tvId.getText().toString();
        String message2 = etTableNum.getText().toString();
        //create two different records for the two strings we are sending
        //they will go in the same ndef message
        NdefRecord ndefRecord1 = NdefRecord.createMime("text/plain", message1.getBytes());
        NdefRecord ndefRecord2 = NdefRecord.createMime("text/plain", message2.getBytes());
        NdefMessage ndefMessage = new NdefMessage(ndefRecord1,ndefRecord2);
        return ndefMessage;
    }

}
