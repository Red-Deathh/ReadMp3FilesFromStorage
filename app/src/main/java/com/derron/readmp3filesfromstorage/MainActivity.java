package com.derron.readmp3filesfromstorage;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<ArrayList<HashMap<String, String>>> {

    private final static String TAG = MainActivity.class.getName(), MEDIA_PATH = Environment.getExternalStorageDirectory().getPath() + "/";
    ;
    private final static int LOADER_ID = 12;
    private ArrayList<HashMap<String, String>> songList;
    private ProgressBar pbLoadFiles;
    RecyclerView recyclerView;
    private TextView tvNoFiles;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //recyclerView reference
        recyclerView = findViewById(R.id.recyclerView);
        pbLoadFiles = (ProgressBar) findViewById(R.id.pb_load_files);
        tvNoFiles = (TextView) findViewById(R.id.no_files);

//        songList = new ArrayList<>(getPlayList(MEDIA_PATH));

        pbLoadFiles.setVisibility(View.VISIBLE);
        tvNoFiles.setVisibility(View.VISIBLE);

        LoaderManager loader = getSupportLoaderManager();
        Bundle bundle = new Bundle();
        bundle.putString("path", MEDIA_PATH);
        loader.initLoader(LOADER_ID, bundle, MainActivity.this);
    }

    ArrayList<HashMap<String, String>> getPlayList (String rootPath) {
        ArrayList<HashMap<String, String>> fileList = new ArrayList<>();
//        Log.i(TAG, "getPlayList()");
//        Log.i(TAG, "rootPath:: " + rootPath);
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
        } catch (AssertionError ae) {
            Log.i(TAG, ae.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public Loader<ArrayList<HashMap<String, String>>> onCreateLoader (int id, final Bundle args) {
        Log.i(TAG, "onCreateLoader()");
        return new AsyncTaskLoader<ArrayList<HashMap<String, String>>>(this) {

            ArrayList<HashMap<String, String>> fileList = null;
            String rootDir = args.getString("path");

            @Override
            protected void onStartLoading () {
                Log.i(TAG, "onStartLoading()");
                if (fileList == null) {
                    pbLoadFiles.setVisibility(View.INVISIBLE);
                    tvNoFiles.setVisibility(View.GONE);
                    forceLoad();
                } else {
                    deliverResult(fileList);
                }
            }

            @Override
            public ArrayList<HashMap<String, String>> loadInBackground () {
                Log.i(TAG, "loadInBackground():: The search starts...");
                return getPlayList(rootDir);
            }

            @Override
            public void deliverResult (ArrayList<HashMap<String, String>> data) {
                fileList = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished (Loader<ArrayList<HashMap<String, String>>> loader, final ArrayList<HashMap<String, String>> data) {
        Log.i(TAG, "onLoadFinished()");
        pbLoadFiles.setVisibility(View.INVISIBLE);
        if (data != null) {
            tvNoFiles.setVisibility(View.GONE);

            //recyclerView setup
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this,
                    LinearLayoutManager.VERTICAL, false);
            SongArrayAdapter adapter = new SongArrayAdapter(data);

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

                        assert data != null;
                        mp.setDataSource(data.get(position).get("file_path"));
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
        } else
            tvNoFiles.setVisibility(View.VISIBLE);

    }

    @Override
    public void onLoaderReset (Loader<ArrayList<HashMap<String, String>>> loader) {
        pbLoadFiles.setVisibility(View.VISIBLE);
    }
}