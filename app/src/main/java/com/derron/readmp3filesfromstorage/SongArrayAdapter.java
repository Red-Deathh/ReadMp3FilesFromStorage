package com.derron.readmp3filesfromstorage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class SongArrayAdapter extends RecyclerView.Adapter<SongArrayAdapter.SongViewHolder> {

    private ArrayList<HashMap<String,String>> songList;
    //private static final String TAG = "SongArrayAdapter";
    private OnSongClickListener onSongClickListener;

    public interface OnSongClickListener {
        void onSongClick(int position);
    }

    public void setOnSongClickListener(OnSongClickListener listener) {
        onSongClickListener = listener;
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {

        public TextView songTitle;

        public SongViewHolder(final View itemView, final OnSongClickListener listener) {
            super(itemView);

            songTitle = itemView.findViewById(R.id.single_song);

            songTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onSongClick(position);
                        }
                    }
                }
            });

        }
    }

    public SongArrayAdapter(ArrayList<HashMap<String,String>> songList) {
        this.songList = songList;
    }

    @NonNull
    @Override
    public SongArrayAdapter.SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.songs, parent, false);
        return new SongViewHolder(view, onSongClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final SongViewHolder holder, final int position) {
        final String song_name = songList.get(position).get("file_name");

        holder.songTitle.setText(song_name);
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

}