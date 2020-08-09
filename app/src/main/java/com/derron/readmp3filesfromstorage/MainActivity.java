package com.derron.readmp3filesfromstorage;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<ArrayList<HashMap<String, String>>>{

    private final static String TAG = MainActivity.class.getName(), MEDIA_PATH = Environment.getExternalStorageDirectory().getPath() + "/";;
    private final static int LOADER_ID = 12;
    private ArrayList<HashMap<String, String>> songList;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //recyclerView reference
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        //recyclerView setup
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this,
                LinearLayoutManager.VERTICAL, false);

        songList = new ArrayList<>(getPlayList(MEDIA_PATH));
        SongArrayAdapter adapter = new SongArrayAdapter(songList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        //handling clickEvents
        adapter.setOnSongClickListener(new SongArrayAdapter.OnSongClickListener() {
            @Override
            public void onSongClick (int position) {
                MediaPlayer mp = new MediaPlayer();
                try {
                    if (mp.isPlaying()) {
                        mp.stop();
                    }

                    assert songList != null;
                    mp.setDataSource(songList.get(position).get("file_path"));
                    mp.prepare();
                    mp.start();
                    Toast.makeText(MainActivity.this, "Song Playing", Toast.LENGTH_SHORT).show();
                } catch (AssertionError ae) {
                    Toast.makeText(MainActivity.this, "No files could be found!", Toast.LENGTH_SHORT).show();
                    ae.printStackTrace();
                } catch (IOException ioe) {
                    Toast.makeText(MainActivity.this, "File could not be played... Sorry! Need to implement AudioManager", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    ArrayList<HashMap<String, String>> getPlayList (String rootPath) {
        ArrayList<HashMap<String, String>> fileList = new ArrayList<>();

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

    @Override
    public Loader<ArrayList<HashMap<String, String>>> onCreateLoader (int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished (Loader<ArrayList<HashMap<String, String>>> loader, ArrayList<HashMap<String, String>> data) {

    }

    @Override
    public void onLoaderReset (Loader<ArrayList<HashMap<String, String>>> loader) {

    }
}