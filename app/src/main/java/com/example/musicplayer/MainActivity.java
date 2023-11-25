package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listViewSongs;
    public static ImageButton imgbtnPlay;
    ImageButton imgbtnNext;
    ImageButton imgbtnPrev;
    public static MediaPlayer mediaPlayer;
    public static String path;
    int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    LinearLayout bottomPlayer;

    public static Boolean flag;
    public static int currentIndex = 0;
    public static ArrayList<song> SongsList;
    public static ArrayAdapter<song> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetUPView();
        //  mediaPlayer = new MediaPlayer();
        SongsList = new ArrayList<>();
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

        adapter =
                new ArrayAdapter<song>(MainActivity.this, android.R.layout.simple_list_item_2, android.R.id.text1, SongsList) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                        TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                        // Set title and subtitle
                        text1.setText(SongsList.get(position).getSongName());
                        text2.setText(SongsList.get(position).getSingerName());

                        text2.setTextColor(Color.argb(100,255,255,255));
                        if (SongsList.get(position).running) {
                            view.setBackgroundColor(Color.argb(20, 255, 255, 255)); // Selected color
                        } else {
                            view.setBackgroundColor(getColor(R.color.Black)); // Default color
                        }
                        return view;
                    }
                };
        listViewSongs.setAdapter(adapter);


        flag = false;
        path = SongsList.get(0).getPath();


        listViewSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (int j = 0; j < adapterView.getChildCount(); j++) {
                    adapterView.getChildAt(j).setBackgroundColor(getColor(R.color.Black));
                    SongsList.get(j).running = false;
                }
                view.setBackgroundColor(Color.argb(20, 255, 255, 255));

                if (SongsList.get(currentIndex).running)
                    SongsList.get(currentIndex).running = false;
                path = SongsList.get(i).getPath();
                currentIndex = i;
                SongsList.get(i).running = true;
                try {


                    if (flag) {
                        mediaPlayer.stop();
                        flag = false;
                        imgbtnPlay.setImageResource(android.R.drawable.ic_media_play);

                    }
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(path);
                    mediaPlayer.prepare();


                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            }
        });


        imgbtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (flag == false) {
                    if (mediaPlayer != null) {
                        mediaPlayer.start();
                    } else {

                        //  mediaPlayer = new MediaPlayer();
                        try {
                            mediaPlayer.setDataSource(path);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    imgbtnPlay.setImageResource(android.R.drawable.ic_media_pause);
                    flag = true;
                } else {
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        imgbtnPlay.setImageResource(android.R.drawable.ic_media_play);
                        flag = false;
                    }
                }

                adapter.notifyDataSetChanged();

            }
        });
        imgbtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SongsList.get(currentIndex).running = false;
                if (currentIndex < SongsList.size() - 1) {
                    currentIndex++;
                    SongsList.get(currentIndex).running = true;
                    playSong(SongsList.get(currentIndex).getPath());

                    if (flag == false) {
                        flag = true;
                        imgbtnPlay.setImageResource(android.R.drawable.ic_media_pause);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        imgbtnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SongsList.get(currentIndex).running = false;
                if (currentIndex > 0) {
                    currentIndex--;
                    SongsList.get(currentIndex).running = true;
                    playSong(SongsList.get(currentIndex).getPath());
                    if (flag == false) {
                        flag = true;
                        imgbtnPlay.setImageResource(android.R.drawable.ic_media_pause);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
        bottomPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SongActivity.class);
                intent.putExtra("songName", SongsList.get(currentIndex).getSongName());
                intent.putExtra("singerName", SongsList.get(currentIndex).getSingerName());
                intent.putExtra("curretIndex", currentIndex);
                intent.putExtra("flag", flag);


                startActivity(intent);

            }
        });


    }

    public void SetUPView() {
        listViewSongs = findViewById(R.id.ListViewSongs);
        imgbtnPlay = findViewById(R.id.imgbtnPlay);
        imgbtnNext = findViewById(R.id.imgbtnNext);
        imgbtnPrev = findViewById(R.id.imgbtnPrev);
        bottomPlayer = findViewById(R.id.bottomPlayer);

    }

    public static void playSong(String path) {
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