package com.tzabochen.player.service;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import com.tzabochen.player.model.Song;

import java.util.ArrayList;

public class MusicService extends Service
        implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    // VALUES
    private MediaPlayer myMediaPlayer;
    private ArrayList<Song> songList;
    private int songCurrentPosition;

    private IBinder musicServiceBinder = new MusicServiceBinder();

    @Override
    public void onCreate() {
        super.onCreate();

        // INIT VALUES
        songCurrentPosition = 0;

        // CREATE & CONFIG PLAYER
        myMediaPlayer = new MediaPlayer();
        myMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        myMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        myMediaPlayer.setOnPreparedListener(this);
        myMediaPlayer.setOnCompletionListener(this);
        myMediaPlayer.setOnErrorListener(this);
    }

    public void setSongList(ArrayList<Song> songList) {
        this.songList = songList;
    }

    public void setSongCurrentPosition(int songPosition) {
        this.songCurrentPosition = songPosition;
    }

    public void playSong() {
        myMediaPlayer.reset();

        // SONG WITH LIST
        Song currentSong = songList.get(songCurrentPosition);

        // GET SONG ID
        long songId = currentSong.getId();

        // URI
        Uri trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songId);

        myMediaPlayer.prepareAsync();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicServiceBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        myMediaPlayer.stop();
        myMediaPlayer.release();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    // BINDER
    public class MusicServiceBinder extends Binder {

        public MusicService getMusicService() {
            return MusicService.this;
        }

    }
}
