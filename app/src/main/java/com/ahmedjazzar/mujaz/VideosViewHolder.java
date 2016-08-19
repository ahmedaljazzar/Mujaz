package com.ahmedjazzar.mujaz;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.volokh.danylo.video_player_manager.ui.VideoPlayerView;

/**
 * This class holds each video element values in the list
 *
 * Created by ahmedjazzar on 8/19/16.
 */
public class VideosViewHolder extends RecyclerView.ViewHolder {
    public TextView title, count;
    public ImageView playBtn;
    public ImageView overflow;
    public ImageView thumbnail;
    public ProgressBar progressBar;
    public VideoPlayerView videoView;

    public VideosViewHolder(View view) {
        super(view);
        title = (TextView) view.findViewById(R.id.title);
        count = (TextView) view.findViewById(R.id.count);
        videoView = (VideoPlayerView) view.findViewById(R.id.video_view);
        overflow = (ImageView) view.findViewById(R.id.overflow);
        playBtn = (ImageView) view.findViewById(R.id.play_btn);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
    }
}
