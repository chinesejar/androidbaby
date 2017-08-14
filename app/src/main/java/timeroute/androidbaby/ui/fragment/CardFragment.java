package timeroute.androidbaby.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import timeroute.androidbaby.R;
import timeroute.androidbaby.bean.feed.Feed;
import timeroute.androidbaby.ui.activity.FeedDetailActivity;


/**
 * Created by Nate on 2016/7/22.
 */
public class CardFragment extends Fragment {
    private static final String IMAGE_URL = "image_url";
    private Feed feed = null;

    public static CardFragment newInstance(Feed feed) {
        CardFragment fragment = new CardFragment();
        Bundle bdl = new Bundle();
        bdl.putSerializable("feed", feed);
        fragment.setArguments(bdl);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_card, container, false);
        ImageView imageView = v.findViewById(R.id.image_view);
        final Bundle bundle = getArguments();
        if (bundle != null) {
            feed =  (Feed)bundle.getSerializable("feed");
            if(feed != null){
                Picasso.with(getContext())
                        .load(feed.getFeedPic().get(0).getUrl())
                        .into(imageView);
            }
        }
        imageView.setOnClickListener(view -> {
            if(feed!=null){
                Intent intent = new Intent(getContext(), FeedDetailActivity.class);
                intent.putExtra("feed", feed);
                getContext().startActivity(intent);
            }
        });
        return v;
    }
}