package com.example.mhci;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

public class ScoreFragment extends Fragment {
    private String guidNo;
    private int pointsEarned;
    TextView pointsEarnedTv;
    Button viewRankBtn;
    DatabaseReference mDatabase;
    User u = new User();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_score, container, false);
        Bundle bundle = getArguments();
        MainActivity ma = (MainActivity)getActivity();
        boolean status = ma.globalStatus;
        pointsEarnedTv = (TextView) v.findViewById(R.id.tvPointsEarned);
        viewRankBtn = (Button) v.findViewById(R.id.viewRankBtn);
        //Set drawer locker to appear/hidden
        ((DrawerLocker) getActivity()).setDrawerEnabled(true);


        if(status){
            if(bundle != null){
                guidNo = bundle.getString("guid", guidNo);
                pointsEarned = bundle.getInt("pointsEarned", pointsEarned);
                pointsEarnedTv.setText(String.valueOf(pointsEarned) + " Points!");
            }else{

            }
        }

        viewRankBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                RankingFragment fragment = new RankingFragment();
//                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.fragment_container, fragment).addToBackStack("ScoreFragment");
                fragmentTransaction.commit();
            }
        });
        return v;
    }


}
