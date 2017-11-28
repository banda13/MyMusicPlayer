package com.example.andy.mymusicplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andy.mymusicplayer.R;
import com.example.andy.mymusicplayer.activity.ListActivity;
import com.example.andy.mymusicplayer.activity.MusicActivity;
import com.example.andy.mymusicplayer.fragmens.Music_Played_Fragment;
import com.example.andy.mymusicplayer.fragmens.SettingsFragment;
import com.example.andy.mymusicplayer.soundtrack.Track;

import java.util.ArrayList;
import java.util.List;


public class ItemRecycleView
        extends RecyclerView.Adapter<ItemRecycleView.ViewHolder> {

    private List<Track> trackList;
    private MusicAdapter MAdapter;
    private FragmentManager fragmentManaget;
    private int id;

    public ItemRecycleView(FragmentManager fragmentManager, int i, ArrayList<Track> tracks) {
        MAdapter = ListActivity.MAdapter;
        if (MAdapter.getSortedTracks().isEmpty()) {
                Log.d("endy", "Empty List");
        } else {
            if (i == 0) {
                trackList = MAdapter.getSortedTracks();
            } else if (i == 1) {
                trackList = MAdapter.getSortedDateList();
            } else if (i == 2) {
                trackList = MAdapter.getTmpList();
            } else {
                trackList = tracks;
            }
        }
        fragmentManaget = fragmentManager;
        id = i;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_content, parent, false);


        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTarck = trackList.get(position);
        holder.name.setText(trackList.get(position).getName());
        holder.artist.setText(trackList.get(position).getArtist());
        holder.sound.setImageResource(R.drawable.musical_notes_1_music_note_clipart);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Context context = v.getContext();
                if (id != 4) {
                    Intent intent = new Intent(context, MusicActivity.class);
                    intent.putExtra(Music_Played_Fragment.KEY_ID, position);
                    intent.putExtra(Music_Played_Fragment.FRAGMENT_KEY, id);

                    context.startActivity(intent);
                } else {
                    String path = trackList.get(position).getPath();
                    int i = MAdapter.getTrackIdByPath(path);
                    Intent intent = new Intent(context, MusicActivity.class);
                    intent.putExtra(Music_Played_Fragment.KEY_ID, i);
                    intent.putExtra(Music_Played_Fragment.FRAGMENT_KEY, 0);

                    context.startActivity(intent);
                }
            }
        });
        if (id != 4) {
            holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    SettingsFragment stf = new SettingsFragment();
                    Bundle bundle = new Bundle();

                    bundle.putInt(SettingsFragment.POS_KEY, position);
                    bundle.putInt(SettingsFragment.F_KEY, id);
                    stf.setArguments(bundle);

                    stf.show(fragmentManaget, SettingsFragment.TAG);
                    return true;
                }
            });

        }
    }

    public void refresh() {
        notifyDataSetChanged();

    }


    @Override
    public int getItemCount() {
        if (trackList != null) {
            return trackList.size();
        } else {
            return 0;
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView name;
        public final TextView artist;
        public final ImageView sound;
        public Track mTarck;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            name = (TextView) view.findViewById(R.id.sound_name);
            artist = (TextView) view.findViewById(R.id.sound_artist);
            sound = (ImageView) view.findViewById(R.id.sound_img);
        }
    }


}