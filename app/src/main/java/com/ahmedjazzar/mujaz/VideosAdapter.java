package com.ahmedjazzar.mujaz;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;
import com.volokh.danylo.video_player_manager.ui.MediaPlayerWrapper;
import com.volokh.danylo.video_player_manager.ui.SimpleMainThreadMediaPlayerListener;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by ahmedjazzar on 8/15/16.
 */
public class VideosAdapter extends RecyclerView.Adapter<VideosViewHolder> implements PopupMenu.OnMenuItemClickListener    {

    private Context mContext;
    private List<Video> videoList;
    private VideoPlayerManager<MetaData> mVideoPlayerManager;

    public VideosAdapter(Context context, List<Video> videoList, VideoPlayerManager<MetaData> videoPlayerManager) {
        this.mContext = context;
        this.videoList = videoList;
        this.mVideoPlayerManager = videoPlayerManager;
    }

    @Override
    public VideosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_card, parent, false);

        return new VideosViewHolder(itemView);
    }

    public void loadBitmap(int resId, ImageView imageView) {
        BitmapWorkerTask task = new BitmapWorkerTask(imageView, mContext.getResources());
        task.execute(resId);
    }

    @Override
    public void onBindViewHolder(final VideosViewHolder holder, int position) {
        final Video video = videoList.get(position);

        holder.title.setText(video.getName());
        holder.count.setText(video.getNumOfViews() + " views");

        loadBitmap(video.getThumbnail(), holder.thumbnail);

        // This listener is responsible for hiding and showing views relate to video state
        holder.videoView.addMediaPlayerListener(new SimpleMainThreadMediaPlayerListener()   {
            @Override
            public void onVideoPreparedMainThread() {
                // We hide the cover when video is prepared. Playback is about to start
                holder.playBtn.setVisibility(View.INVISIBLE);
                holder.thumbnail.setVisibility(View.INVISIBLE);
                holder.progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onVideoStoppedMainThread() {
                // We show the cover when video is stopped
                holder.playBtn.setVisibility(View.VISIBLE);
                holder.thumbnail.setVisibility(View.VISIBLE);
            }

            @Override
            public void onVideoCompletionMainThread() {
                // We show the cover when video is completed
                holder.playBtn.setVisibility(View.VISIBLE);
                holder.thumbnail.setVisibility(View.GONE);
            }

        });

        // Handling pause and play events.
        // This allow the video to pause once the user clicks on the video card instead of the
        // default behavior of stopping it. Things to handle also in this case is the views that
        // depend on the video state.
        holder.videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (holder.videoView.getCurrentState() == MediaPlayerWrapper.State.STARTED) {
                        holder.videoView.pause();
                        holder.playBtn.setVisibility(View.VISIBLE);
                    } else {
                        holder.videoView.start();
                        holder.playBtn.setVisibility(View.INVISIBLE);
                    }
                } catch (NullPointerException ex)   {
                    // The player manger is not initialized yet
                    mVideoPlayerManager.playNewVideo(null, holder.videoView, video.getVideoUrl());
                    holder.progressBar.setVisibility(View.VISIBLE);
                    holder.playBtn.setVisibility(View.INVISIBLE);
                }

            }
        });

        // Show popup menu on click on option button
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_video, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_add_favourite:
                Toast.makeText(mContext, R.string.action_add_favourite, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_watch_later:
                Toast.makeText(mContext, R.string.action_watch_later, Toast.LENGTH_SHORT).show();
                return true;
            default:
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }
}