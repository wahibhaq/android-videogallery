package videogallery.dubsmash.wahib.myvideogallery.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import videogallery.dubsmash.wahib.myvideogallery.R;

/**
 * Created by Wahib-Ul-Haq on 25.10.2015.
 */
class CustomArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> mData;

    public CustomArrayAdapter(Context context, final List<String> mData) {
        super(context, R.layout.list_row, mData);
        this.mData = mData;
        this.context = context;
    }

    public List<String> getData() {
        return mData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        String rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_row, null);
            holder = new ViewHolder();
            holder.tVideoName = (TextView) convertView.findViewById(R.id.video_name);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.tVideoName.setText(rowItem);
        return convertView;
    }

    /*private view holder class*/
    private static class ViewHolder {
        TextView tVideoName;
    }
}
