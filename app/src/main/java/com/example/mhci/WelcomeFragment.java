package com.example.mhci;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomeFragment extends Fragment implements OutcomingNfcManager.NfcActivity {

    String guidNo;
    int guidPoints;
    User newUser;
    String text = "Testing tag";

    private Button btnSetOutcomingMessage;

    private NfcAdapter nfcAdapter;
    private OutcomingNfcManager outcomingNfccallback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_welcome, container, false);
        if (!isNfcSupported()) {
            Toast.makeText(getActivity(), "Nfc is not supported on this device", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
        if (!nfcAdapter.isEnabled()) {
            Toast.makeText(getActivity(), "NFC disabled on this device. Turn on to proceed", Toast.LENGTH_SHORT).show();
        }



        final EditText guidText = v.findViewById(R.id.guidText);

        ((DrawerLocker) getActivity()).setDrawerEnabled(false);

        Button btnSubmit = v.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guidNo = guidText.getText().toString();
                setOutGoingMessage();
                final DatabaseReference database = FirebaseDatabase.getInstance().getReference(getString(R.string.guidPath));


                newUser = new User(guidNo, 0);
                database.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(guidNo)) {
                                newUser = dataSnapshot.child(guidNo).getValue(User.class);
                                guidPoints = newUser.getPoints();
                            } else {
                                //setvalue replaces the value so need to check!
                                database.child(guidNo).setValue(newUser);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("Failure", "Failed to read value.", error.toException());
                    }
                });
                setPrefVal();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                HomeFragment fragment = new HomeFragment();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
            }
        });

        this.outcomingNfccallback = new OutcomingNfcManager(this);
        this.nfcAdapter.setOnNdefPushCompleteCallback(outcomingNfccallback, getActivity());
        this.nfcAdapter.setNdefPushMessageCallback(outcomingNfccallback, getActivity());


        return v;
    }

    public void setPrefVal(){
        SharedPreferences sp = this.getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("guid",newUser.getGuid());
        edit.putInt("points", newUser.getPoints());
        edit.commit();
    }

    public void getPrefVal(){
        SharedPreferences sp = this.getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        newUser.setGuid(sp.getString("guid", ""));
        newUser.setPoints(sp.getInt("points", 0));
    }





    protected void onNewIntent(Intent intent) {
        getActivity().setIntent(intent);
    }

    private boolean isNfcSupported() {
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());
        return this.nfcAdapter != null;
    }

    private void setOutGoingMessage() {
        String outMessage = guidNo;
//        this.tvOutcomingMessage.setText(outMessage);
    }

    @Override
    public String getOutcomingMessage() {
        return guidNo;
    }

    @Override
    public void signalResult() {
        // this will be triggered when NFC message is sent to a device.
        // should be triggered on UI thread. We specify it explicitly
        // cause onNdefPushComplete is called from the Binder thread
//        runOnUiThread(() ->
//                Toast.makeText(getActivity(), R.string.message_beaming_complete, Toast.LENGTH_SHORT).show());
    }


}
