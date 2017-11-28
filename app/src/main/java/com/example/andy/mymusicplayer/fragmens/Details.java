package com.example.andy.mymusicplayer.fragmens;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.andy.mymusicplayer.R;


public class Details extends Fragment {

    public static final String TITLE = "title";
    public static final String ARTIST = "artist";
    public static final String SIZE = "size";
    public static final String ALBUM = "album";
    public static final String DURATION = "duration";
    public static final String ADDED = "added";
    public static final String PATH = "path";

    private String title;
    private String artist;
    private int duration;
    private int size;
    private String album;
    private String added;
    private String path;


    private TextView text_title;
    private TextView text_artist;
    private TextView text_duration;
    private TextView text_added;
    private TextView text_path;
    private TextView text_album;
    private TextView text_size;


    @Deprecated
    public Details() {

    }

    public static android.support.v4.app.Fragment newInstance(String artist,
        String title, int duration, String added , String path ,int size, String album) {
        Details details = new Details();

        Bundle bundle = new Bundle();
        bundle.putString(ARTIST, artist);
        bundle.putString(TITLE, title);
        bundle.putInt(DURATION, duration);
        bundle.putString(ADDED, added);
        bundle.putString(PATH, path);
        bundle.putInt(SIZE, size);
        bundle.putString(ALBUM, album);

        details.setArguments(bundle);
        return details;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.details, null);
        title = getArguments().getString(TITLE);
        artist = getArguments().getString(ARTIST);
        duration = getArguments().getInt(DURATION);
        added = getArguments().getString(ADDED);
        path = getArguments().getString(PATH);
        album = getArguments().getString(ALBUM);
        size = getArguments().getInt(SIZE);

        resolve(rootView);



        text_title.setText("Title:\t\t"+title);
        text_artist.setText("Artist:\t\t"+artist);
        text_duration.setText("Duration:\t\t"+duration);
        text_added.setText("Added:\t\t"+added);
        text_path.setText("Path:\t\t"+path);
        text_album.setText("Album:\t\t"+album);
        text_size.setText("Size:\t\t"+size);

        return rootView;
    }

    public void resolve(View v){


        text_title = (TextView) v.findViewById(R.id.title);
        text_artist = (TextView) v.findViewById(R.id.artist);
        text_duration = (TextView) v.findViewById(R.id.duration);
        text_added = (TextView) v.findViewById(R.id.added);
        text_path = (TextView) v.findViewById(R.id.path);
        text_album = (TextView) v.findViewById(R.id.album);
        text_size = (TextView) v.findViewById(R.id.size);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
