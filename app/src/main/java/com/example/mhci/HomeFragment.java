package com.example.mhci;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.google.android.material.navigation.NavigationView;

public class HomeFragment extends Fragment {

    Button viewRanking, startQuiz;
    User u = new User();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        getPrefVal();
        //Set drawer locker to appear/hidden
        ((DrawerLocker) getActivity()).setDrawerEnabled(true);

        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView tvGUID = (TextView) headerView.findViewById(R.id.tvGUID);
        tvGUID.setText(u.getGuid());

        viewRanking = (Button) v.findViewById(R.id.btnRanking);
        startQuiz = (Button) v.findViewById(R.id.btnStartQuiz);

        viewRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                RankingFragment fragment = new RankingFragment();
                fragmentTransaction.replace(R.id.fragment_container, fragment).addToBackStack("HomeFragment");
                fragmentTransaction.commit();
            }
        });

        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                QRFragment fragment = new QRFragment();
                fragmentTransaction.replace(R.id.fragment_container, fragment).addToBackStack("HomeFragment");
                fragmentTransaction.commit();
            }
        });
        return v;
    }

    public void getPrefVal(){
        SharedPreferences sp = this.getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String key = sp.getString("key", "");
        u.setGuid(sp.getString("guid", ""));
        u.setPoints(sp.getInt("points", 0));
    }




}
