package com.example.andy.mymusicplayer.broadcast_reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.andy.mymusicplayer.activity.ListActivity;

/**
 * Created by Andy on 2016. 11. 13..
 */
public class myReciver extends BroadcastReceiver {

    public static int past_state = -1;

    @Override
    public void onReceive(Context context, Intent intent) {
        int state = intent.getIntExtra("state", -1);
        Log.d("broadcast", "state: "+state+" past_state: "+past_state );
        if (state != past_state) {
            switch (state) {
                case 0:
                    Toast.makeText(context, "headsetunplugged", Toast.LENGTH_SHORT).show();
                    if (ListActivity.MPlayer.playing()) {
                        ListActivity.MPlayer.pause();
                    }
                    break;
                case 1:
                    Toast.makeText(context, "headsetplugged", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(context, "asdasd", Toast.LENGTH_SHORT).show();
            }
        }
        past_state = state;
    }

}
