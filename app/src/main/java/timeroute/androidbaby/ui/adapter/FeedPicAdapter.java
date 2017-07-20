package timeroute.androidbaby.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timeroute.androidbaby.R;

public class FeedPicAdapter extends SimpleAdapter {

    private Context mContext;
    public LayoutInflater inflater = null;

    public FeedPicAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        mContext = context;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.layout_feed_pic, null);

        HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
        Log.d("data", data.toString());
        ImageView image = (ImageView) vi.findViewById(R.id.image_view_pic);
        String image_url = (String) data.get("url");
        Glide.with(mContext).load(image_url)
                .into(image);
        return vi;
    }

}