package com.example.andy.mymusicplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.andy.mymusicplayer.R;
import com.example.andy.mymusicplayer.adapter.ItemRecycleView;
import com.example.andy.mymusicplayer.musicplayer.MyMusicPlayer;
import com.example.andy.mymusicplayer.soundtrack.Track;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private ArrayList<Track> tracks;
    private ArrayList<Track> mytracks;
    private SwipeRefreshLayout refresh;
    private ItemRecycleView RecycleAdapter;
    private EditText searching;
    private View recyclerView;
    private MyMusicPlayer MPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MPlayer = ListActivity.MPlayer;

        recyclerView = findViewById(R.id.music_list);
        assert recyclerView != null;

        tracks = ListActivity.MAdapter.getSortedTracks();
        mytracks = new ArrayList<>();

        setupRecyclerView((RecyclerView) recyclerView, tracks);

        RecycleAdapter = (ItemRecycleView) ((RecyclerView) recyclerView).getAdapter();

        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RecycleAdapter.refresh();
                refresh.setRefreshing(false);
            }
        });

        searching = (EditText) findViewById(R.id.etName);

        searching.addTextChangedListener(textWatcher);


    }
    private void setupRecyclerView(RecyclerView recyclerView, ArrayList<Track> t) {


        recyclerView.setAdapter(new ItemRecycleView(getSupportFragmentManager(), 4, t));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            NavUtils.navigateUpTo(this, new Intent(this, ListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void afterTextChanged(Editable s) {
            String txt = searching.getText().toString();
            mytracks = new ArrayList<>();
            for(int i=0; i<tracks.size() ;i++){
                if(tracks.get(i).getName().toUpperCase().contains(txt.toUpperCase())){
                    mytracks.add(tracks.get(i));
                }
            }
            if(txt.matches("")) {
                setupRecyclerView((RecyclerView) recyclerView, tracks);
            }
            else{
                setupRecyclerView((RecyclerView) recyclerView, mytracks);
            }
        }

    };

    @Override
    public void onResume(){
        super.onResume();
        MPlayer.regist(this);
    }

    @Override
    public void onPause() {
       MPlayer.unregist(this);
        super.onPause();
    }

}
