package com.example.musicplayer;

import android.net.Uri;

public class song {
    private String songName;
    private String singerName;
    private int songId ;
    private String path ;
    private Uri songImageId;


    public boolean running = false ;
    public song() {
    }

    public song(String songName, String singerName , String path) {
        this.songName = songName;
        this.singerName = singerName;
        this.path = path ;
        running = false ;

    }


    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public Uri getSongImageId() {
        return songImageId;
    }

    public void setSongImageId(Uri songImageId) {
        this.songImageId = songImageId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
