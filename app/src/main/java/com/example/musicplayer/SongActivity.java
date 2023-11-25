package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class SongActivity extends AppCompatActivity {
    TextView txtSongName;
    TextView txtSingerName;
    ImageButton imgbtnPlay ;
    ImageButton imgbtnNext ;
    ImageButton imgbtnPrev ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);
        SetUpView();
        Bundle bundle = getIntent().getExtras();
        String songName = bundle.getString("songName");
        String singerName = bundle.getString("singerName");
        boolean flag = bundle.getBoolean("flag");
        int currentIndex = bundle.getInt("currentIndex");

        txtSongName.setText(songName);
        txtSingerName.setText(singerName);
        imgbtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public void SetUpView() {
        txtSongName = findViewById(R.id.txtSongName);
        txtSingerName = findViewById(R.id.txtSingerName);
        imgbtnPlay = findViewById(R.id.imgbtnPlay);
        imgbtnNext = findViewById(R.id.imgbtnNext);
        imgbtnPrev = findViewById(R.id.imgbtnPrev);

    }
}