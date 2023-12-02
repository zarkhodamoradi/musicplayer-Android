package com.example.musicplayer;

import android.net.Uri;

public class song {
    private String songName;
    private String singerName;
    private int songId ;
    private String path ;
    private Uri songImageId;
    private String Duration ;


    public boolean running = false ;
    public song() {
    }

    public song(String songName, String singerName , String path ,String Duration ) {
        this.songName = songName;
        this.singerName = singerName;
        this.path = path ;
        this.Duration = Duration ;
        running = false ;

    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
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
