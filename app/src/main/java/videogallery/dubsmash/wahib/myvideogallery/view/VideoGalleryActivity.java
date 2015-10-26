package videogallery.dubsmash.wahib.myvideogallery.view;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import videogallery.dubsmash.wahib.myvideogallery.controller.FragmentControl;
import videogallery.dubsmash.wahib.myvideogallery.util.Storage;
import videogallery.dubsmash.wahib.myvideogallery.util.Util;
import videogallery.dubsmash.wahib.myvideogallery.R;

public class VideoGalleryActivity extends AppCompatActivity {

    private static String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            FragmentControl fragmentControl = new FragmentControl(this);
            fragmentControl.showFragment(FragmentControl.FragmentTag.FRAGMENT_GALLERY);

        }
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_video_gallery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_recordvideo) {
            startRecordingVideo();
        }

        return super.onOptionsItemSelected(item);
    }


    private void startRecordingVideo() {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, Util.RECORDING_MAX_DURATION);
            fileName = "VID_" + new SimpleDateFormat("yyyyMMdd_hhmmss").format(new Date()) + ".mp4";
            File mediaFile = new File(
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + fileName );
            Uri videoUri = Uri.fromFile(mediaFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
            startActivityForResult(intent, Util.VIDEO_CAPTURE);
        } else {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "No camera on device", Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.RED);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Util.VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Snackbar.make(findViewById(android.R.id.content), "Video has been saved as : " + fileName, Snackbar.LENGTH_LONG)
                        .show();
                Storage store = new Storage(getApplicationContext());
                store.addToList(Util.VIDEO_NAMES_KEY, fileName);
                store.addToList(Util.VIDEO_URIS_KEY, data.getDataString());
                updateGalleryList();
            } else if (resultCode == RESULT_CANCELED) {
                Snackbar.make(findViewById(android.R.id.content), "Video recording cancelled", Snackbar.LENGTH_LONG)
                        .show();
            } else {
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Failed to record video", Snackbar.LENGTH_LONG);
                View snackbarView = snackbar.getView();
                TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.RED);
            }
        }
    }

    private void updateGalleryList() {
        List<Fragment> allFragments = getSupportFragmentManager().getFragments();
        if (allFragments != null) {
            for (Fragment fragment : allFragments) {
                VideoGalleryFragment f1 = (VideoGalleryFragment)fragment;
                if (f1.getMyFragmentTag() == FragmentControl.FragmentTag.FRAGMENT_GALLERY) {
                    f1.updateData();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        List<Fragment> allFragments = getSupportFragmentManager().getFragments();
        if (allFragments != null) {
            for (Fragment fragment : allFragments) {
                VideoGalleryFragment f1 = (VideoGalleryFragment)fragment;
                if (f1.getMyFragmentTag() == FragmentControl.FragmentTag.FRAGMENT_GALLERY) {
                    if(f1.isVisible()){
                        if(f1.isPreviewMode()) {
                            //Return from Preview mode to Gallery List mode
                            f1.switchToGalleryMode();
                        }
                        else {
                            //if already in Gallery List Mode then just finish the application
                            super.onBackPressed();
                        }
                    }
                }
            }
        }
    }


}
