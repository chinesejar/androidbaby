package timeroute.androidbaby.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import timeroute.androidbaby.R;
import timeroute.androidbaby.ui.activity.ImageViewActivity;
import timeroute.androidbaby.util.DensityUtil;

/**
 * Created by quentin on 17-8-13.
 */

public class FeedListPicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Map<String, Object>> pics;

    public FeedListPicAdapter(Context context, List<Map<String, Object>> pics) {
        this.context = context;
        this.pics = pics;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.layout_feed_pic, null);
        return new PicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PicViewHolder picViewHolder = (PicViewHolder) holder;
        picViewHolder.bindItem(pics.get(position));
    }

    @Override
    public int getItemCount() {
        return pics.size();
    }

    class PicViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.image_view_pic)
        ImageView imageView;

        public PicViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bindItem(Map<String, Object> pic) {
            String image_url = (String) pic.get("url");
            Picasso.with(context)
                    .load(image_url+"?imageMogr2/thumbnail/1000x"+String.valueOf(DensityUtil.dip2px(context, 400))+"/format/webp/blur/1x0/quality/75|imageslim")
                    .into(imageView);
            imageView.setOnClickListener(view -> {
                Intent intent = new Intent(context, ImageViewActivity.class);
                intent.putExtra("index", pics.indexOf(pic));
                String[] images = new String[pics.size()];
                for (int i=0;i<images.length;i++){
                    images[i] = pics.get(i).get("url").toString();
                }
                intent.putExtra("images", images);
                context.startActivity(intent);
            });
        }
    }
}
