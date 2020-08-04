package com.derron.readmp3filesfromstorage;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static String SD_PATH = "/sdcard/";
    MediaPlayer mediaPlayer = new MediaPlayer();
    static ArrayList<String> musiclist = new ArrayList<>();
    static Mp3Filter filter = new Mp3Filter();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listview);
        updateList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(SD_PATH + musiclist.get(i));
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    Log.v(getString(R.string.app_name),e.getMessage());
                }

            }
        });

    }

    private void updateList(){
        File dir = new File(SD_PATH);
        if (dir.listFiles(filter).length > 0){
            for (File file: dir.listFiles(filter)){
                musiclist.add(file.getName());
            }
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this,R.layout.songs,musiclist);
        listView.setAdapter(adapter);
    }

}