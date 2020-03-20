package com.example.mhci;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class HelpDialog extends AppCompatDialogFragment{

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("E-Profile Card Help")
                .setMessage("Welcome to the e-profile card help dialog! In this page, you can view your own e-profile and edit your short description by holding onto the description area! You can also share your e-profile with others by using the NFC capabilities. Just tap" +
                        "your phone back facing back and then to share your e-profile, just tap on the beam! Its that simple!")
                .setPositiveButton("Ok I understand!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


        return builder.create();
    }
}
