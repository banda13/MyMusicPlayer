package com.example.andy.mymusicplayer.soundtrack;

import com.orm.SugarRecord;

public class Track extends SugarRecord {
    String name;
    String artist;
    String path;
    String album;
    int size;
    String added;
    int duration;

    public Track() {
    }

    public Track(String name, String artist, String path, String album, int size, String added, int duration) {
        this.name = name;
        this.artist = artist;
        this.path = path;
        this.album = album;
        this.size = size;
        this.added = added;
        this.duration = duration;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }


    public String getName() {

        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getAdded() {
        return added;
    }

    public void setAdded(String added) {
        this.added = added;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
