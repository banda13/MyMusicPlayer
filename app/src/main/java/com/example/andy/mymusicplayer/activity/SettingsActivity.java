package com.example.andy.mymusicplayer.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.example.andy.mymusicplayer.R;
import com.example.andy.mymusicplayer.musicplayer.MyMusicPlayer;

public class SettingsActivity extends PreferenceActivity {


    private MyMusicPlayer MPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

        MPlayer = ListActivity.MPlayer;

    }

    @Override
    public void onResume(){
    super.onResume();
    MPlayer.regist(this);
    }

    @Override
    public void onPause() {
        MPlayer.unregist(this);
        super.onPause();
    }
}
