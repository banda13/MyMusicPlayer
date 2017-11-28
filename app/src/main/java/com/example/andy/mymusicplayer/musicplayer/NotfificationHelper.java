package com.example.andy.mymusicplayer.musicplayer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.andy.mymusicplayer.activity.ListActivity;


public class NotfificationHelper extends Activity {


    private MyMusicPlayer MPlayer;

    @Override
    protected void onCreate(Bundle onSavedInstanceState) {
        super.onCreate(onSavedInstanceState);


        MPlayer = ListActivity.MPlayer;
        Log.i("asdasd", "asdasdasdasdassd");

        String action = (String) getIntent().getExtras().get("DO");
        if(MPlayer.getContext() != null) {
            if (action != null && action.equals("prev")) {
                //Toast.makeText(this, "Prev", Toast.LENGTH_SHORT).show();
                MPlayer.previous();
            }
            if (action != null && action.equals("next")) {
                //Toast.makeText(this, "Next", Toast.LENGTH_SHORT).show();
                MPlayer.next();
            }
            if (action != null && action.equals("play")) {
                //Toast.makeText(this, "Play", Toast.LENGTH_SHORT).show();
                MPlayer.playorpause();
            }
            if (action != null && action.equals("stop")) {
                //Toast.makeText(this, "Closed", Toast.LENGTH_SHORT).show();
                MPlayer.DisableNotification();
            }
        }
        finish();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

}
