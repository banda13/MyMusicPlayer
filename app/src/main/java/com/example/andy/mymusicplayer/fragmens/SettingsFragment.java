package com.example.andy.mymusicplayer.fragmens;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.andy.mymusicplayer.R;

public class SettingsFragment extends DialogFragment implements
        DialogInterface.OnClickListener {

    public static final String TAG = "OptionsFragment";
    public static final String POS_KEY = "PosiionKey";
    public static final String F_KEY = "FragmentKey";

    public interface OptionsFragmentInterface {
        public void onOptionsFragmentResult(String option, int pos, int fragment);
    }

    private String[] options = {"Next Song", "Details", "Delete"};
    private OptionsFragmentInterface optionsFragmentInterface;
    private int pos;
    private int fragment;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            optionsFragmentInterface = (OptionsFragmentInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OptionsFragmentInterface");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        pos = getArguments().getInt(POS_KEY);
        fragment = getArguments().getInt(F_KEY);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Options");
        builder.setItems(options, this);
        builder.setIcon(R.drawable.ic_apps);
        AlertDialog alert = builder.create();

        return alert;
    }

    @Override
    public void onClick(DialogInterface dialog, int choice) {
        optionsFragmentInterface.onOptionsFragmentResult(options[choice], pos, fragment);
    }
}