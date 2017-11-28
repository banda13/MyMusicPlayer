package com.example.andy.mymusicplayer.fragmens;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.andy.mymusicplayer.R;
import com.example.andy.mymusicplayer.activity.ListActivity;
import com.example.andy.mymusicplayer.adapter.ItemRecycleView;
import com.example.andy.mymusicplayer.soundtrack.Track;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

/**
 * Created by Andy on 2016. 10. 04..
 */
public class Music extends Fragment {


    private FragmentActivity myContext;
    private ArrayList<Track> tracks;
    private ItemRecycleView RecycleAdapter;

    private VerticalRecyclerViewFastScroller fastScroller;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.music, null);
        fastScroller = (VerticalRecyclerViewFastScroller) rootView.findViewById(R.id.fast_scroller);


        View recyclerView = rootView.findViewById(R.id.music_list);
        assert recyclerView != null;

        tracks = ListActivity.MAdapter.getSortedTracks();

        setupRecyclerView((RecyclerView) recyclerView);

        RecycleAdapter = (ItemRecycleView) ((RecyclerView) recyclerView).getAdapter();
        RecycleAdapter.refresh();



        return rootView;

    }

    public ItemRecycleView getRecycleAdapter(){
        return RecycleAdapter;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {


        recyclerView.setAdapter(new ItemRecycleView(myContext.getSupportFragmentManager(),0, tracks));

        // Connect the recycler to the scroller (to let the scroller scroll the list)
        fastScroller.setRecyclerView(recyclerView);
        recyclerView.setItemAnimator(new SlideInLeftAnimator());
        recyclerView.getItemAnimator().setRemoveDuration(1000);

        // Connect the scroller to the recycler (to let the recycler scroll the scroller's handle)
        recyclerView.setOnScrollListener(fastScroller.getOnScrollListener());
    }



    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

}
