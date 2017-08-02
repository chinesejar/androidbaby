package timeroute.androidbaby.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timeroute.androidbaby.R;
import timeroute.androidbaby.util.DensityUtil;

public class FeedPicAdapter extends SimpleAdapter {

    private Context mContext;
    private LayoutInflater inflater = null;

    public FeedPicAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        mContext = context;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.layout_feed_pic, null);
        }

        HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
        ImageView image = vi.findViewById(R.id.image_view_pic);
        String image_url = (String) data.get("url");
        Picasso.with(mContext)
                .load(image_url+"?imageMogr2/thumbnail/600x"+String.valueOf(DensityUtil.dip2px(mContext, 400))+"/format/webp/blur/1x0/quality/75|imageslim")
                .into(image);
        return vi;
    }

}