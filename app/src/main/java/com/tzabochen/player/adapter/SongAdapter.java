package com.tzabochen.player.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tzabochen.player.R;
import com.tzabochen.player.model.Song;

import java.util.ArrayList;

public class SongAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Song> songList;
    private LayoutInflater layoutInflater;

    public SongAdapter(Context context, ArrayList<Song> songList) {
        this.context = context;
        this.songList = songList;
    }

    @Override
    public int getCount() {
        return songList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // GET INFLATER
        this.layoutInflater = LayoutInflater.from(context);

        // GET LAYOUT
        RelativeLayout relativeLayout = (RelativeLayout) layoutInflater.inflate(R.layout.item_song, parent, false);

        // GET VIEWS WITH LAYOUT
        TextView songTitle = (TextView) relativeLayout.findViewById(R.id.item_song_title);
        TextView songArtist = (TextView) relativeLayout.findViewById(R.id.item_song_artist);

        // SET FIND VIEWS
        Song currentSong = songList.get(position);
        songTitle.setText(currentSong.getTitle());
        songArtist.setText(currentSong.getArtist());

        // TAG -> POSITION
        relativeLayout.setTag(position);

        // RETURN
        return relativeLayout;
    }
}
