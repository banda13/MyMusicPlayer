package com.example.andy.mymusicplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.andy.mymusicplayer.R;
import com.example.andy.mymusicplayer.fragmens.Details;
import com.example.andy.mymusicplayer.fragmens.Music_Played_Fragment;
import com.example.andy.mymusicplayer.musicplayer.MyMusicPlayer;
import com.example.andy.mymusicplayer.soundtrack.Track;

/**
 * Created by Andy on 2016. 10. 05..
 */
public class MusicActivity extends AppCompatActivity {

    private Fragment fragment;
    private MyMusicPlayer MPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_played);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        MPlayer = ListActivity.MPlayer;


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }



        int id = getIntent().getIntExtra(Music_Played_Fragment.KEY_ID, 0);
        int fragmentId = getIntent().getIntExtra(Music_Played_Fragment.FRAGMENT_KEY, 0);
        fragment = Music_Played_Fragment.newInstance(id, fragmentId);



        this.getSupportFragmentManager().beginTransaction()
                .add(R.id.music_detail_container, fragment)
                .commit();



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.music_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            NavUtils.navigateUpTo(this, new Intent(this, ListActivity.class));
            overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
            return true;
        }
        if(id == R.id.detail_menu){
            Track touched = ((Music_Played_Fragment) fragment).getPlayed();

            Fragment details = Details.newInstance(touched.getArtist(), touched.getName(),
                    touched.getDuration(), touched.getAdded(), touched.getPath(),
                    touched.getSize(), touched.getAlbum());

            FragmentManager fm = this.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

            ft.addToBackStack(null);

            ft.add(android.R.id.content, details);
            ft.commit();
        }
        return super.onOptionsItemSelected(item);
    }

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
