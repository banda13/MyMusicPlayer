package com.example.andy.mymusicplayer.musicplayer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.andy.mymusicplayer.R;
import com.example.andy.mymusicplayer.activity.ListActivity;
import com.example.andy.mymusicplayer.activity.MusicActivity;
import com.example.andy.mymusicplayer.adapter.MusicAdapter;
import com.example.andy.mymusicplayer.broadcast_reciver.myReciver;
import com.example.andy.mymusicplayer.fragmens.Controll;
import com.example.andy.mymusicplayer.fragmens.Music_Played_Fragment;
import com.example.andy.mymusicplayer.soundtrack.Track;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Andy on 2016. 11. 08..
 */
public class MyMusicPlayer implements MediaPlayer.OnCompletionListener {

    private ArrayList<Track> tracks;
    private ArrayList<Track> tmpTracks;
    private MediaPlayer mediaPlayer;
    private MusicAdapter MAdapter;
    private boolean isShuffle;
    private boolean isRepeat;
    private boolean isNextOn;
    private int position;
    private Music_Played_Fragment fragment;
    private Controll controll_fragment;
    private Track playedTrack;
    private Context context;
    private RemoteViews bigView;
    private RemoteViews remoteView;
    private NotificationManager notificationmanager;
    private int FragmentId;
    public myReciver myReceiver;


    public MyMusicPlayer(Context c) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        isShuffle = false;
        isRepeat = false;
        isNextOn = false;
        context = c;
        MAdapter = ListActivity.MAdapter;
        tmpTracks = new ArrayList<>();
        tracks = new ArrayList<>();
        position = -1;
        // Create Notification Manager
        notificationmanager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        myReceiver = new myReciver();
        IntentFilter receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        c.registerReceiver(myReceiver, receiverFilter);

    }

    public void regist(Context c){
        IntentFilter receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        c.registerReceiver(myReceiver, receiverFilter);
    }
    public void unregist(Context c){

        c.unregisterReceiver(myReceiver);
    }
    public Context getContext(){
        return context;
    }


    public boolean setTracks(ArrayList<Track> list) {

        tracks = list;
        return true;
    }

    public void setTmpTracks(ArrayList<Track> tmplist) {
        tmpTracks = tmplist;
        isNextOn = true;
    }

    public void setFragmentId(int f){
        FragmentId = f;
    }

    public void setPosition(int pos) {
        if (position != pos) {
            position = pos;
            play(tracks.get(position));
        } else {
            fragment.playSong(playedTrack);
            fragment.setThreadrun(mediaPlayer.isPlaying());
            fragment.setPlayButtonState(mediaPlayer.isPlaying());
        }
    }

    public void play(Track t) {
        playedTrack = t;

        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }

            mediaPlayer.reset();
            mediaPlayer.setDataSource(t.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();

            if (fragment != null) {
                fragment.playSong(playedTrack);
                fragment.setThreadrun(true);
                fragment.setPlayButtonState(true);
            }
            if(controll_fragment != null){
                controll_fragment.setPlayPause();
                controll_fragment.setText(playedTrack.getName());
            }

            createNotification(playedTrack);

        } catch (IllegalArgumentException | IllegalStateException | IOException e) {
            e.printStackTrace();
        }
    }


    public void seek(int time) {
        mediaPlayer.seekTo(time);
    }

    public void pause() {
        mediaPlayer.pause();
        if (fragment != null) {
            fragment.setPlayButtonState(false);
        }
        if(controll_fragment != null){
            controll_fragment.setPlayPause();
        }
        createNotification(playedTrack);

    }
    public void playorpause(){
        if(mediaPlayer.isPlaying()){
            pause();
        }
        else{
            resume();
        }
    }

    public void resume() {
        mediaPlayer.start();
        if (fragment != null) {
            fragment.setPlayButtonState(true);
        }
        if(controll_fragment != null){
            controll_fragment.setPlayPause();
        }
        createNotification(playedTrack);
    }

    public boolean playing() {
        return mediaPlayer.isPlaying();
    }

    public long getDuration() {
        return mediaPlayer.getDuration();
    }

    public long getCurrPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public void setShuffle(boolean shuffle) {
        isShuffle = shuffle;
    }

    public void setRepeat(boolean repeat) {
        isRepeat = repeat;
    }

    public void addTmp(Track t){
        if(!tmpTracks.contains(t)) {
            tmpTracks.add(t);
            isNextOn = true;
        }
    }
    public void deleteTmps(){
        tmpTracks.clear();
    }

    public String getName() {
        if(playedTrack != null) {
            return playedTrack.getName();
        }
        else{
            return null;
        }
    }

    public Track getTrack() {
        if(playedTrack != null){
            return playedTrack;
        }
        else{
            return null;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (fragment != null) {
            fragment.setThreadrun(false);
        }
        if (isNextOn) {
            play(tmpTracks.get(0));
            MAdapter.deleteTmp(tmpTracks.get(0));
            tmpTracks.remove(tmpTracks.get(0));
            if (tmpTracks.isEmpty()) {
                isNextOn = false;
            }
        } else if(!tracks.isEmpty()){
            if (isRepeat) {
                // repeat is on play same song again
                play(tracks.get(position));
            } else if (isShuffle) {
                // shuffle is on - play a random song
                Random rand = new Random();
                position = rand.nextInt((tracks.size() - 1) - 0 + 1) + 0;
                play(tracks.get(position));
            } else {
                // no repeat or shuffle ON - play next song
                if (position < (tracks.size() - 1)) {
                    play(tracks.get(position + 1));
                    position = position + 1;
                } else {
                    // play first song
                    if(!tracks.isEmpty()) {
                        play(tracks.get(0));
                    }
                    position = 0;
                }
            }
        }
        else{
            Toast.makeText(context, "The TrackList is empty", Toast.LENGTH_SHORT).show();
        }
    }

    public void previous() {
        if (position > 0) {
            play(tracks.get(position - 1));
            position = position - 1;
        } else {
            // play last song
            play(tracks.get(tracks.size() - 1));
            position = tracks.size() - 1;
        }
    }

    public void next() {
        if (isNextOn && !tmpTracks.isEmpty()) {
            Track t = tmpTracks.get(0);
            play(t);
            MAdapter.deleteTmp(t);
            tmpTracks.remove(t);


            if (tmpTracks.isEmpty()) {
                isNextOn = false;
            }
        } else if(!tracks.isEmpty()){
            if (isShuffle) {
                // shuffle is on - play a random song
                Random rand = new Random();
                position = rand.nextInt((tracks.size() - 1) - 0 + 1) + 0;
                play(tracks.get(position));
            } else {
                // no repeat or shuffle ON - play next song
                if (position < (tracks.size() - 1)) {
                    play(tracks.get(position + 1));
                    position = position + 1;
                } else {
                    // play first song
                    position = 0;
                    play(tracks.get(position));

                }
            }
        }
        else{
            Toast.makeText(context, "The TrackList is empty", Toast.LENGTH_SHORT).show();
        }
    }


    public void setFragment(Music_Played_Fragment mpf) {
        fragment = mpf;
    }

    public void removeFragment() {
        fragment = null;
    }

    public void setControll_fragment(Controll c){
        controll_fragment = c;
    }
    public void removeControll_fragment(){
        controll_fragment = null;
    }

    public void createNotification(Track t) {


        remoteView = new RemoteViews(context.getPackageName(), R.layout.status_bar);
        bigView = new RemoteViews(context.getPackageName(), R.layout.status_bar_expanded);


        Intent intent = new Intent(context, MusicActivity.class);
        intent.putExtra(Music_Played_Fragment.KEY_ID, position);
        intent.putExtra(Music_Played_Fragment.FRAGMENT_KEY, FragmentId);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.myicon2)
                .addAction(R.drawable.myicon2, "Open Music Player", pIntent)
                .setContentIntent(pIntent)
                .setContent(remoteView)
                .setOngoing(true)
                .setCustomBigContentView(bigView);


        if (mediaPlayer.isPlaying()) {
            remoteView.setImageViewResource(R.id.status_bar_play,
                    R.drawable.ic_pause_white_24dp);
            bigView.setImageViewResource(R.id.status_bar_play, R.drawable.ic_pause_white_24dp);
        } else {
            remoteView.setImageViewResource(R.id.status_bar_play, R.drawable.ic_play_arrow_white_24dp);
            bigView.setImageViewResource(R.id.status_bar_play, R.drawable.ic_play_arrow_white_24dp);
        }

        remoteView.setTextViewText(R.id.status_bar_track_name, t.getName());
        bigView.setTextViewText(R.id.status_bar_track_name, t.getName());
        remoteView.setTextViewText(R.id.status_bar_artist_name, t.getArtist());
        bigView.setTextViewText(R.id.status_bar_artist_name, t.getArtist());
        bigView.setTextViewText(R.id.status_bar_album_name, t.getAlbum());


        Intent previousIntent = new Intent(context, NotfificationHelper.class);
        previousIntent.putExtra("DO", "prev");
        PendingIntent ppreviousIntent = PendingIntent.getActivity(context, 0,
                previousIntent, 0);

        Intent playIntent = new Intent(context, NotfificationHelper.class);
        playIntent.putExtra("DO", "play");
        PendingIntent pplayIntent = PendingIntent.getActivity(context, 1,
                playIntent, 0);

        Intent nextIntent = new Intent(context, NotfificationHelper.class);
        nextIntent.putExtra("DO", "next");
        PendingIntent pnextIntent = PendingIntent.getActivity(context, 2,
                nextIntent, 0);

        Intent closeIntent = new Intent(context, NotfificationHelper.class);
        closeIntent.putExtra("DO", "stop");
        PendingIntent pcloseIntent = PendingIntent.getActivity(context, 3,
                closeIntent, 0);


        remoteView.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);
        bigView.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);


        remoteView.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);
        bigView.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);


        remoteView.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);
        bigView.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);

        remoteView.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);
        bigView.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);




        // Build Notification with Notification Manager
        notificationmanager.notify(0, builder.build());
    }

    public void DisableNotification(){
        notificationmanager.cancelAll();
    }


    public int getPos(){
        return position;
    }
    public int getFragmentId(){
        return FragmentId;
    }




}
