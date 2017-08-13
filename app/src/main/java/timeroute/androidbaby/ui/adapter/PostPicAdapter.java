package timeroute.androidbaby.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timeroute.androidbaby.R;
import timeroute.androidbaby.util.DensityUtil;

/**
 * Created by quentin on 17-8-13.
 */

public class PostPicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Map<String, Object>> pics;

    public PostPicAdapter(Context context, List<Map<String, Object>> pics) {
        this.context = context;
        this.pics = pics;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.layout_pic, null);
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
            String imageFile = (String) pic.get("pic");
            new Compressor(context)
                    .setMaxWidth(DensityUtil.dip2px(context, 100))
                    .setQuality(75)
                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
                    .compressToFileAsFlowable(new File(imageFile))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((file) -> {
                        imageView.setImageURI(Uri.fromFile(file));
                    }, Throwable::printStackTrace);
            imageView.setOnClickListener(view -> {
                new AlertDialog.Builder(context)
                            .setTitle(context.getString(R.string.delete_title))
                            .setMessage(context.getString(R.string.delete_message))
                            .setPositiveButton(context.getString(R.string.sure), (dialog, j) -> {
                                pics.remove(pics.indexOf(pic));
                                notifyDataSetChanged();
                            })
                            .setNegativeButton(context.getString(R.string.cancel), null)
                    .show();
            });
        }
    }
}
