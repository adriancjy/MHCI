package com.example.mhci;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class QuestionFragment extends Fragment {

    Trivia t;
    private String key = "";
    private DatabaseReference mDatabase;
    private String triviaText, schoolName;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_question, container, false);
        final TextView triviaTV = (TextView) v.findViewById(R.id.tvTrivia);
        final TextView schoolTv = (TextView) v.findViewById(R.id.tvSchool);
        Bundle bundle = getArguments();
        MainActivity ma = (MainActivity)getActivity();
        boolean status = ma.globalStatus;





        if(status){
            if(bundle != null){
                key = bundle.getString("key", key);
                setPrefVal();
            }else{
                getPrefVal();

            }
        }

        mDatabase = FirebaseDatabase.getInstance().getReference("trivia");

        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.hasChild(key)){
                    t = snapshot.child(key).getValue(Trivia.class);
                    triviaText = t.getTriviaText();
                    schoolName = t.getSchoolName();
                    Log.d("MSG", triviaText);
                    triviaTV.setText(triviaText);
                    schoolTv.setText(schoolName);
                }



            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Failure", "Failed to read value.", error.toException());
            }
        });


        return v;
    }

    public void setPrefVal(){
        SharedPreferences sp = this.getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("key",key);
        edit.commit();
    }

    public void getPrefVal(){
        SharedPreferences sp = this.getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        key = sp.getString("key", key);
    }
}
