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

public class OtherHelpDialog extends AppCompatDialogFragment{

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("E-Profile Card Help")
                .setMessage("Welcome to the e-profile card help dialog! You are currently viewing your friend's e-profile card! You must have gotten them by using the NFC capabilities!")
                .setPositiveButton("Yes Thank you!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


        return builder.create();
    }
}
