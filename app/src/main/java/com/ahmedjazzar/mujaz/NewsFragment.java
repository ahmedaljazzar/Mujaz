package com.ahmedjazzar.mujaz;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.volokh.danylo.video_player_manager.manager.PlayerItemChangeListener;
import com.volokh.danylo.video_player_manager.manager.SingleVideoPlayerManager;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;
import com.volokh.danylo.visibility_utils.calculator.DefaultSingleItemCalculatorCallback;
import com.volokh.danylo.visibility_utils.calculator.ListItemsVisibilityCalculator;
import com.volokh.danylo.visibility_utils.calculator.SingleListViewItemActiveCalculator;
import com.volokh.danylo.visibility_utils.scroll_utils.ItemsPositionGetter;
import com.volokh.danylo.visibility_utils.scroll_utils.RecyclerViewItemPositionGetter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass used as a base for each new fragment in the app home.
 *
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author ahmedjazzar
 */
public class NewsFragment extends Fragment implements PlayerItemChangeListener{

    private static final String ARG_URLS_ARRAY = "array_of_videos_urls";
    private static final String ARG_VIEWS_ARRAY = "array_of_videos_views";
    private static final String ARG_TITLES_ARRAY = "array_of_videos_titles";
    private static final String ARG_THUMBNAILS_ARRAY = "array_of_videos_thumbnails";

    private VideosAdapter adapter;
    private List<Video> mVideosList;
    private RecyclerView mRecyclerView;
    private ListItemsVisibilityCalculator mVideoVisibilityCalculator;
    private ItemsPositionGetter mItemsPositionGetter;
    private LinearLayoutManager mLayoutManager;
    private final VideoPlayerManager<MetaData> mVideoPlayerManager = new SingleVideoPlayerManager(this);

    private int mScrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

    private String[] mTitles;
    private int[] mViews;
    private int[] mThumbnails;
    private String[] mURLS;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param titles Array of videos titles.
     * @param views Array of videos number of views.
     * @param urls Array of videos urls.
     * @return A new instance of fragment NewsFragment.
     */
    public static NewsFragment newInstance(String[] titles, int[] views, String[] urls, int[] thumbnails) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();

        args.putStringArray(ARG_TITLES_ARRAY, titles);
        args.putIntArray(ARG_VIEWS_ARRAY, views);
        args.putStringArray(ARG_URLS_ARRAY, urls);
        args.putIntArray(ARG_THUMBNAILS_ARRAY, thumbnails);

        fragment.setArguments(args);
        return fragment;
    }

    /**
     * I'm calling it if I no longer need the player
     */
    public void resetMediaPlayer()  {
        mVideoPlayerManager.resetMediaPlayer();
    }

    /**
     * Setup videos titles, views, and urls
     */
    private void setupVideos()  {
        mVideosList = new ArrayList<>();

        for (int i = 0; i < mURLS.length; i++) {
            mVideosList.add(new Video(mTitles[i], mViews[i], mURLS[i], mThumbnails[i]));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitles = getArguments().getStringArray(ARG_TITLES_ARRAY);
            mViews = getArguments().getIntArray(ARG_VIEWS_ARRAY);
            mURLS = getArguments().getStringArray(ARG_URLS_ARRAY);
            mThumbnails = getArguments().getIntArray(ARG_THUMBNAILS_ARRAY);

            setupVideos();
        }

        adapter = new VideosAdapter(getContext(), mVideosList, mVideoPlayerManager);
        mVideoVisibilityCalculator = new  SingleListViewItemActiveCalculator(new DefaultSingleItemCalculatorCallback(), mVideosList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
                mScrollState = scrollState;
                if (scrollState == RecyclerView.SCROLL_STATE_IDLE && !mVideosList.isEmpty()) {

                    mVideoVisibilityCalculator.onScrollStateIdle(
                            mItemsPositionGetter,
                            mLayoutManager.findFirstVisibleItemPosition(),
                            mLayoutManager.findLastVisibleItemPosition());
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!mVideosList.isEmpty()) {
                    mVideoVisibilityCalculator.onScroll(
                            mItemsPositionGetter,
                            mLayoutManager.findFirstVisibleItemPosition(),
                            mLayoutManager.findLastVisibleItemPosition() - mLayoutManager.findFirstVisibleItemPosition() + 1,
                            mScrollState);
                }
            }
        });
        mItemsPositionGetter = new RecyclerViewItemPositionGetter(mLayoutManager, mRecyclerView);

        adapter.notifyDataSetChanged();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!mVideosList.isEmpty()){
            // need to call this method from list view handler in order to have filled list

            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {

                    mVideoVisibilityCalculator.onScrollStateIdle(
                            mItemsPositionGetter,
                            mLayoutManager.findFirstVisibleItemPosition(),
                            mLayoutManager.findLastVisibleItemPosition());

                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        resetMediaPlayer();
    }

    @Override
    public void onPlayerItemChanged(MetaData currentItemMetaData) {

    }
}
