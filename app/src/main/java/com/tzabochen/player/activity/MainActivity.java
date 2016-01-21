package com.tzabochen.player.activity;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.tzabochen.player.R;
import com.tzabochen.player.adapter.SongAdapter;
import com.tzabochen.player.model.Song;
import com.tzabochen.player.service.MusicService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // VALUES
    private ArrayList<Song> songList;
    private ListView songListView;

    // SERVICE
    private ServiceConnection myMusicServiceConnection;
    private MusicService myMusicService;
    private MusicService.MusicServiceBinder myMusicServiceBinder;
    private boolean myMusicServiceBound = false;
    private Intent myMusicServiceIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // FIND VIEW
        songListView = (ListView) findViewById(R.id.song_list_view);

        // GET SONG LIST
        this.songList = new ArrayList<>();
        getSongList();

        // SET ADAPTER
        SongAdapter songAdapter = new SongAdapter(getApplicationContext(), songList);
        songListView.setAdapter(songAdapter);

        // GET MUSIC SERVICE
        myMusicServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myMusicServiceBinder = (MusicService.MusicServiceBinder) service;
                myMusicService = myMusicServiceBinder.getMusicService();
                myMusicService.setSongList(songList);
                myMusicServiceBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                myMusicServiceBound = false;
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(myMusicServiceIntent == null){
            myMusicServiceIntent = new Intent(this, MusicService.class);
            bindService(myMusicServiceIntent, myMusicServiceConnection, Context.BIND_AUTO_CREATE);
            startService(myMusicServiceIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.menu_item_shuffle):
                break;
            case (R.id.menu_item_close):
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getSongList() {

        // GET DATA
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            // GET COLUMNS INDEX
            int columnId = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int columnTitle = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int columnArtist = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);

            // CREATE SONG LIST
            do {
                long songId = musicCursor.getLong(columnId);
                String songTitle = musicCursor.getString(columnTitle);
                String songArtist = musicCursor.getString(columnArtist);
                songList.add(new Song(songId, songTitle, songArtist));
            } while (musicCursor.moveToNext());
        }

        if (musicCursor != null) {
            musicCursor.close();
        }
    }

    public void songPosition(View view){
        myMusicService.setSongCurrentPosition(Integer.parseInt(view.getTag().toString()));
        myMusicService.playSong();
    }


}
