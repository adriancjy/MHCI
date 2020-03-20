package com.example.mhci;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.Image;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
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

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.w3c.dom.Text;

public class OtherProfile extends AppCompatActivity {

    public static final String MIME_TEXT_PLAIN = "text/plain";

    private OutcomingNfcManager outcomingNfccallback;
    private NfcAdapter nfcAdapter;
    User u = new User();
    User newU = new User();
    String guid = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);


        if (!isNfcSupported()) {
            Toast.makeText(this, "Nfc is not supported on this device", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (!nfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC disabled on this device. Turn on to proceed", Toast.LENGTH_SHORT).show();
        }

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference(getString(R.string.guidPath));
        final TextView tvName = (TextView) findViewById(R.id.tvName2);
        final TextView tvCourse = (TextView) findViewById(R.id.tvCourse2);
        final TextView tvDifferentiate = (TextView) findViewById(R.id.tvCardTitle2);
        final TextView tvInfo = (TextView)findViewById(R.id.tvInfo2);
        final ImageView imHelp = (ImageView) findViewById(R.id.imHelp2);
        getPrefVal();










        imHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OtherHelpDialog hd = new OtherHelpDialog();
                hd.show(getSupportFragmentManager(), "Exit dialog");
            }
        });


        Button btnBack = (Button)findViewById(R.id.btnBack2);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });






    }

    // need to check NfcAdapter for nullability. Null means no NFC support on the device
    private boolean isNfcSupported() {
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        return this.nfcAdapter != null;
    }

    private void initViews() {
//        this.tvIncomingMessage = findViewById(R.id.nfcText);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // also reading NFC message from here in case this activity is already started in order
        // not to start another instance of this activity
        super.onNewIntent(intent);
        receiveMessageFromDevice(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // foreground dispatch should be enabled here, as onResume is the guaranteed place where app
        // is in the foreground
        enableForegroundDispatch(this, this.nfcAdapter);
        receiveMessageFromDevice(getIntent());
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableForegroundDispatch(this, this.nfcAdapter);
    }

    private void receiveMessageFromDevice(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage inNdefMessage = (NdefMessage) parcelables[0];
            NdefRecord[] inNdefRecords = inNdefMessage.getRecords();
            NdefRecord ndefRecord_0 = inNdefRecords[0];

            String inMessage = new String(ndefRecord_0.getPayload());
            guid = inMessage;
            final DatabaseReference database = FirebaseDatabase.getInstance().getReference(getString(R.string.guidPath));
            final TextView tvName = (TextView) findViewById(R.id.tvName2);
            final TextView tvCourse = (TextView) findViewById(R.id.tvCourse2);
            final TextView tvDifferentiate = (TextView) findViewById(R.id.tvCardTitle2);
            final TextView tvInfo = (TextView)findViewById(R.id.tvInfo2);

            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(guid)) {
                        newU = dataSnapshot.child(guid).getValue(User.class);
                        tvName.setText(newU.getName());
                        tvCourse.setText(newU.getCourse());
                        tvDifferentiate.setText(newU.getName() + "'s e-profile card!");
                        if(newU.getInformation().equals("")){
                            tvInfo.setText("You are a boring person....Hold here to add in interesting facts about yourself such as your hobbies etc!");
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

            tvInfo.setOnLongClickListener(null);

        }
    }


    // Foreground dispatch holds the highest priority for capturing NFC intents
    // then go activities with these intent filters:
    // 1) ACTION_NDEF_DISCOVERED
    // 2) ACTION_TECH_DISCOVERED
    // 3) ACTION_TAG_DISCOVERED

    // always try to match the one with the highest priority, cause ACTION_TAG_DISCOVERED is the most
    // general case and might be intercepted by some other apps installed on your device as well

    // When several apps can match the same intent Android OS will bring up an app chooser dialog
    // which is undesirable, because user will most likely have to move his device from the tag or another
    // NFC device thus breaking a connection, as it's a short range

    public void enableForegroundDispatch(AppCompatActivity activity, NfcAdapter adapter) {

        // here we are setting up receiving activity for a foreground dispatch
        // thus if activity is already started it will take precedence over any other activity or app
        // with the same intent filters


        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        //
        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException ex) {
            throw new RuntimeException("Check your MIME type");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    public void disableForegroundDispatch(final AppCompatActivity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
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
