package com.example.andy.mymusicplayer.adapter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.Toast;

import com.example.andy.mymusicplayer.soundtrack.Track;
import com.example.andy.mymusicplayer.soundtrack.TrackComparator;
import com.example.andy.mymusicplayer.soundtrack.TrackDateComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Andy on 2016. 10. 26..
 */
public class MusicAdapter {



    private ArrayList<Track> tracks;
    private ArrayList<Track> sortedDateTracks;
    private ArrayList<Track> Tmptracks;
    private Context context;


    public MusicAdapter(Activity a) {

        tracks = new ArrayList<>();
        Tmptracks = new ArrayList<>();
        sortedDateTracks = new ArrayList<>();
        context = a;

    }
    public boolean init(){
        boolean ok = false;
        while(!ok) {

            ok=readDatafromSdcard();
        }
        loadItemsInBackground();
        return true;
    }


    public void addTmp(Track ujTrack) {
        if(!Tmptracks.contains(ujTrack)) {
            Tmptracks.add(ujTrack);
            ujTrack.save();
        }
    }

    public void deleteTmp(Track szam){
        szam.delete();
        Tmptracks.remove(szam);
    }


    public ArrayList<Track> getSortedTracks() {
        return tracks;
    }

    public ArrayList<Track> getTmpList() {
        return Tmptracks;
    }

    public ArrayList<Track> getSortedDateList(){
        return sortedDateTracks;
    }

    public void delete(Track t)
    {
        Uri rootUri = MediaStore.Audio.Media.getContentUriForPath(t.getPath());
        tracks.remove(t);
        int i = context.getContentResolver().delete( rootUri,
                MediaStore.MediaColumns.DATA + " LIKE ? ", new String[]{t.getPath()} );
        File f = new File(t.getPath());
        f.delete();
        Toast.makeText(context, "A törölt sorok száma:"+ i, Toast.LENGTH_SHORT).show();
    }
    public boolean deleteTmps(){
        if(Tmptracks.isEmpty()){
            return false;
        }
        else {
            List<Track> savedtracks = Track.listAll(Track.class);
            Track.deleteAll(Track.class);
            Tmptracks.clear();
            return true;
        }
    }
    public int getTrackIdByPath(String path){
        for(int i= 0; i<tracks.size(); i++){
            if(tracks.get(i).getPath().equals(path)){
                return i;
            }
        }
        return 0;
    }


    private void loadItemsInBackground() {
        new AsyncTask<Void, Void, List<Track>>() {

            @Override
            protected List<Track> doInBackground(Void... voids) {
                    return Track.listAll(Track.class);
            }

            @Override
            protected void onPostExecute(List<Track> tracklist) {
                if(tracklist.isEmpty()){
                    Tmptracks = new ArrayList<>();
                }
                else {
                    super.onPostExecute(tracklist);
                    if(!tracklist.isEmpty()) {
                        Tmptracks = new ArrayList<>(tracklist);
                    }
                }
            }
        }.execute();
    }

    private boolean readDatafromSdcard() {



        ArrayList<Track> tracklist = new ArrayList<>();

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        ContentResolver cr = context.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = cr.query(uri, null, selection, null, null);

        assert cursor != null;
        while (cursor.moveToNext()) {
            String name = cursor.getString(2);
            String end = name.substring(name.length() - 4, name.length());
            //String title = name.substring(0, name.length() - 4);
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            //String path = cursor.getString(1);
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
            String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
            int size = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
            String added = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED));
            int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

            if (end.equals(".mp3") || end.equals(".MP3")) {
                Track ujTrack = new Track(title, artist, path, album, size, added, duration);
                tracklist.add(ujTrack);
            }
        }
        cursor.close();

        if(tracklist.isEmpty()){
            tracks = new ArrayList<>();
            sortedDateTracks = new ArrayList<Track>();
            return true;
        }
        else {
            Collections.sort(tracklist, new TrackComparator());
            tracks = new ArrayList<>(tracklist);
            Collections.sort(tracklist, new TrackDateComparator());
            sortedDateTracks = new ArrayList<>(tracklist);
            return true;
        }
    }

}
