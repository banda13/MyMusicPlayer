package com.example.andy.mymusicplayer.activity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.example.andy.mymusicplayer.R;
import com.example.andy.mymusicplayer.adapter.MusicAdapter;
import com.example.andy.mymusicplayer.adapter.MyPagerAdapter;
import com.example.andy.mymusicplayer.fragmens.Controll;
import com.example.andy.mymusicplayer.fragmens.Details;
import com.example.andy.mymusicplayer.fragmens.SettingsFragment;
import com.example.andy.mymusicplayer.musicplayer.MyMusicPlayer;
import com.example.andy.mymusicplayer.soundtrack.Track;

public class ListActivity extends AppCompatActivity implements SettingsFragment.OptionsFragmentInterface {


    public static MusicAdapter MAdapter;
    public static MyMusicPlayer MPlayer;
    private LocationManager locMan;
    private Track touched;
    private Controll panel;

    private MyPagerAdapter pageradapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);


       if(MAdapter == null){
           MAdapter = new MusicAdapter(this);
           MAdapter.init();

       }
        pageradapter = new MyPagerAdapter(getSupportFragmentManager(), MAdapter);




        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pageradapter);
        viewPager.setPageTransformer(true, new RotateUpTransformer());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        locMan = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


    }



    @Override
    public void onResume() {
        super.onResume();
        if (MPlayer == null) {
            MPlayer = new MyMusicPlayer(this);
        }
        else if(panel == null && MPlayer.getTrack() != null){
            panel = new Controll();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            ft.add(R.id.rejtett_layout, panel);
            ft.commit();
        }
        MPlayer.regist(this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.Refresh){
            Toast.makeText(ListActivity.this, "Refreshed", Toast.LENGTH_SHORT).show();
            pageradapter.refresh();
        }
        if(id == R.id.DeleteTmp){
            showAlertDialog("If ok, u cant restore the list", 1);
        }
        if(id == R.id.Search){
            Intent intent = new Intent(ListActivity.this, SearchActivity.class);
            overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
            startActivity(intent);

        }
        if(id == R.id.About){
            showPopUp(findViewById(R.id.toolbar));
        }
        if(id == R.id.Settings){
            Intent intent = new Intent(ListActivity.this, SettingsActivity.class);
            this.startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    public void onOptionsFragmentResult(String result, int pos, int fragment) {
        touched = null;
        if (fragment == 0) {
            touched = MAdapter.getSortedTracks().get(pos);
        }
        if (fragment == 1) {
            touched = MAdapter.getSortedDateList().get(pos);
        }
        if (fragment == 2) {
            touched = MAdapter.getTmpList().get(pos);
        }


        if (result.equals("Next Song")) {
            MAdapter.addTmp(touched);
            MPlayer.addTmp(touched);
            Snackbar.make(findViewById(android.R.id.content),
                    "Realy want this next?", Snackbar.LENGTH_LONG)
                    .setAction("Undo", mOnClickListener).show();
        }

        if (result.equals("Details")) {
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
        if(result.equals("Delete")){
            showAlertDialog("U realy want to delete this: "+ touched.getName(), 0);
        }
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MAdapter.deleteTmp(touched);
        }
    };



    @Override
    public void onPause() {
        MPlayer.unregist(this);
        super.onPause();
    }

    private void showAlertDialog(String msg, int kapcsolo) {
        final AlertDialog.Builder alertbox =
                new AlertDialog.Builder(this, R.style.YourAlertDialogTheme);
        alertbox.setTitle("Are u sure?");
        alertbox.setMessage(msg);

        if(kapcsolo == 0) {
            alertbox.setNeutralButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0,
                                            int arg1) {
                            MAdapter.delete(touched);
                            pageradapter.refresh();
                            Toast.makeText(ListActivity.this, "The track was deleted", Toast.LENGTH_SHORT).show();

                        }
                    });
            alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(ListActivity.this, "The delete was canceled", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if(kapcsolo == 1) {
            alertbox.setNeutralButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0,
                                            int arg1) {

                            if(!MAdapter.deleteTmps()){
                                Toast.makeText(ListActivity.this, "The List was already empty", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(ListActivity.this, "The List was deleted", Toast.LENGTH_SHORT).show();
                            }



                        }
                    });
            alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(ListActivity.this, "Delete canceled", Toast.LENGTH_SHORT).show();
                }
            });
        }

        alertbox.show();
    }

    private void showPopUp(View v) {
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.about_popup, null);
        final PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView myMsg = (TextView) popupView.findViewById(R.id.MyMessage);
        myMsg.setText("Created by: Endy!! \n I hope u enjoy it! ;)");

        Button button = (Button) popupView.findViewById(R.id.exit_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

    }


}
