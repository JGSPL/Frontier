package com.procialize.mrgeApp20.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.CustomTools.MyJZVideoPlayerStandard;
import com.procialize.mrgeApp20.CustomTools.PixabayImageView;
import com.procialize.mrgeApp20.GetterSetter.EventSettingList;
import com.procialize.mrgeApp20.GetterSetter.NewsFeedList;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;

import org.apache.commons.lang3.StringEscapeUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.jzvd.JZVideoPlayerStandard;

import static android.content.Context.MODE_PRIVATE;
import static com.procialize.mrgeApp20.Utility.Util.setTextViewDrawableColor;

/**
 * Created by Naushad on 10/31/2017.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MyViewHolder> {

    public List<NewsFeedList> feedLists;
    APIService mAPIService;
    SessionManager sessionManager;
    float p1;
    String news_feed_like, news_feed_comment, news_feed_share;
    List<EventSettingList> eventSettingLists;
    HashMap<String, String> user;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    SharedPreferences prefs;
    private Context context;
    private FeedAdapterListner listener;

    public FeedAdapter(Context con, List<NewsFeedList> feedLists, FeedAdapterListner listener) {
        this.feedLists = feedLists;
        this.listener = listener;
        this.context = con;

        SessionManager sessionManager = new SessionManager(con);

        user = sessionManager.getUserDetails();
    }

    private void initialize(final NewsFeedList feed, final MyViewHolder holder, final int position) {

        holder.nameTv.setText(feed.getFirstName() + " " + feed.getLastName());
        holder.companyTv.setText(feed.getCompanyName());
        holder.designationTv.setText(feed.getDesignation());
        holder.headingTv.setText(StringEscapeUtils.unescapeJava(feed.getPostStatus()));
        holder.liketext.setText(feed.getTotalLikes() + " Likes ");
        holder.commenttext.setText(feed.getTotalComments() + " Comments ");

        if (user.get(SessionManager.KEY_ID).equalsIgnoreCase(feedLists.get(position).getAttendeeId())) {
            holder.editIV.setVisibility(View.VISIBLE);
            holder.moreIV.setVisibility(View.VISIBLE);
//            reportTv.setVisibility(View.GONE);
//            reportuserTv.setVisibility(View.GONE);
//            blockuserTv.setVisibility(View.GONE);
        } else {
            holder.editIV.setVisibility(View.GONE);
            holder.moreIV.setVisibility(View.GONE);
//            deleteTv.setVisibility(View.GONE);
//            hideTv.setVisibility(View.VISIBLE);
//            reportTv.setVisibility(View.VISIBLE);
//            reportuserTv.setVisibility(View.VISIBLE);
//            blockuserTv.setVisibility(View.VISIBLE);
        }

        if (news_feed_like.equalsIgnoreCase("0")) {
            holder.likeTv.setVisibility(View.GONE);
        }

        if (news_feed_comment.equalsIgnoreCase("0")) {
            holder.commentTv.setVisibility(View.GONE);
        }

        if (news_feed_share.equalsIgnoreCase("0")) {
            holder.shareTv.setVisibility(View.GONE);

        }

        weightapply(holder.likeTv, holder.commentTv, holder.shareTv, holder.viewone, holder.viewtwo);

        try {
            float width = Float.parseFloat(feed.getWidth());
            float height = Float.parseFloat(feed.getHeight());

            p1 = height / width;
            holder.feedimageIv.setAspectRatio(p1);

        } catch (Exception e) {
            e.printStackTrace();
        }


        SimpleDateFormat formatter = new SimpleDateFormat("ApiConstant HH:mm:ss");
        try {
            Date date1 = formatter.parse(feed.getPostDate());

            DateFormat originalFormat = new SimpleDateFormat("dd MMM , HH:mm", Locale.UK);

            String date = originalFormat.format(date1);

            holder.dateTv.setText(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (feed.getProfilePic() != null) {

            Glide.with(context).load(ApiConstant.profilepic + feed.getProfilePic())
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    holder.progressView.setVisibility(View.GONE);
                    holder.profileIv.setImageResource(R.drawable.profilepic_placeholder);
                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    holder.progressView.setVisibility(View.GONE);
                    return false;
                }
            }).into(holder.profileIv).onLoadStarted(context.getDrawable(R.drawable.profilepic_placeholder));

        } else {
            holder.progressView.setVisibility(View.GONE);
        }


        if (feed.getType().equals("Image")) {
            //photo

            holder.feedimageIv.setVisibility(View.VISIBLE);
            holder.playicon.setVisibility(View.GONE);
            holder.VideoView.setVisibility(View.GONE);

            Glide.with(context).load((ApiConstant.newsfeedwall + feed.getMediaFile()))
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    holder.feedprogress.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    holder.feedprogress.setVisibility(View.GONE);
                    return false;
                }
            }).into(holder.feedimageIv).onLoadStarted(context.getDrawable(R.drawable.gallery_placeholder));

        } else if (feed.getType().equals("Video")) {
            //video

            holder.feedimageIv.setVisibility(View.GONE);
            holder.playicon.setVisibility(View.GONE);
            if (holder.feedprogress.getVisibility() == View.VISIBLE) {
                holder.feedprogress.setVisibility(View.GONE);
            }
            holder.VideoView.setVisibility(View.VISIBLE);

//
//            if (holder.VideoView.isCurrentPlay())
//            {
//
//                try{
//                    holder.VideoView.backPress();
//                    holder.VideoView.onStatePrepared();
////                    holder.VideoView.releaseAllVideos();
//                    holder.VideoView.playOnThisJzvd();
//
//                }catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//            }

            holder.VideoView.setUp(ApiConstant.newsfeedwall + feed.getMediaFile()
                    , JZVideoPlayerStandard.SCREEN_WINDOW_LIST, "");

            Glide.with(holder.VideoView.getContext()).load(ApiConstant.newsfeedwall + feed.getMediaFile()).into(holder.VideoView.thumbImageView);


//            JZVideoPlayer.setJzUserAction(new MyUserActionStandard());

//
//            holder.VideoView.thumbImageView.setImageURI(uri);
//            holder.VideoView.setUp(ApiConstant.newsfeedwall+feed.getMediaFile(),JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL,"");

//            Glide.with(context).load((ApiConstant.newsfeedwall + feed.getThumbImage()))
//                    .apply(RequestOptions.skipMemoryCacheOf(true))
//                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<Drawable>() {
//                @Override
//                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                    holder.feedprogress.setVisibility(View.GONE);
//                    return false;
//                }
//
//                @Override
//                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                    holder.feedprogress.setVisibility(View.GONE);
//                    return false;
//                }
//            }).into(holder.feedimageIv).onLoadStarted(context.getDrawable(R.drawable.gallery_placeholder));

        } else {
            holder.feedimageIv.setVisibility(View.GONE);
            holder.playicon.setVisibility(View.GONE);
            holder.feedprogress.setVisibility(View.GONE);
            holder.VideoView.setVisibility(View.GONE);

        }


        if (feed.getLikeFlag().equals("1")) {


            holder.img_like.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_afterlike, 0);
            setTextViewDrawableColor(holder.img_like, colorActive);


        } else {
            holder.img_like.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_like, 0);
        }

//
//        holder.likeTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.newsfeedlistingrow, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final NewsFeedList feed = feedLists.get(position);
        initialize(feed, holder, position);
    }

    private void weightapply(LinearLayout likeTv, LinearLayout commentTv, LinearLayout shareTv, View viewone, View viewtwo) {

        if (likeTv.getVisibility() == View.GONE && commentTv.getVisibility() == View.VISIBLE && shareTv.getVisibility() == View.VISIBLE) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.5f
            );
            commentTv.setLayoutParams(param);
            shareTv.setLayoutParams(param);

            viewone.setVisibility(View.GONE);
            viewtwo.setVisibility(View.VISIBLE);

        } else if (commentTv.getVisibility() == View.GONE && likeTv.getVisibility() == View.VISIBLE && shareTv.getVisibility() == View.VISIBLE) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.5f
            );
            likeTv.setLayoutParams(param);
            shareTv.setLayoutParams(param);

            viewone.setVisibility(View.VISIBLE);
            viewtwo.setVisibility(View.GONE);

        } else if (shareTv.getVisibility() == View.GONE && likeTv.getVisibility() == View.VISIBLE && commentTv.getVisibility() == View.VISIBLE) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.5f
            );

            likeTv.setLayoutParams(param);
            commentTv.setLayoutParams(param);

            viewone.setVisibility(View.VISIBLE);
            viewtwo.setVisibility(View.GONE);


        } else if (likeTv.getVisibility() == View.VISIBLE && commentTv.getVisibility() == View.VISIBLE && shareTv.getVisibility() == View.VISIBLE) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            );

            likeTv.setLayoutParams(param);
            commentTv.setLayoutParams(param);
            shareTv.setLayoutParams(param);


            viewone.setVisibility(View.VISIBLE);
            viewtwo.setVisibility(View.VISIBLE);

        } else if (shareTv.getVisibility() == View.VISIBLE && commentTv.getVisibility() == View.GONE && likeTv.getVisibility() == View.GONE) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    3.0f
            );
            shareTv.setLayoutParams(param);

            viewone.setVisibility(View.GONE);
            viewtwo.setVisibility(View.GONE);
        } else if (shareTv.getVisibility() == View.GONE && commentTv.getVisibility() == View.VISIBLE && likeTv.getVisibility() == View.GONE) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    3.0f
            );
            commentTv.setLayoutParams(param);

            viewone.setVisibility(View.GONE);
            viewtwo.setVisibility(View.GONE);

        } else if (shareTv.getVisibility() == View.GONE && commentTv.getVisibility() == View.GONE && likeTv.getVisibility() == View.VISIBLE) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    3.0f
            );

            likeTv.setLayoutParams(param);

            viewone.setVisibility(View.GONE);
            viewtwo.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return feedLists.size();
    }

    private void applysetting(List<EventSettingList> eventSettingLists) {

        for (int i = 0; i < eventSettingLists.size(); i++) {

            if (eventSettingLists.get(i).getFieldName().equals("news_feed_like")) {
                news_feed_like = eventSettingLists.get(i).getFieldValue();
            }

            if (eventSettingLists.get(i).getFieldName().equals("news_feed_comment")) {
                news_feed_comment = eventSettingLists.get(i).getFieldValue();
            }

            if (eventSettingLists.get(i).getFieldName().equals("news_feed_share")) {
                news_feed_share = eventSettingLists.get(i).getFieldValue();
            }
        }
    }

    public interface FeedAdapterListner {
        void onContactSelected(NewsFeedList feed, ImageView imageView);

        void likeTvViewOnClick(View v, NewsFeedList feed, int position, TextView likeimage, TextView liketext);

        void commentTvViewOnClick(View v, NewsFeedList feed);

        void shareTvFollowOnClick(View v, NewsFeedList feed);

        void moreTvFollowOnClick(View v, NewsFeedList feed, int position);

        void moreLikeListOnClick(View v, NewsFeedList feed, int position);

        void FeedEditOnClick(View v, NewsFeedList feed, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv, designationTv, companyTv, dateTv, headingTv, liketext, commenttext, sharetext, img_like;
        public ImageView profileIv, img_vol, img_playback;
        public ProgressBar progressView, feedprogress;
        public PixabayImageView feedimageIv;
        public ImageView playicon, moreIV, editIV;
        public View viewone, viewtwo;
        private LinearLayout likeTv, commentTv, shareTv;
        private MyJZVideoPlayerStandard VideoView;


        public MyViewHolder(View view) {
            super(view);

            prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            colorActive = prefs.getString("colorActive", "");


            nameTv = view.findViewById(R.id.nameTv);
            companyTv = view.findViewById(R.id.companyTv);
            designationTv = view.findViewById(R.id.designationTv);
            dateTv = view.findViewById(R.id.dateTv);
            headingTv = view.findViewById(R.id.headingTv);

            likeTv = view.findViewById(R.id.likeTv);
            commentTv = view.findViewById(R.id.commentTv);
            shareTv = view.findViewById(R.id.shareTv);
            img_like = view.findViewById(R.id.img_like);

            liketext = view.findViewById(R.id.liketext);
            commenttext = view.findViewById(R.id.commenttext);

            feedimageIv = view.findViewById(R.id.feedimageIv);
            VideoView = view.findViewById(R.id.videoplayer);

            profileIv = view.findViewById(R.id.profileIV);


            progressView = view.findViewById(R.id.progressView);
            feedprogress = view.findViewById(R.id.feedprogress);

            playicon = view.findViewById(R.id.playicon);
            moreIV = view.findViewById(R.id.moreIV);
            editIV = view.findViewById(R.id.editIV);

            viewone = view.findViewById(R.id.viewone);
            viewtwo = view.findViewById(R.id.viewtwo);


            mAPIService = ApiUtils.getAPIService();
            sessionManager = new SessionManager(context);

            eventSettingLists = SessionManager.loadEventList();
            liketext.setFocusable(true);

            if (eventSettingLists.size() != 0) {
                applysetting(eventSettingLists);
            }


            feedimageIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(feedLists.get(getAdapterPosition()), feedimageIv);
                }
            });


            img_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.likeTvViewOnClick(v, feedLists.get(getAdapterPosition()), getAdapterPosition(), img_like, liketext);
                }
            });

            commentTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.commentTvViewOnClick(v, feedLists.get(getAdapterPosition()));
                }
            });


            shareTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.shareTvFollowOnClick(v, feedLists.get(getAdapterPosition()));
                }
            });

            moreIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.moreTvFollowOnClick(v, feedLists.get(getAdapterPosition()), getAdapterPosition());

//                    feedLists.remove(getAdapterPosition());
//                    notifyItemRemoved(getAdapterPosition());
                }
            });


            liketext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.moreLikeListOnClick(v, feedLists.get(getAdapterPosition()), getAdapterPosition());

//                    feedLists.remove(getAdapterPosition());
//                    notifyItemRemoved(getAdapterPosition());
                }
            });


            editIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.FeedEditOnClick(v, feedLists.get(getAdapterPosition()), getAdapterPosition());

                }
            });


        }
    }


}