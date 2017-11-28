package com.example.andy.mymusicplayer.fragmens;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andy.mymusicplayer.R;
import com.example.andy.mymusicplayer.activity.ListActivity;
import com.example.andy.mymusicplayer.adapter.MusicAdapter;
import com.example.andy.mymusicplayer.musicplayer.MyMusicPlayer;
import com.example.andy.mymusicplayer.soundtrack.Track;
import com.example.andy.mymusicplayer.soundtrack.Utilities;

import java.util.ArrayList;

/**
 * Created by Andy on 2016. 10. 05..
 */
public class Music_Played_Fragment extends Fragment implements SeekBar.OnSeekBarChangeListener{


    public static final String KEY_ID = "ID";
    public static final String FRAGMENT_KEY = "Fragment_id";

    private ImageButton btn_play;
    private ImageButton btn_next;
    private ImageButton btn_prev;
    private ImageButton btn_shuffle;
    private ImageButton btn_repplay;
    private TextView musicName;
    private TextView currDuration;
    private TextView totDuration;
    private SeekBar processBar;
    private ImageView robot;
    private Animation pushAnim;
    // Handler to update UI timer, progress bar etc,.
    private Handler mHandler = new Handler();
    private MusicAdapter MAdapter;
    private MyMusicPlayer MPlayer;
    private Track played;

    private Utilities utils;
    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000; // 5000 milliseconds


    private ArrayList<Track> tracks;
    private ArrayList<Track> tmp_tracks;

    private boolean isShuffle = false;
    private boolean isRepeat = false;
    private boolean threadrun = false;
    //-> mert a progreszbar-os szál nem állt le amikor leállt az activity
    //->ne frisítse ha nem él az activity
    private int pos = 0;
    private int fragment_id = 0;


    @Deprecated
    public Music_Played_Fragment() {

    }

    public static android.support.v4.app.Fragment newInstance(int id, int fragment_id) {
        Music_Played_Fragment fragment = new Music_Played_Fragment();

        Bundle bundle = new Bundle();
        bundle.putInt(KEY_ID, id);
        bundle.putInt(FRAGMENT_KEY, fragment_id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    public Track getPlayed(){
        return played;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.music_played, container, false);

        btn_play = (ImageButton) rootView.findViewById(R.id.play_button);
        btn_next = (ImageButton) rootView.findViewById(R.id.right_button);
        btn_prev = (ImageButton) rootView.findViewById(R.id.left_button);
        btn_repplay = (ImageButton) rootView.findViewById(R.id.replay_btn);
        btn_shuffle = (ImageButton) rootView.findViewById(R.id.shuffle_btn);
        processBar = (SeekBar) rootView.findViewById(R.id.duration_bar);
        totDuration = (TextView) rootView.findViewById(R.id.TotalDuration);
        currDuration = (TextView) rootView.findViewById(R.id.CurrentDuration);
        musicName = (TextView) rootView.findViewById(R.id.sound_name);
        robot = (ImageView) rootView.findViewById(R.id.imageView);

        btn_play.setOnClickListener(play_pause_listener);
        btn_next.setOnClickListener(next_back_listener);
        btn_prev.setOnClickListener(next_back_listener);
        btn_shuffle.setOnClickListener(shuffleListener);
        btn_repplay.setOnClickListener(replayListener);

        utils = new Utilities();
        robot.setImageResource(R.drawable.myicon2);
        robot.setMinimumHeight(100);
        robot.setMinimumWidth(100);

        MAdapter = ListActivity.MAdapter;
        MPlayer = ListActivity.MPlayer;

        processBar.setOnSeekBarChangeListener(this); // Important
        pos = getArguments().getInt(KEY_ID);
        fragment_id = getArguments().getInt(FRAGMENT_KEY);

        if (!MAdapter.getTmpList().isEmpty()) {
            tmp_tracks = MAdapter.getTmpList();
            MPlayer.setTmpTracks(tmp_tracks);
        }

        if (fragment_id == 0) {
            //Toast.makeText(rootView.getContext(), "Sorted List", Toast.LENGTH_SHORT).show();
            tracks = MAdapter.getSortedTracks();

        }
        else if (fragment_id == 1) {
            //Toast.makeText(rootView.getContext(), "Date Sorted", Toast.LENGTH_SHORT).show();
            tracks = MAdapter.getSortedDateList();

        }
        else if (fragment_id == 2) {
            //Toast.makeText(rootView.getContext(), "Tmp_list", Toast.LENGTH_SHORT).show();
            tracks = MAdapter.getTmpList();
        }
        else{
            Toast.makeText(rootView.getContext(), "Error", Toast.LENGTH_SHORT).show();
        }

        MPlayer.setFragment(this);
        MPlayer.setFragmentId(fragment_id);

        pushAnim =
                AnimationUtils.loadAnimation(
                        rootView.getContext(), R.anim.rotate);

        pushAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        if(MPlayer.setTracks(tracks)) {
            MPlayer.setPosition(pos);
        }

        return rootView;
    }


    public void playSong(Track track) {
        // Play song

        threadrun = true;
        // Displaying Song title
        String songName = track.getName();
        played = track;
        musicName.setText(songName);

        // Changing Button Image to pause image
        btn_play.setImageResource(R.drawable.ic_pause_black_24dp);

        // set Progress bar values
        processBar.setProgress(0);
        processBar.setMax(100);

        // Updating progress bar
        updateProgressBar();

    }




    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }


    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if (threadrun) {
                long totalDuration = MPlayer.getDuration();
                long currentDuration = MPlayer.getCurrPosition();
                totDuration.setText("" + utils.milliSecondsToTimer(totalDuration));
                // Displaying time completed playing
                currDuration.setText("" + utils.milliSecondsToTimer(currentDuration));

                // Updating progress bar
                int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
                //Log.d("Progress", ""+progress);
                processBar.setProgress(progress);

                // Running this thread after 100 milliseconds
                mHandler.postDelayed(this, 100);
            }
        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // remove message Handler from updating progress bar
        mHandler.removeCallbacks(mUpdateTimeTask);
    }


    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = (int) MPlayer.getDuration();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        MPlayer.seek(currentPosition);

        // update timer progress again
        updateProgressBar();
    }


    View.OnClickListener play_pause_listener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            // check for already playing
            if (MPlayer.playing()) {
                if (MPlayer != null) {
                    MPlayer.pause();
                    // Changing button image to play button
                    btn_play.setImageResource(R.drawable.ic_play_arrow_black_24dp);

                }
            } else {
                // Resume song
                if (MPlayer != null) {
                    MPlayer.resume();
                    // Changing button image to pause button
                    btn_play.setImageResource(R.drawable.ic_pause_black_24dp);
                    robot.startAnimation(pushAnim);
                }
            }

        }
    };

    View.OnClickListener next_back_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.left_button) { //prev
                MPlayer.previous();
            } else { //next

                MPlayer.next();
            }
        }
    };
    View.OnClickListener replayListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isRepeat) {
                isRepeat = false;
                MPlayer.setRepeat(false);
                Toast.makeText(getContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
                btn_repplay.setImageResource(R.drawable.ic_replay_black_24dp);
            } else {
                // make repeat to true
                isRepeat = true;
                Toast.makeText(getContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
                MPlayer.setRepeat(true);
                // make shuffle to false
                isShuffle = false;
                MPlayer.setShuffle(false);
                btn_repplay.setImageResource(R.drawable.ic_replay_white_24dp);
                btn_shuffle.setImageResource(R.drawable.ic_shuffle_black_24dp);
            }
        }
    };
    View.OnClickListener shuffleListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isShuffle) {
                isShuffle = false;
                MPlayer.setShuffle(false);
                Toast.makeText(getContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
                btn_shuffle.setImageResource(R.drawable.ic_shuffle_black_24dp);
            } else {
                // make repeat to true
                isShuffle = true;
                MPlayer.setShuffle(true);
                Toast.makeText(getContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
                // make shuffle to false
                isRepeat = false;
                MPlayer.setRepeat(false);
                btn_shuffle.setImageResource(R.drawable.ic_shuffle_white_24dp);
                btn_repplay.setImageResource(R.drawable.ic_replay_black_24dp);
            }
        }
    };

    public void setThreadrun(boolean t){
        threadrun = t;
    }

    public void setPlayButtonState(boolean isPlaying){
        if(isPlaying){
            btn_play.setImageResource(R.drawable.ic_pause_black_24dp);
        }
        else{
            btn_play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MPlayer.removeFragment();
        threadrun = false;
    }

}