package com.example.mhci;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QuizFragment extends Fragment {
    final int NUMBER_OF_RADIOBUTTONS_TO_ADD = 4;//Change it for other number of RadioButtons
    RadioButton[] radioButton;
    RadioButton rd1, rd2;
    RadioGroup radioGroup1, radioGroup2;
    TextView tv1, tv2, schoolTv;
    String key, guidNo;
    DatabaseReference mDatabase;
    QuestionBank qb = new QuestionBank();
    Button submitBtn;
    User u = new User();
    Bundle args = new Bundle();
    int newPoints, totalPoints, TWO_CORRECT = 10, ONE_CORRECT = 5, NONE_CORRECT = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_quiz, container, false);
        getPrefVal();
        radioGroup1 = (RadioGroup) v.findViewById(R.id.rdg1);
        radioGroup2 = (RadioGroup) v.findViewById(R.id.rdg2);
        tv1 = (TextView) v.findViewById(R.id.tvQns1);
        tv2 = (TextView) v.findViewById(R.id.tvQns2);
        schoolTv = (TextView) v.findViewById(R.id.tvSchool2);
        submitBtn = (Button) v.findViewById(R.id.submitBtn);


        mDatabase = FirebaseDatabase.getInstance().getReference(getString(R.string.questionPath));

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.hasChild(key)){
                    qb = snapshot.child(key).getValue(QuestionBank.class);
                    schoolTv.setText(qb.getSchoolName());
                    tv1.setText(qb.getQuestion1());
                    tv2.setText(qb.getQuestion2());
                    randomizeRB(radioGroup1, qb.getAnswer1(), 2021);
                    randomizeRB(radioGroup2, qb.getAnswer2(), 1000);

                }



            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Failure", "Failed to read value.", error.toException());
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedID1 = radioGroup1.getCheckedRadioButtonId();
                int selectedID2 = radioGroup2.getCheckedRadioButtonId();
                rd1 = (RadioButton) radioGroup1.findViewById(selectedID1);
                rd2 = (RadioButton) radioGroup2.findViewById(selectedID2);

                if(qb.getAnswer1().equals(rd1.getText()) && qb.getAnswer2().equals(rd2.getText())){
                    mDatabase = FirebaseDatabase.getInstance().getReference(getString(R.string.guidPath));
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if(snapshot.hasChild(guidNo)){
                                u = snapshot.child(guidNo).getValue(User.class);
                                newPoints = u.getPoints();
                                totalPoints = newPoints + TWO_CORRECT;
                                u = new User(guidNo, totalPoints);
                                mDatabase.child(guidNo).child("points").setValue(totalPoints);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w("Failure", "Failed to read value.", error.toException());
                        }
                    });

                    fragmentNext(TWO_CORRECT);
                }else if(qb.getAnswer1().equals(rd1.getText()) || qb.getAnswer2().equals(rd2.getText())){
                    mDatabase = FirebaseDatabase.getInstance().getReference(getString(R.string.guidPath));
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if(snapshot.hasChild(guidNo)){
                                u = snapshot.child(guidNo).getValue(User.class);
                                newPoints = u.getPoints();
                                totalPoints = newPoints + ONE_CORRECT;
                                u = new User(guidNo, totalPoints);
                                mDatabase.child(guidNo).child("points").setValue(totalPoints);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w("Failure", "Failed to read value.", error.toException());
                        }
                    });

                    fragmentNext(ONE_CORRECT);

                }else{

                    mDatabase = FirebaseDatabase.getInstance().getReference(getString(R.string.guidPath));
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if(snapshot.hasChild(guidNo)){
                                u = snapshot.child(guidNo).getValue(User.class);
                                newPoints = u.getPoints();
                                totalPoints = newPoints + NONE_CORRECT;
                                u = new User(guidNo, totalPoints);
                                mDatabase.child(guidNo).child("points").setValue(totalPoints);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w("Failure", "Failed to read value.", error.toException());
                        }
                    });
                    fragmentNext(NONE_CORRECT);
                }
            }
        });

        return v;
    }

    public void getPrefVal(){
        SharedPreferences sp = this.getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        key = sp.getString("key", key);
        guidNo = sp.getString("guid", guidNo);
    }


    public void randomizeRB(RadioGroup rdg, String option, int bounds){
        radioButton = new RadioButton[NUMBER_OF_RADIOBUTTONS_TO_ADD];
        for (int i = 0; i < NUMBER_OF_RADIOBUTTONS_TO_ADD; i++) {
            radioButton[i] = new RadioButton(getActivity());
            ;
            //Text can be loaded here
            radioButton[i].setId(i);
            radioButton[i].setText(randomizeOptions(bounds));
        }

        radioButton[randomizeIndex()].setText(option);

        //Random Swapping
        for (int i = 0; i < 4; i++) {//this loop is randomly changing values 4 times
            int swap_ind1 = ((int) (Math.random() * 10) % NUMBER_OF_RADIOBUTTONS_TO_ADD);
            int swap_ind2 = ((int) (Math.random() * 10) % NUMBER_OF_RADIOBUTTONS_TO_ADD);
            RadioButton temp = radioButton[swap_ind1];
            radioButton[swap_ind1] = radioButton[swap_ind2];
            radioButton[swap_ind2] = temp;
        }


        //Adding RadioButtons in RadioGroup
        for (int i = 0; i < NUMBER_OF_RADIOBUTTONS_TO_ADD; i++) {
            rdg.addView(radioButton[i]);
        }

    }

    public String randomizeOptions(int bounds){
        Random rand = new Random();
        int n = rand.nextInt(bounds);
        return String.valueOf(n);
    }

    public int randomizeIndex(){
        Random rand = new Random();
        int n = rand.nextInt(4);
        return n;
    }

    public void fragmentNext(int points){
        args.putString("guid", guidNo);
        args.putInt("pointsEarned", points);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ScoreFragment fragment = new ScoreFragment();
        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.fragment_container, fragment).addToBackStack("QuizFragment");
        fragmentTransaction.commit();
    }


}
