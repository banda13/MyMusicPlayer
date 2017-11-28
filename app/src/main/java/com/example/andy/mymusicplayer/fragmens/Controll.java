package com.example.andy.mymusicplayer.fragmens;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andy.mymusicplayer.R;
import com.example.andy.mymusicplayer.activity.ListActivity;
import com.example.andy.mymusicplayer.activity.MusicActivity;
import com.example.andy.mymusicplayer.musicplayer.MyMusicPlayer;

/**
 * Created by Andy on 2016. 11. 11..
 */
public class Controll extends Fragment {

    private FragmentActivity myContext;
    private MyMusicPlayer MPlayer;
    private FloatingActionButton playpause;
    private FloatingActionButton prev;
    private FloatingActionButton next;
    private ImageView pic;
    private TextView title;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MPlayer = ListActivity.MPlayer;
        MPlayer.setControll_fragment(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.controll_panel, null);

        initbuttons(rootView);

        setPlayPause();

        playpause.setOnClickListener(PlayListener);
        next.setOnClickListener(NextListener);
        prev.setOnClickListener(PrevListener);

        pic.setOnClickListener(ImageListener);
        setText(MPlayer.getName());

        return rootView;
    }

    @Override
    public void onAttach(Activity activity){
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    public void initbuttons(View v){

        playpause = (FloatingActionButton) v.findViewById(R.id.play_btn);
        next = (FloatingActionButton) v.findViewById(R.id.next_btn);
        prev = (FloatingActionButton) v.findViewById(R.id.prev_btn);

        pic = (ImageView) v.findViewById(R.id.logo);
        title = (TextView) v.findViewById(R.id.played_title);
    }

    View.OnClickListener PlayListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            MPlayer.playorpause();
            setPlayPause();
        }
    };


    View.OnClickListener NextListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            MPlayer.next();
            setText(MPlayer.getName());
        }
    };


    View.OnClickListener PrevListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            MPlayer.previous();
            setText(MPlayer.getName());
        }
    };


    View.OnClickListener ImageListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(myContext, MusicActivity.class);
            intent.putExtra(Music_Played_Fragment.KEY_ID, MPlayer.getPos());
            intent.putExtra(Music_Played_Fragment.FRAGMENT_KEY, MPlayer.getFragmentId());
            myContext.startActivity(intent);
        }
    };

    public void setPlayPause(){
        if(MPlayer.playing()) {
            playpause.setImageResource(R.drawable.ic_pause_black_24dp);
        }
        else{
            playpause.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        }
    }
    public void setText(String name){
        title.setText(name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MPlayer.removeControll_fragment();
    }
}
