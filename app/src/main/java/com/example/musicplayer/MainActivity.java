package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listViewSongs;
    ImageButton imgbtnPlay;
    ImageButton imgbtnNext;
    ImageButton imgbtnPrev;
    MediaPlayer mediaPlayer;
    int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetUPView();
      //  mediaPlayer = new MediaPlayer();
        ArrayList<song> SongsList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
        // Define the projection (columns to retrieve)
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM_ID
        };

// Construct the query
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        Cursor cursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null
        );

// Iterate over the cursor to retrieve music information
        if (cursor != null) {
            int idColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int titleColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artistColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int pathColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int DurationColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int albumIdColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);

            while (cursor.moveToNext()) {
                // Check if column indexes are valid before retrieving values
                if (idColumnIndex != -1 && titleColumnIndex != -1 &&
                        artistColumnIndex != -1 && pathColumnIndex != -1
                        && DurationColumnIndex != -1 && albumIdColumnIndex != -1) {
                    String songId = cursor.getString(idColumnIndex);
                    String title = cursor.getString(titleColumnIndex);
                    String artist = cursor.getString(artistColumnIndex);
                    String path = cursor.getString(pathColumnIndex);
                    // String albumId = cursor.getString(albumIdColumnIndex);
                    String duration = cursor.getString(DurationColumnIndex);

                    // Retrieve the album art using the album ID
                    // Uri albumArtUri = Uri.parse("content://media/external/audio/albumart/" + albumId);

                    // Process the music information (e.g., display in a ListView or RecyclerView)
                    // ...
                    SongsList.add(new song(title, artist, path));
                } else {
                    // Log an error or handle the situation accordingly
                    // ...
                }
            }
        }

        cursor.close();

        ArrayAdapter<song> adapter =
                new ArrayAdapter<song>(MainActivity.this, android.R.layout.simple_list_item_2, android.R.id.text1, SongsList) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                        TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                        // Set title and subtitle
                        text1.setText(SongsList.get(position).getSongName());
                        text2.setText(SongsList.get(position).getSingerName());
                        if (SongsList.get(position).running) {
                            view.setBackgroundColor(Color.argb(20, 255, 255, 255)); // Selected color
                        } else {
                            view.setBackgroundColor(Color.TRANSPARENT); // Default color
                        }
                        return view;
                    }
                };
        listViewSongs.setAdapter(adapter);


        final int[] currentIndex = new int[1];
        final String[] path = new String[1];

        listViewSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (int j = 0; j < adapterView.getChildCount(); j++) {
                    adapterView.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
                    SongsList.get(j).running = false ;
                }
                view.setBackgroundColor(Color.argb(20, 255, 255, 255));

                path[0] = SongsList.get(i).getPath();
                currentIndex[0] = i;
                SongsList.get(i).running = true;
                try {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(path[0]);
                    mediaPlayer.prepare();


                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            }
        });

        final boolean[] flag = {false};
        imgbtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (flag[0] == false) {
                    if (mediaPlayer != null) {
                        mediaPlayer.start();
                    } else {

                        //  mediaPlayer = new MediaPlayer();
                        try {
                            mediaPlayer.setDataSource(path[0]);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    imgbtnPlay.setImageResource(android.R.drawable.ic_media_pause);
                    flag[0] = true;
                } else {
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        imgbtnPlay.setImageResource(android.R.drawable.ic_media_play);
                        flag[0] = false;
                    }
                }

            }
        });
        imgbtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SongsList.get(currentIndex[0]).running = false ;
                if (currentIndex[0] < SongsList.size() - 1) {
                    currentIndex[0] ++;
                    SongsList.get(currentIndex[0]).running = true  ;
                    playSong(SongsList.get(currentIndex[0] ).getPath());

                    if(flag[0] == false ){
                        flag[0]=true ;
                        imgbtnPlay.setImageResource(android.R.drawable.ic_media_pause);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        imgbtnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentIndex[0]  > 0) { // Check if there is a previous song
                    currentIndex[0] --;
                    playSong(SongsList.get(currentIndex[0] ).getPath());
                    if(flag[0] == false ){
                        flag[0]=true ;
                        imgbtnPlay.setImageResource(android.R.drawable.ic_media_pause);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    public void SetUPView() {
        listViewSongs = findViewById(R.id.ListViewSongs);
        imgbtnPlay = findViewById(R.id.imgbtnPlay);
        imgbtnNext = findViewById(R.id.imgbtnNext);
        imgbtnPrev = findViewById(R.id.imgbtnPrev);

    }
    public void playSong(String path) {
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}