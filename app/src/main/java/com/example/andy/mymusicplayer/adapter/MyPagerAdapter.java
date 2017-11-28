package com.example.andy.mymusicplayer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.example.andy.mymusicplayer.fragmens.Music;
import com.example.andy.mymusicplayer.fragmens.MyPlayLists;
import com.example.andy.mymusicplayer.fragmens.PlayLists;

public class MyPagerAdapter  extends FragmentPagerAdapter {
    private MusicAdapter Madapter;
    private int pages;
    private Music m;
    private PlayLists playM;
    private MyPlayLists MplayM;

    public MyPagerAdapter(FragmentManager fm, MusicAdapter MAdapter) {
        super(fm);
        pages = 3;
        Madapter = MAdapter;
        m = new Music();
        playM = new PlayLists();
        MplayM = new MyPlayLists();
    }

    @Override
    public Fragment getItem(int pos) {
        switch (pos) {
            case 0:{
                Log.i("MYTAG", "music");
                return m;
            }
            case 1: {
                Log.i("MYTAG", "playList");
                return playM;
            }
            case 2:{
                Log.i("MYTAG", "Myplaylist");
                return MplayM;
            }
            default:{
                Log.i("MYTAG", "music default");
                return m;
            }
        }
    }
    public void refresh(){
        if(m.getRecycleAdapter() != null){
        m.getRecycleAdapter().notifyDataSetChanged();}
        if(MplayM.getRecycleAdapter() != null){
        MplayM.getRecycleAdapter().notifyDataSetChanged();}
        if(playM.getRecycleAdapter() != null){
        playM.getRecycleAdapter().notifyDataSetChanged();}
    }

    @Override
    public int getCount() {
        return pages;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Music";
            case 1:
                return "PlayLists";
            case 2:
                return "MyPlayLists";
            default:
                return "Music";
        }
    }

}
