package com.example.mhci;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

public class WelcomeFragment extends Fragment {

    String guidNo;
    int guidPoints;
    User newUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_welcome, container, false);

        final EditText guidText = v.findViewById(R.id.guidText);

        ((DrawerLocker) getActivity()).setDrawerEnabled(false);

        Button btnSubmit = v.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guidNo = guidText.getText().toString();
                final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                final DatabaseReference myRef = database.child("/guidProfile/" + guidNo);
                newUser = new User(guidNo, 0);
                myRef.setValue(newUser);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        newUser = dataSnapshot.getValue(User.class);
                        guidPoints = newUser.getPoints();
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
                QRScan fragment = new QRScan();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
            }
        });




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


}
