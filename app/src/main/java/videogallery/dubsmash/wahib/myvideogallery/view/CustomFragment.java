package videogallery.dubsmash.wahib.myvideogallery.view;

import android.support.v4.app.ListFragment;

import videogallery.dubsmash.wahib.myvideogallery.controller.FragmentControl;

/**
 * Created by Wahib-Ul-Haq on 25.10.2015.
 */
public class CustomFragment extends ListFragment {
    private FragmentControl.FragmentTag myFragmentTag;

    public void setMyFragmentTag(FragmentControl.FragmentTag fragmentTagIn) {
        myFragmentTag = fragmentTagIn;
    }

    public FragmentControl.FragmentTag getMyFragmentTag() {
        return myFragmentTag;
    }
}
