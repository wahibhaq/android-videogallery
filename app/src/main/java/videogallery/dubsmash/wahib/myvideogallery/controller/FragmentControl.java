package videogallery.dubsmash.wahib.myvideogallery.controller;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import videogallery.dubsmash.wahib.myvideogallery.R;
import videogallery.dubsmash.wahib.myvideogallery.view.CustomFragment;
import videogallery.dubsmash.wahib.myvideogallery.view.VideoGalleryActivity;
import videogallery.dubsmash.wahib.myvideogallery.view.VideoGalleryFragment;

/**
 * Created by Wahib-Ul-Haq on 25.10.2015.
 */
public class FragmentControl {
    public enum FragmentTag {
        FRAGMENT_GALLERY,
        NONE
    }

    private final FragmentManager fragmentManager;

    public FragmentControl(VideoGalleryActivity mainActivity) {
        this.fragmentManager = mainActivity.getSupportFragmentManager();
    }

    private static CustomFragment createFragmentForTag(FragmentTag tag) {
        CustomFragment frag = null;
        switch (tag) {
            case FRAGMENT_GALLERY:
                frag = new VideoGalleryFragment();
                break;
            case NONE:
                throw new RuntimeException("Tried to create a NONE fragment");
        }

        frag.setMyFragmentTag(tag);
        return frag;
    }

    public void showFragment(FragmentTag tag) {
        CustomFragment fragment = createFragmentForTag(tag);
        if(fragment != null) {
            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            //firstFragment.setArguments(getIntent().getExtras());
            // Add the fragment to the 'fragment_container' FrameLayout
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(R.id.fragment_container, fragment).commit();
            fragmentManager.executePendingTransactions();
        }
    }

}
