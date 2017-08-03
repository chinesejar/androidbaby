package timeroute.androidbaby.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import timeroute.androidbaby.R;
import timeroute.androidbaby.bean.feed.Comment;
import timeroute.androidbaby.bean.feed.CommentTimeLine;
import timeroute.androidbaby.ui.view.IFeedDetailView;
import timeroute.androidbaby.ui.view.RecyclerViewClickListener;
import timeroute.androidbaby.util.RoundTransform;
import timeroute.androidbaby.util.ScreenUtil;

/**
 * Created by chinesejar on 17-7-8.
 */

public class CommentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "FeedListAdapter";
    private RecyclerViewClickListener listener;
    private Context context;
    private CommentTimeLine commentTimeLine;
    private int status = 1;

    public static final int LOAD_MORE = 0;
    public static final int LOAD_PULL_TO = 1;
    public static final int LOAD_NONE = 2;
    public static final int LOAD_END = 3;
    private static final int TYPE_FOOTER = -1;

    public CommentListAdapter(Context context, CommentTimeLine commentTimeLine, RecyclerViewClickListener listener) {
        this.listener = listener;
        this.context = context;
        this.commentTimeLine = commentTimeLine;
    }

    @Override
    public int getItemViewType(int position) {
        if (commentTimeLine.getComments() != null) {
            if (position + 1 == getItemCount()) {
                return TYPE_FOOTER;
            } else {
                return position;
            }
        } else if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return position;
        }
    }

    public void updateLoadStatus(int status) {
        this.status = status;
        notifyDataSetChanged();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.linearLayoutComment)
        LinearLayout linearLayoutComment;
        @Bind(R.id.avatar)
        ImageView avatar;
        @Bind(R.id.nickname)
        TextView nickname;
        @Bind(R.id.content)
        TextView content;
        @Bind(R.id.comment_or_reply)
        TextView comment_or_reply;
        @Bind(R.id.at)
        TextView at;
        @Bind(R.id.create_time)
        TextView create_time;

        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            ScreenUtil screenUtil = ScreenUtil.instance(context);
            int screenWidth = screenUtil.getScreenWidth();
            linearLayoutComment.setLayoutParams(new LinearLayout.LayoutParams(screenWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
        }

        public void bindItem(Comment comment) {
            loadCirclePic(context, comment.getCreator().getAvatar(), avatar);
            avatar.setOnClickListener(view -> {
                if(listener != null){
                    listener.onAvatarClicked(comment.getCreator().getId(), comment.getCreator().getNickname(), comment.getCreator().getAssignment(), comment.getCreator().getAvatar());
                }
            });
            nickname.setText(comment.getCreator().getNickname());
            if(comment.getAt() != null){
                at.setVisibility(View.VISIBLE);
                comment_or_reply.setText(context.getString(R.string.reply));
                at.setText(comment.getAt().getNickname());
                at.setOnClickListener(view -> {
                    if(listener != null){
                        listener.onAvatarClicked(comment.getAt().getId(), comment.getAt().getNickname(), comment.getAt().getAssignment(), comment.getAt().getAvatar());
                    }
                });
            }else {
                comment_or_reply.setText(context.getString(R.string.comment));
            }
            content.setText(comment.getContent());
            create_time.setText(String.valueOf(comment.getCreate_time()));
            linearLayoutComment.setOnClickListener(view -> {
                listener.onAtClicked(comment.getCreator());
            });
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_load_prompt)
        TextView tv_load_prompt;
        @Bind(R.id.progress)
        ProgressBar progress;

        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtil.instance(context).dip2px(40));
            itemView.setLayoutParams(params);
        }

        private void bindItem() {
            switch (status) {
                case LOAD_MORE:
                    progress.setVisibility(View.VISIBLE);
                    tv_load_prompt.setText(context.getString(R.string.loading));
                    itemView.setVisibility(View.VISIBLE);
                    break;
                case LOAD_PULL_TO:
                    progress.setVisibility(View.GONE);
                    tv_load_prompt.setText(context.getString(R.string.load_more));
                    itemView.setVisibility(View.VISIBLE);
                    break;
                case LOAD_NONE:
                    progress.setVisibility(View.GONE);
                    tv_load_prompt.setText(context.getString(R.string.load_none));
                    break;
                case LOAD_END:
                    itemView.setVisibility(View.GONE);
                default:
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return commentTimeLine == null ? 0 : commentTimeLine.getComments().size() + 1;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            View view = View.inflate(parent.getContext(), R.layout.activity_view_footer, null);
            return new FooterViewHolder(view);
        } else {
            View view = View.inflate(parent.getContext(), R.layout.layout_comment, null);
            return new CommentViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommentViewHolder) {
            CommentViewHolder commentHolder = (CommentViewHolder) holder;
            commentHolder.bindItem(commentTimeLine.getComments().get(position));
        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.bindItem();
        }
    }

    public static void loadCirclePic(final Context context, String url, ImageView imageView) {
        if(url != null){
            Picasso.with(context)
                    .load(url)
                    .placeholder(R.drawable.ic_android)
                    .transform(new RoundTransform())
                    .into(imageView);
        }

    }
}
