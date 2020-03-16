package com.example.mhci;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MapFragment extends Fragment {

    private String key = "", guidNo = "";
    TextView imgV;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        getPrefVal();
        if(!key.equals("")) {
            if (key.equals("uogcs")) {
                imgV = (TextView) v.findViewById(R.id.uogcs);
            } else {
                imgV = (TextView) v.findViewById(R.id.uogeng);
            }
            blinkEffect(imgV);
        }
        return v;
    }

    public void getPrefVal(){
        SharedPreferences sp = this.getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        key = sp.getString("key", key);
        guidNo = sp.getString("guid", guidNo);
    }

    private void blinkEffect(TextView tv){
        ObjectAnimator anim = ObjectAnimator.ofInt(tv, "backgroundColor", Color.TRANSPARENT, Color.GREEN, Color.TRANSPARENT);
        anim.setDuration(800);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        anim.start();
    }
}
