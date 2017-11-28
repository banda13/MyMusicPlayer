package com.example.andy.mymusicplayer.soundtrack;

import java.util.Comparator;

import static java.lang.Integer.parseInt;

/**
 * Created by Andy on 2016. 11. 13..
 */
public class TrackDateComparator implements Comparator<Track> {
    @Override
    public int compare(Track o1, Track o2) {
        int o1Added = parseInt(o1.getAdded());
        int o2Added = parseInt(o2.getAdded());
        if(o1Added > o2Added){
            return -1;
        }
        else{
            return 1;
        }
    }
}
