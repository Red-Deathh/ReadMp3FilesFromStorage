package com.derron.readmp3filesfromstorage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    //private final static String TAG = "MainActivity";
    final String MEDIA_PATH = Environment.getExternalStorageDirectory().getPath() + "/";
    ArrayList<HashMap<String,String>> songList;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //recyclerView reference
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        //recyclerView setup
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this,
                LinearLayoutManager.VERTICAL, false);
        SongArrayAdapter adapter = new SongArrayAdapter(getPlayList(MEDIA_PATH));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        //handling clickEvents
        adapter.setOnSongClickListener(new SongArrayAdapter.OnSongClickListener() {
            @Override
            public void onSongClick(int position) {
                MediaPlayer mp = new MediaPlayer();
                try {

                    if (mp.isPlaying()){
                        mp.stop();
                    }

                    assert songList != null;
                    mp.setDataSource(songList.get(position).get("file_path"));
                    mp.prepare();
                    mp.start();
                    Toast.makeText(MainActivity.this,"Song Playing",Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

    }

    ArrayList<HashMap<String,String>> getPlayList(String rootPath) {
        ArrayList<HashMap<String,String>> fileList = new ArrayList<>();

        try {
            File rootFolder = new File(rootPath);
            File[] files = rootFolder.listFiles(); //here you will get NPE if directory doesn't contains any file,handle it like this.
            assert files != null;
            for (File file : files) {
                if (file.isDirectory()) {
                    if (getPlayList(file.getAbsolutePath()) != null) {
                        fileList.addAll(getPlayList(file.getAbsolutePath()));
                    } else {
                        break;
                    }
                } else if (file.getName().endsWith(".mp3")
                        || file.getName().endsWith(".aac")
                        || file.getName().endsWith(".m4a")
                        || file.getName().endsWith(".ogg")
                        || file.getName().endsWith(".wav")) {
                    HashMap<String, String> song = new HashMap<>();
                    song.put("file_path", file.getAbsolutePath());
                    song.put("file_name", file.getName());
                    fileList.add(song);
                }

            }
            return fileList;
        } catch (Exception e) {
            return null;
        }
    }
}