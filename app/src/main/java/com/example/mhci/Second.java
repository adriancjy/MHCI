package com.example.mhci;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.Image;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.w3c.dom.Text;

public class Second extends AppCompatActivity implements OutcomingNfcManager.NfcActivity {

    public static final String MIME_TEXT_PLAIN = "text/plain";
    private static final int PERMISSION_REQUEST_CODE = 200;


    private OutcomingNfcManager outcomingNfccallback;
    private NfcAdapter nfcAdapter;
    User u = new User();
    User newU = new User();
    String guid = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        if (!isNfcSupported()) {
            Toast.makeText(this, "Nfc is not supported on this device", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (!nfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC disabled on this device. Turn on to proceed", Toast.LENGTH_SHORT).show();
        }

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference(getString(R.string.guidPath));
        final TextView tvName = (TextView) findViewById(R.id.tvName);
        final TextView tvCourse = (TextView) findViewById(R.id.tvCourse);
        final TextView tvDifferentiate = (TextView) findViewById(R.id.tvCardTitle);
        final TextView tvInfo = (TextView)findViewById(R.id.tvInfo);
        final EditText etInfo = (EditText)findViewById(R.id.etInfo);
        final Button btnSave = (Button) findViewById(R.id.btnSave);
        final ImageView imHelp = (ImageView) findViewById(R.id.imHelp);
            getPrefVal();
            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(u.getGuid())) {
                        newU = dataSnapshot.child(u.getGuid()).getValue(User.class);
                        tvName.setText(newU.getName());
                        tvCourse.setText(newU.getCourse());
                        tvDifferentiate.setText("Your very own e-profile card");
                        if(newU.getInformation().equals("")){
                            tvInfo.setText("You are a boring person..... Hold here to add in interesting facts about yourself such as your hobbies etc!");
                        }else{
                            tvInfo.setText(newU.getInformation());
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("Failure", "Failed to read value.", error.toException());
                }
            });




        tvInfo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                etInfo.setVisibility(View.VISIBLE);
                btnSave.setVisibility(View.VISIBLE);
                tvInfo.setVisibility(View.INVISIBLE);
                etInfo.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                return false;
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etInfo.setVisibility(View.INVISIBLE);
                btnSave.setVisibility(View.INVISIBLE);
                tvInfo.setVisibility(View.VISIBLE);
                tvInfo.setText(etInfo.getText());
                String value = etInfo.getText().toString();
                database.child(u.getGuid()).child("information").setValue(value);


            }
        });


        imHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpDialog hd = new HelpDialog();
                hd.show(getSupportFragmentManager(), "Exit dialog");
            }
        });


        Button btnBack = (Button)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        this.outcomingNfccallback = new OutcomingNfcManager(Second.this);
        this.nfcAdapter.setOnNdefPushCompleteCallback(outcomingNfccallback, Second.this);
        this.nfcAdapter.setNdefPushMessageCallback(outcomingNfccallback, Second.this);



    setOutGoingMessage();

    }

    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        super.onNewIntent(intent);
    }

    private boolean isNfcSupported() {
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        return this.nfcAdapter != null;
    }

    private void setOutGoingMessage() {
        String outMessage = u.getGuid();
//        this.tvOutcomingMessage.setText(outMessage);
    }

    public String getOutcomingMessage() {
        return u.getGuid();
    }

    public void signalResult() {
        // this will be triggered when NFC message is sent to a device.
        // should be triggered on UI thread. We specify it explicitly
        // cause onNdefPushComplete is called from the Binder thread
//        runOnUiThread(() ->
//                Toast.makeText(getActivity(), R.string.message_beaming_complete, Toast.LENGTH_SHORT).show());
    }

    public void getPrefVal(){
        SharedPreferences sp = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        u.setGuid(sp.getString("guid", ""));
        u.setPoints(sp.getInt("points", 0));
    }

    public void setPrefVal(){
        SharedPreferences sp = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("guid",u.getGuid());
        edit.putInt("points", u.getPoints());
        edit.commit();
    }

    @Override
    public void onBackPressed() {
        setPrefVal();
        super.onBackPressed();
    }
    




}
