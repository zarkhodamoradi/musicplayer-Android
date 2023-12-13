package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextClock;
import android.widget.TextView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class SongActivity extends AppCompatActivity {
    TextView txtSongName;
    TextView txtSingerName;
    ImageButton imgbtnPlay;
    ImageButton imgbtnNext;
    ImageButton imgbtnPrev;
    SeekBar seekBar;
    Handler handler = new Handler();


    TextClock textClockDuration;
    TextClock textClockLeftDuration ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);
        SetUpView();


        seekBar.setMax(MainActivity.mediaPlayer.getDuration());
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                seekBar.setProgress(MainActivity.mediaPlayer.getCurrentPosition());

                textClockDuration.setText(DateUtils.formatElapsedTime(MainActivity.mediaPlayer.getCurrentPosition()/1000) );
                textClockLeftDuration.setText(DateUtils.formatElapsedTime((MainActivity.mediaPlayer.getDuration()-MainActivity.mediaPlayer.getCurrentPosition())/1000));

            }
        }, 0, 200);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                    MainActivity.mediaPlayer.seekTo(progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        if (MainActivity.flag) {
            imgbtnPlay.setImageResource(R.drawable.baseline_pause_circle_outline_24);
//    UpdateDuration();
        } else imgbtnPlay.setImageResource(R.drawable.baseline_play_circle_24);
        txtSongName.setText(MainActivity.SongsList.get(MainActivity.currentIndex).getSongName());
        txtSingerName.setText(MainActivity.SongsList.get(MainActivity.currentIndex).getSingerName());
        imgbtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.flag == false) {
                    if (MainActivity.mediaPlayer != null) {
                        MainActivity.mediaPlayer.start();
                    } else {

                        //  mediaPlayer = new MediaPlayer();
                        try {
                            MainActivity.mediaPlayer.setDataSource(MainActivity.path);
                            MainActivity.mediaPlayer.prepare();
                            MainActivity.mediaPlayer.start();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    MainActivity.imgbtnPlay.setImageResource(android.R.drawable.ic_media_pause);
                    imgbtnPlay.setImageResource(R.drawable.baseline_pause_circle_outline_24);
                    MainActivity.flag = true;
//                    UpdateDuration();
                } else {
                    if (MainActivity.mediaPlayer != null && MainActivity.mediaPlayer.isPlaying()) {
                        MainActivity.mediaPlayer.pause();
                        MainActivity.imgbtnPlay.setImageResource(android.R.drawable.ic_media_play);
                        imgbtnPlay.setImageResource(R.drawable.baseline_play_circle_24);
                        MainActivity.flag = false;
                    }
                }

                MainActivity.adapter.notifyDataSetChanged();
            }
        });

        imgbtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.SongsList.get(MainActivity.currentIndex).running = false;
                if (MainActivity.currentIndex < MainActivity.SongsList.size() - 1) {
                    MainActivity.currentIndex++;
                    MainActivity.SongsList.get(MainActivity.currentIndex).running = true;
                    MainActivity.playSong(MainActivity.SongsList.get(MainActivity.currentIndex).getPath());

                    if (MainActivity.flag == false) {
                        MainActivity.flag = true;
                        MainActivity.imgbtnPlay.setImageResource(android.R.drawable.ic_media_pause);

                        imgbtnPlay.setImageResource(R.drawable.baseline_pause_circle_outline_24);
                    }
                    txtSongName.setText(MainActivity.SongsList.get(MainActivity.currentIndex).getSongName());
                    txtSingerName.setText(MainActivity.SongsList.get(MainActivity.currentIndex).getSingerName());
                    MainActivity.adapter.notifyDataSetChanged();
                }
            }
        });

        imgbtnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.SongsList.get(MainActivity.currentIndex).running = false;
                if (MainActivity.currentIndex > 0) {
                    MainActivity.currentIndex--;
                    MainActivity.SongsList.get(MainActivity.currentIndex).running = true;
                    MainActivity.playSong(MainActivity.SongsList.get(MainActivity.currentIndex).getPath());
                    if (MainActivity.flag == false) {
                        MainActivity.flag = true;
                        MainActivity.imgbtnPlay.setImageResource(android.R.drawable.ic_media_pause);

                        imgbtnPlay.setImageResource(R.drawable.baseline_pause_circle_outline_24);
                    }
                    txtSongName.setText(MainActivity.SongsList.get(MainActivity.currentIndex).getSongName());
                    txtSingerName.setText(MainActivity.SongsList.get(MainActivity.currentIndex).getSingerName());
                    MainActivity.adapter.notifyDataSetChanged();
                }
            }
        });

    }

    public void SetUpView() {
        txtSongName = findViewById(R.id.txtSongName);
        txtSingerName = findViewById(R.id.txtSingerName);
        imgbtnPlay = findViewById(R.id.imgbtnPlay);
        imgbtnNext = findViewById(R.id.imgbtnNext);
        imgbtnPrev = findViewById(R.id.imgbtnPrev);
        seekBar = findViewById(R.id.seekBar);


textClockDuration = findViewById(R.id.textClockDuration);
textClockLeftDuration = findViewById(R.id.textClockLeftDuration);
    }
//    public  void UpdateDuration() {
//
//        try {
//            int totalDuration = MainActivity.mediaPlayer.getDuration();
//            seekBar.setMax(totalDuration);
//
//
//            Thread thread = new Thread(new Runnable() {
//
//                @Override
//                public void run() {
//
//                    while (MainActivity.currentDuration[0] < totalDuration) {
//
//                      handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (MainActivity.mediaPlayer.isPlaying()) {
//                                    MainActivity.currentDuration[0] = MainActivity.mediaPlayer.getCurrentPosition();
//                                    seekBar.setProgress(MainActivity.currentDuration[0]);
//                                }
//                            }
//                        });
//                        try {
//                            Thread.sleep(50);
//                        } catch (InterruptedException e) {
//                            throw new RuntimeException(e);
//                        }
//
//
//                    }
//                }
//            });
//
//
//            thread.start();
//        }catch (Exception ex){}
//
//    }

}