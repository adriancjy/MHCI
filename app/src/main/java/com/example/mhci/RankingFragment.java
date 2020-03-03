package com.example.mhci;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RankingFragment extends Fragment {

    private DatabaseReference mDatabase;

    private ListView listView;

    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayList<User> userList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private User user = new User();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_ranking, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference("guidProfile");

        ((DrawerLocker) getActivity()).setDrawerEnabled(true);

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
        listView = (ListView) v.findViewById(R.id.rankinglistView);
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.listview_header,listView,false);
        listView.addHeaderView(header);
        listView.setAdapter(adapter);

        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {


                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                     user = postSnapshot.getValue(User.class);
                     userList.add(user);

                }
                RankingPointsSorter rps = new RankingPointsSorter(userList);
                for(int i = 0; i < rps.getSortedJobCandidateByAge().size(); i++){
                    arrayList.add(i+1 + ": " + rps.getSortedJobCandidateByAge().get(i).toString());
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Failure", "Failed to read value.", error.toException());
            }
        });


        return v;
    }
}
