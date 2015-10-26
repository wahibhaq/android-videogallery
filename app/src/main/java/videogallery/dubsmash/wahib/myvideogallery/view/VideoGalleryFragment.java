package videogallery.dubsmash.wahib.myvideogallery.view;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;
import java.util.List;

import videogallery.dubsmash.wahib.myvideogallery.R;
import videogallery.dubsmash.wahib.myvideogallery.util.Storage;
import videogallery.dubsmash.wahib.myvideogallery.util.Util;

/**
 * Principal fragment containing ListView and VideoView.
 */
public class VideoGalleryFragment extends CustomFragment {

    private List<String> videoItems;
    private CustomArrayAdapter mAdapter;
    private Storage store;
    private VideoView playbackView;
    private Uri videoUri = null;
    private int lastVideoPosition = 0;

    public VideoGalleryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        store = new Storage(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View thisFragment = inflater.inflate(R.layout.fragment_video_gallery, container, false);
        playbackView = (VideoView) thisFragment.findViewById(R.id.video_playback);
        resetActionBar();
        return thisFragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // remove the dividers from the ListView of the ListFragment
        getListView().setDivider(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        startPreview(position);
    }

    private void resetActionBar() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        MenuItem item = toolbar.getMenu().findItem(R.id.action_recordvideo);
        if(item != null)
            item.setVisible(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        videoItems = store.getListString(Util.VIDEO_NAMES_KEY);
        mAdapter = new CustomArrayAdapter(getActivity(), videoItems);
        setListAdapter(mAdapter);

        if (savedInstanceState != null) {
            lastVideoPosition = savedInstanceState.getInt("lastPosition");
            playbackView.seekTo(lastVideoPosition);
        }
    }

    public void updateData() {
        videoItems = store.getListString(Util.VIDEO_NAMES_KEY);
        mAdapter.getData().clear();
        mAdapter.getData().addAll(videoItems);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Preview Mode refers to playing selected video
     */
    private void switchToPreviewMode() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.videopreview_title));
        MenuItem item = toolbar.getMenu().findItem(R.id.action_recordvideo);
        if(item != null)
            item.setVisible(false);
        getListView().setVisibility(View.GONE);
        playbackView.setVisibility(View.VISIBLE);
    }

    /**
     * Gallery Mode refers to showing list of videos already recorded
     */
    public void switchToGalleryMode() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        MenuItem item = toolbar.getMenu().findItem(R.id.action_recordvideo);
        if (item != null)
            item.setVisible(true);
        getListView().setVisibility(View.VISIBLE);
        playbackView.pause();
        playbackView.setVisibility(View.GONE);
    }

    /**
     * Depending on the video selected, play it in VideoView
     * @param position
     */
    private void startPreview(int position) {
        switchToPreviewMode();
        videoUri = Uri.parse(store.getListString(Util.VIDEO_URIS_KEY).get(position));
        if(videoUri != null)
            playbackRecordedVideo();
    }

    private void playbackRecordedVideo() {
        playbackView.setVideoURI(videoUri);
        playbackView.setMediaController(new MediaController(getActivity()));
        playbackView.requestFocus();
        playbackView.start();
        playbackView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }

    // This gets called before onPause so pause video here.
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        lastVideoPosition = playbackView.getCurrentPosition();
        outState.putInt("lastPosition", lastVideoPosition);
        playbackView.pause();
    }


    public boolean isPreviewMode() {
        return playbackView.getVisibility() == View.VISIBLE;
    }
}
