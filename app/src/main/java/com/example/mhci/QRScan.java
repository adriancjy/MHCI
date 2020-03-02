package com.example.mhci;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRScan extends Fragment implements ZXingScannerView.ResultHandler {

    User u2 = new User();
    private ZXingScannerView ScannerView;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        getPrefVal();

        //Set drawer locker to appear/hidden
        ((DrawerLocker) getActivity()).setDrawerEnabled(true);

        //Get the header out.

        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView tvGUID = (TextView) headerView.findViewById(R.id.tvGUID);
        tvGUID.setText(u2.getGuid());

        //ZXING Scanner
        ScannerView = new ZXingScannerView(getActivity());
        RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.relative_scan_take_single);
        rl.addView(ScannerView);
        ScannerView.setResultHandler(this);
        ScannerView.startCamera();
        ScannerView.setSoundEffectsEnabled(true);
        ScannerView.setAutoFocus(true);
        return v;
    }

    public void setPrefVal(){
        SharedPreferences sp = this.getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("guid", u2.getGuid());
        edit.putInt("points", u2.getPoints());
        edit.commit();
    }

    public void getPrefVal(){
        SharedPreferences sp = this.getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        u2.setGuid(sp.getString("guid", ""));
        u2.setPoints(sp.getInt("points", 0));
    }

    @Override
    public void handleResult(Result result) {
        String value = result.getText();
        //Add in intent to pass value to a new page, then check the value and retrieve from firebase
        //Also add in a button at the welcome page to allow for user to view ranking. Use intent to check if its access via pressing the button or just view rnaking to hide or show the drawer.
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}
