package com.example.restauranteur.Server.Fragment;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.restauranteur.R;
import com.example.restauranteur.Model.Server;

public class ServerProfileFragment extends Fragment implements NfcAdapter.CreateNdefMessageCallback{
    TextView tvId;
    EditText etTableNum;

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
        etTableNum = view.findViewById(R.id.etTableNum);

        //check whether the phone is able to send via NFC
        NfcAdapter mAdapter = NfcAdapter.getDefaultAdapter(getContext());
        //no adapter, no NFC available on phone
        if (mAdapter == null) {
            tvId.setText("Sorry this device does not have NFC.");
            return;
        }
        //if adapter exists but is not enabled, phone does not have NFC enabled in settings
        if (!mAdapter.isEnabled()) {
            Toast.makeText(getContext(), "Please enable NFC via Settings.", Toast.LENGTH_LONG).show();
        }

        mAdapter.setNdefPushMessageCallback(this, getActivity());

        //set the text to the serverId
        Server currentServer = Server.getCurrentServer();
        String serverId = currentServer.getString("username");
        tvId.setText(serverId);
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
        //set the message to be the ServerID text view
        String message = tvId.getText().toString();
        //create the Ndef message
        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", message.getBytes());
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);
        return ndefMessage;
    }

}
