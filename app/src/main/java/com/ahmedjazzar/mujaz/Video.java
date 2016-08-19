package com.ahmedjazzar.mujaz;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.volokh.danylo.visibility_utils.items.ListItem;

/**
 * Created by ahmedjazzar on 8/15/16.
 */
public class Video implements ListItem {
    private String mName;
    private int mNumOfViews;
    private int mThumbnail;
    private String mVideoUrl;

    public Video(String name, int numOfViews, String videoUrl, int thumbnail) {
        this.mName = name;
        this.mNumOfViews = numOfViews;
        this.mVideoUrl = videoUrl;
        this.mThumbnail = thumbnail;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public int getNumOfViews() {
        return mNumOfViews;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public int getThumbnail() {
        return mThumbnail;
    }

    @Override
    public int getVisibilityPercents(View view) {
        return 0;
    }

    @Override
    public void setActive(View newActiveView, int newActiveViewPosition) {

    }

    @Override
    public void deactivate(View currentView, int position) {

    }
}
