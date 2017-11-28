package com.example.andy.mymusicplayer.soundtrack;

import java.util.Comparator;

/**
 * Created by Andy on 2016. 10. 19..
 */
public class TrackComparator implements Comparator<Track> {

    @Override
    public int compare(Track o1, Track o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
