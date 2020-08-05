package com.derron.readmp3filesfromstorage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public String SD_PATH = "/sdcard/";
    MediaPlayer mediaPlayer = new MediaPlayer();
    static ArrayList<String> musicList = new ArrayList<>();
    ListView listView;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listview);
        updateList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(SD_PATH + musicList.get(i));
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    Log.v(getString(R.string.app_name), e.getMessage());
                }

            }
        });

    }

    private void updateList () {
        File rootDir = new File(SD_PATH);
        if (rootDir.isDirectory()) {
            try {
                File[] files = rootDir.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept (File file, String name) {
                        if (name.lastIndexOf('.') > 0 ){
                            String extension = name.substring(name.lastIndexOf('.')+1).toLowerCase();
                            Toast formats = Toast.makeText(MainActivity.this.getApplicationContext(), "Format " + extension + " found", Toast.LENGTH_SHORT);

                            switch (extension) {
                                case "mp3":
                                case "aac":
                                case "flac":
                                case "gsm":
                                case "m4a":
                                case "mid":
                                case "ogg":
                                case "ota":
                                case "rtttl":
                                case "rtx":
                                case "xmp":
                                case "wav":
                                    if (formats != null) {
                                        formats.cancel();
                                    }
                                    formats.show();
                                    return true;
                                default:
                                    return false;
                            }
                        }
                        return false;
                    }
                });
                if (files != null && files.length > 0) {
                    for (File file : files) {
                        musicList.add(file.getName());
                    }
                } else
                    Toast.makeText(this, "File(s) couldn't be found.", Toast.LENGTH_LONG).show();
            } catch (NullPointerException npe) {
                npe.getLocalizedMessage();
            }
        } else
            Toast.makeText(this, "Specified/Hard-coded path is not a directory!", Toast.LENGTH_LONG).show();

        ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.songs, musicList);
        listView.setAdapter(adapter);
    }

}