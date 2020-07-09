package com.procialize.mrgeApp20.NewsFeed.Views.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.procialize.mrgeApp20.Activity.AttendeeDetailActivity;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.CustomTools.ClickableViewPager;

import com.procialize.mrgeApp20.CustomTools.ScaledImageView;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.GetterSetter.AttendeeList;
import com.procialize.mrgeApp20.GetterSetter.EventSettingList;
import com.procialize.mrgeApp20.GetterSetter.NewsFeedList;
import com.procialize.mrgeApp20.GetterSetter.news_feed_media;
import com.procialize.mrgeApp20.NewsFeed.Views.Activity.PostNewActivity;
import com.procialize.mrgeApp20.NewsFeed.Views.Fragment.FragmentNewsFeed;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.Utility;

import org.apache.commons.lang3.StringEscapeUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.jzvd.JzvdStd;

import static android.content.Context.MODE_PRIVATE;
import static com.procialize.mrgeApp20.Session.ImagePathConstants.KEY_NEWSFEED_PATH;
import static com.procialize.mrgeApp20.Session.ImagePathConstants.KEY_NEWSFEED_PROFILE_PATH;

//import com.procialize.mrgeApp20.widget.ReactionView;

public class NewsFeedAdapterRecycler extends RecyclerView.Adapter<NewsFeedAdapterRecycler.MyViewHolder> {

    public static int swipableAdapterPosition = 0;
    public List<NewsFeedList> feedLists;
    int width;
    float height;
    String profilepic = "";
    String attendee_status = "";
    APIService mAPIService;
    SessionManager sessionManager;
    float p1;
    String news_feed_like = "0", news_feed_comment = "0", news_feed_share = "0";
    // List<EventSettingList> eventSettingLists;
    HashMap<String, String> user;
    String news_feed_post = "1", news_feed_images = "1", news_feed_video = "1", designatio = "1", company = "1", news_feed_gif = "0";
    String topMgmtFlag;
    Boolean value;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive,newsFeedPath,newsFeedProfilePath;
    ConnectionDetector cd;
    String substring;
    String device = Build.MODEL;
    RelativeLayout relative;
    List<news_feed_media> news_feed_media1;
    private List<AttendeeList> attendeeDBList;
    private Context context;
    private FeedAdapterListner listener;
    private LayoutInflater inflater;
    //  private List<AttendeeList> attendeeDBList;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private DBHelper dbHelper;
    private List<EventSettingList> eventSettingLists;

    public NewsFeedAdapterRecycler(Context con, List<NewsFeedList> feedLists, FeedAdapterListner listener, Boolean value, RelativeLayout _relative) {

        this.feedLists = feedLists;
        this.listener = listener;
        this.context = con;
        this.value = value;
        this.relative = _relative;
        if (con != null) {
            SessionManager sessionManager = new SessionManager(con);
            user = sessionManager.getUserDetails();
            profilepic = user.get(SessionManager.KEY_PIC);
            attendee_status = user.get(SessionManager.ATTENDEE_STATUS);
            topMgmtFlag = sessionManager.getSkipFlag();
            SharedPreferences prefs = con.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            colorActive = prefs.getString("colorActive", "");
            newsFeedPath = prefs.getString(KEY_NEWSFEED_PATH, "");
            newsFeedProfilePath = prefs.getString(KEY_NEWSFEED_PROFILE_PATH, "");
            cd = new ConnectionDetector(context);
        }

        procializeDB = new DBHelper(context);
        db = procializeDB.getWritableDatabase();
        dbHelper = new DBHelper(context);
    }


    public void removeItem(int position) {
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.newsfeedlistingrow, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        NewsFeedList feed;

        if (position == 0) {

            if (value == true) {

                holder.txtfeedRv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent postview = new Intent(context, PostNewActivity.class);
                        postview.putExtra("for", "text");
                        context.startActivity(postview);
                    }
                });


                holder.imagefeedRv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent postview = new Intent(context, PostNewActivity.class);
                        postview.putExtra("for", "image");
                        context.startActivity(postview);
//                getActivity().finish();
                    }
                });

                holder.videofeedRv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                       /* if (device.equalsIgnoreCase("vivo V3")) {
                            Intent postview = new Intent(context, PostActivityVideo.class);
                            postview.putExtra("for", "video");
                            context.startActivity(postview);
                        } else {
                            Intent postview = new Intent(context, PostViewActivity.class);
                            postview.putExtra("for", "video");
                            context.startActivity(postview);
                        }*/
                    }
                });
            } else {

                holder.txtfeedRv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        /*Intent postview = new Intent(context, PostViewActivity.class);
                        postview.putExtra("for", "text");
                        context.startActivity(postview);*/
                    }
                });


                holder.imagefeedRv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent postview = new Intent(context, PostNewActivity.class);
                        postview.putExtra("for", "image");
                        context.startActivity(postview);
//                getActivity().finish();
                    }
                });

                holder.videofeedRv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        /*if (device.equalsIgnoreCase("vivo V3")) {
                            Intent postview = new Intent(context, PostActivityVideo.class);
                            postview.putExtra("for", "video");
                            context.startActivity(postview);
                        } else {
                            Intent postview = new Intent(context, PostViewActivity.class);
                            postview.putExtra("for", "video");
                            context.startActivity(postview);
                        }*/
                    }
                });
            }
        } else {

            try {
                if (value == true) {

                    holder.txtfeedRv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent postview = new Intent(context, PostNewActivity.class);
                            postview.putExtra("for", "text");
                            context.startActivity(postview);
                        }
                    });


                    holder.imagefeedRv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent postview = new Intent(context, PostNewActivity.class);
                            postview.putExtra("for", "image");
                            context.startActivity(postview);
//                getActivity().finish();
                        }
                    });

                    holder.videofeedRv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                       /* if (device.equalsIgnoreCase("vivo V3")) {
                            Intent postview = new Intent(context, PostActivityVideo.class);
                            postview.putExtra("for", "video");
                            context.startActivity(postview);
                        } else {
                            Intent postview = new Intent(context, PostViewActivity.class);
                            postview.putExtra("for", "video");
                            context.startActivity(postview);
                        }*/
                        }
                    });
                } else {
                    holder.txtfeedRv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                       /* Intent postview = new Intent(context, PostViewActivity.class);
                        postview.putExtra("for", "text");
                        context.startActivity(postview);*/
                        }
                    });


                    holder.imagefeedRv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent postview = new Intent(context, PostNewActivity.class);
                            postview.putExtra("for", "image");
                            context.startActivity(postview);
                        }
                    });

                    holder.videofeedRv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                       /* if (device.equalsIgnoreCase("vivo V3")) {
                            Intent postview = new Intent(context, PostActivityVideo.class);
                            postview.putExtra("for", "video");
                            context.startActivity(postview);
                        } else {
                            Intent postview = new Intent(context, PostViewActivity.class);
                            postview.putExtra("for", "video");
                            context.startActivity(postview);
                        }*/
                        }
                    });
                }
            }catch (IllegalArgumentException e)
            {
                e.printStackTrace();
            }
        }

        //---------------For Pagination------------------------
        /*if ((position >= getItemCount() - 1))
            listener.load();*/
        //-------------------------------------------------------

        holder.nameTv.setTextColor(Color.parseColor(colorActive));
        feed = feedLists.get(position);
        if (feed.getLastName() == null) {
            holder.nameTv.setText(feed.getFirstName());
        } else {
            holder.nameTv.setText(feed.getFirstName() + " " + feed.getLastName());
        }

        if (designatio.equalsIgnoreCase("0")) {
            holder.designationTv.setVisibility(View.GONE);
        } else {
            holder.designationTv.setText(feed.getDesignation());
            holder.designationTv.setVisibility(View.VISIBLE);
        }

/*        if (company.equalsIgnoreCase("0")) {
            holder.tv_concat.setVisibility(View.GONE);
            holder.companyTv.setVisibility(View.GONE);
        } else {*/
            //holder.companyTv.setText(feed.getCompanyName());
            holder.companyTv.setText(feed.getCity());
            holder.companyTv.setVisibility(View.VISIBLE);
            holder.tv_concat.setVisibility(View.VISIBLE);
        /*}*/

        holder.testdata.setText(StringEscapeUtils.unescapeJava(feed.getPostStatus()));

        final SpannableStringBuilder stringBuilder = new SpannableStringBuilder(holder.testdata.getText());
        if (feed.getPostStatus() != null) {

            if (!feed.getPostStatus().isEmpty())
                holder.headingTv.setVisibility(View.VISIBLE);
            else
                holder.headingTv.setVisibility(View.GONE);
//                    holder.wallNotificationText.setText(getEmojiFromString(notificationImageStatus));
            int flag = 0;
            for (int i = 0; i < stringBuilder.length(); i++) {
                String sample = stringBuilder.toString();
                if ((stringBuilder.charAt(i) == '<')) {
                    try {
                        String text = "<";
                        String text1 = ">";

                        if (flag == 0) {
                            int start = sample.indexOf(text, i);
                            int end = sample.indexOf(text1, i);

                            Log.v("Indexes of", "Start : " + start + "," + end);
                            try {
                                substring = sample.substring(start, end + 1);
                                Log.v("String names: ", substring);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            if (substring.contains("<")) {
                                if (sample.contains(substring)) {
                                    substring = substring.replace("<", "");
                                    substring = substring.replace(">", "");
                                    int index = substring.indexOf("^");
//                                    substring = substring.replace("^", "");
                                    final String attendeeid = substring.substring(0, index);
                                    substring = substring.substring(index + 1, substring.length());


                                    stringBuilder.setSpan(stringBuilder, start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    stringBuilder.setSpan(new ForegroundColorSpan(Color.RED), start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


                                    stringBuilder.setSpan(new ClickableSpan() {
                                        @Override
                                        public void onClick(View widget) {
                                            attendeeDBList = dbHelper.getAttendeeDetailsId(attendeeid);
                                            Intent intent = new Intent(context, AttendeeDetailActivity.class);
                                            intent.putExtra("id", attendeeDBList.get(0).getAttendeeId());
                                            intent.putExtra("name", attendeeDBList.get(0).getFirstName() + " " + attendeeDBList.get(0).getLastName());
                                            intent.putExtra("city", attendeeDBList.get(0).getCity());
                                            intent.putExtra("country", attendeeDBList.get(0).getCountry());
                                            intent.putExtra("company", attendeeDBList.get(0).getCompanyName());
                                            intent.putExtra("designation", attendeeDBList.get(0).getDesignation());
                                            intent.putExtra("description", attendeeDBList.get(0).getDescription());
                                            intent.putExtra("profile", attendeeDBList.get(0).getProfilePic());
                                            intent.putExtra("mobile", attendeeDBList.get(0).getMobile());
                                            intent.putExtra("buddy_status", attendeeDBList.get(0).getBuddy_status());

                                            context.startActivity(intent);
                                        }
                                    }, start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    stringBuilder.replace(start, end + 1, substring);
                                    holder.testdata.setText(stringBuilder, TextView.BufferType.SPANNABLE);
                                    holder.headingTv.setMovementMethod(LinkMovementMethod.getInstance());
                                    holder.headingTv.setText(stringBuilder);
                                    flag = 1;
//                        holder.attendee_comments.setText(attendees.getComment().indexOf(substring, start));
//                        holder.attendee_comments.setText(attendees.getComment().indexOf(substring, start));
//                        attendees.setComment(substring);
                                }
                            }
                        } else {

                            int start = sample.indexOf(text, i);
                            int end = sample.indexOf(text1, i);

                            Log.v("Indexes of", "Start : " + start + "," + end);
                            try {
                                substring = sample.substring(start, end + 1);
                                Log.v("String names: ", substring);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (substring.contains("<")) {
                                if (sample.contains(substring)) {
                                    substring = substring.replace("<", "");
                                    substring = substring.replace(">", "");
                                    int index = substring.indexOf("^");
//                                    substring = substring.replace("^", "");
                                    final String attendeeid = substring.substring(0, index);
                                    substring = substring.substring(index + 1, substring.length());


                                    stringBuilder.setSpan(stringBuilder, start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    stringBuilder.setSpan(new ForegroundColorSpan(Color.RED), start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                    stringBuilder.setSpan(new ClickableSpan() {
                                        @Override
                                        public void onClick(View widget) {
                                            attendeeDBList = dbHelper.getAttendeeDetailsId(attendeeid);
                                            Intent intent = new Intent(context, AttendeeDetailActivity.class);
                                            intent.putExtra("id", attendeeDBList.get(0).getAttendeeId());
                                            intent.putExtra("name", attendeeDBList.get(0).getFirstName() + " " + attendeeDBList.get(0).getLastName());
                                            intent.putExtra("city", attendeeDBList.get(0).getCity());
                                            intent.putExtra("country", attendeeDBList.get(0).getCountry());
                                            intent.putExtra("company", attendeeDBList.get(0).getCompanyName());
                                            intent.putExtra("designation", attendeeDBList.get(0).getDesignation());
                                            intent.putExtra("description", attendeeDBList.get(0).getDescription());
                                            intent.putExtra("profile", attendeeDBList.get(0).getProfilePic());
                                            intent.putExtra("mobile", attendeeDBList.get(0).getMobile());
                                            intent.putExtra("buddy_status", attendeeDBList.get(0).getBuddy_status());

                                            context.startActivity(intent);
                                        }
                                    }, start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                    stringBuilder.replace(start, end + 1, substring);
                                    holder.testdata.setText(stringBuilder, TextView.BufferType.SPANNABLE);
                                    holder.headingTv.setMovementMethod(LinkMovementMethod.getInstance());

                                    holder.headingTv.setText(stringBuilder);


//                        holder.attendee_comments.setText(attendees.getComment().indexOf(substring, start));
//                        holder.attendee_comments.setText(attendees.getComment().indexOf(substring, start));
//                        attendees.setComment(substring);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            holder.headingTv.setText(stringBuilder);
        } else {
            holder.headingTv.setVisibility(View.GONE);
        }

        if(feed.getTotalLikes()!=null) {
            if (feed.getTotalLikes().equals("1")) {
                holder.liketext.setText(feed.getTotalLikes() + " Like ");
            } else {
                holder.liketext.setText(feed.getTotalLikes() + " Likes ");
            }
        }
        if(feed.getTotalComments()!=null) {
            if (feed.getTotalComments().equals("1")) {
                holder.commenttext.setText(feed.getTotalComments() + " Comment ");
            } else {
                holder.commenttext.setText(feed.getTotalComments() + " Comments ");
            }
        }


        mAPIService = ApiUtils.getAPIService();
        sessionManager = new SessionManager(context);

        eventSettingLists = SessionManager.loadEventList();
        holder.liketext.setFocusable(true);

        if (attendee_status.equalsIgnoreCase("1")) {
            if (user.get(SessionManager.KEY_ID).equalsIgnoreCase(feedLists.get(position).getAttendeeId())) {
                holder.editIV.setVisibility(View.GONE);
                //holder.moreIV.setVisibility(View.VISIBLE);
            } else {
                holder.editIV.setVisibility(View.GONE);
               // holder.moreIV.setVisibility(View.VISIBLE);
            }
        } else {
            if (user.get(SessionManager.KEY_ID).equalsIgnoreCase(feedLists.get(position).getAttendeeId())) {
                holder.editIV.setVisibility(View.GONE);
              //  holder.moreIV.setVisibility(View.VISIBLE);
            } else {
                holder.editIV.setVisibility(View.GONE);
              //  holder.moreIV.setVisibility(View.GONE);
            }
        }
        if (feed.getLike_type() == null) {
            holder.img_like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like));
        } else if (feed.getLike_type().equals("")) {
            holder.img_like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like));
        } else if (feed.getLike_type().equals("0")) {
            //holder.img_like.setImageDrawable(context.getResources().getDrawable(R.drawable.like_0));

            holder.img_like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_afterlike));
            int color = Color.parseColor(colorActive);
            holder.img_like.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

        }/* else if (feed.getLike_type().equals("1")) {
            holder.img_like.setImageDrawable(context.getResources().getDrawable(R.drawable.love_1));
        } else if (feed.getLike_type().equals("2")) {
            holder.img_like.setImageDrawable(context.getResources().getDrawable(R.drawable.smile_2));
        } else if (feed.getLike_type().equals("3")) {
            holder.img_like.setImageDrawable(context.getResources().getDrawable(R.drawable.haha_3));
        } else if (feed.getLike_type().equals("4")) {
            holder.img_like.setImageDrawable(context.getResources().getDrawable(R.drawable.wow_4));
        } else if (feed.getLike_type().equals("5")) {
            holder.img_like.setImageDrawable(context.getResources().getDrawable(R.drawable.sad_5));
        } else if (feed.getLike_type().equals("6")) {
            holder.img_like.setImageDrawable(context.getResources().getDrawable(R.drawable.angry_6));
        }
*/
        holder.img_like.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                width = holder.root.getMeasuredWidth();
                height = holder.root.getMeasuredHeight();
                /*if (cd.isConnectingToInternet()) {
                    ReactionView rvl = new ReactionView(context, feedLists.get(position), position, holder.img_like, holder.liketext, holder.root, relative,height);
                    holder.root.addView(rvl);
                } else {
                    Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }*/

                return true;
            }
        });

        if (feed.getPostDate() != null) {
            SimpleDateFormat formatter = null;

            String formate1 = ApiConstant.dateformat;
            String formate2 = ApiConstant.dateformat1;

            if (Utility.isValidFormat(formate1, feed.getPostDate(), Locale.UK)) {
                formatter = new SimpleDateFormat(ApiConstant.dateformat);
            } else if (Utility.isValidFormat(formate2, feed.getPostDate(), Locale.UK)) {
                formatter = new SimpleDateFormat(ApiConstant.dateformat1);
            }

            try {
                Date date1 = formatter.parse(feed.getPostDate());

                //DateFormat originalFormat = new SimpleDateFormat("dd MMM , HH:mm", Locale.UK);
                //DateFormat originalFormat = new SimpleDateFormat("dd MMM HH:mm", Locale.UK);
                DateFormat originalFormat = new SimpleDateFormat("dd MMMM hh:mm a", Locale.UK);

                String date = originalFormat.format(date1);

                holder.dateTv.setText(date);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        if (feed.getProfilePic() != null) {
            Glide.with(context).load((
                    newsFeedProfilePath/*ApiConstant.profilepic*/ + feed.getProfilePic()))
                    .placeholder(R.drawable.profilepic_placeholder)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)).circleCrop().centerCrop()
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            //feedprogress.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            //feedprogress.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(holder.profileIv);

        } else {
            holder.progressView.setVisibility(View.GONE);
            holder.profileIv.setImageResource(R.drawable.profilepic_placeholder);
        }

        if (feed.getNews_feed_media() != null) {
            if (feed.getNews_feed_media().size() > 0) {

                news_feed_media1 = feed.getNews_feed_media();

                if (news_feed_media1.size() >= 1) {
                    holder.feedimageIv.setVisibility(View.GONE);
                    holder.playicon.setVisibility(View.GONE);
                    holder.VideoView.setVisibility(View.GONE);
                    holder.viewPager.setVisibility(View.VISIBLE);
                    holder.recycler_slider.setVisibility(View.VISIBLE);
                    holder.pager_dots.setVisibility(View.VISIBLE);
                    holder.card_view.setVisibility(View.VISIBLE);

                    final ArrayList<String> imagesSelectednew = new ArrayList<>();
                    final ArrayList<String> imagesSelectednew1 = new ArrayList<>();
                    final ImageView[] ivArrayDotsPager;
                    for (int i = 0; i < news_feed_media1.size(); i++) {
                        imagesSelectednew.add(newsFeedPath /*ApiConstant.newsfeedwall*/ + news_feed_media1.get(i).getMediaFile());
                        if (news_feed_media1.get(i).getMediaFile().contains("mp4")) {
                            imagesSelectednew1.add(newsFeedPath /*ApiConstant.newsfeedwall*/ + news_feed_media1.get(i).getThumb_image());
                        } else {
                            imagesSelectednew1.add("");
                        }
                    }
                    final SwipeMultimediaAdapter swipepagerAdapter = new SwipeMultimediaAdapter(context, imagesSelectednew, imagesSelectednew1, news_feed_media1);
                    holder.viewPager.setAdapter(swipepagerAdapter);
                    swipepagerAdapter.notifyDataSetChanged();

                   /* SwipeableRecyclerAdapter    swipeableRecyclerAdapter = new SwipeableRecyclerAdapter(context, imagesSelectednew, imagesSelectednew1, news_feed_media1);
                  //  RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                    LinearLayoutManager  HorizontalLayout
                            = new LinearLayoutManager(
                            context,
                            LinearLayoutManager.HORIZONTAL,
                            false);
                    holder.recycler_slider.setLayoutManager(HorizontalLayout);
                  //  holder.recycler_slider.setLayoutManager(mLayoutManager);
                    holder.recycler_slider.setItemAnimator(new DefaultItemAnimator());
                    holder.recycler_slider.setAdapter(swipeableRecyclerAdapter);*/

                    if (imagesSelectednew.size() > 1) {
                        ivArrayDotsPager = new ImageView[imagesSelectednew.size()];
                        setupPagerIndidcatorDots(0, holder.pager_dots, imagesSelectednew.size());
                        holder.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                    JzvdStd.goOnPlayOnPause();
                                   // JzvdStd.releaseAllVideos();
//                                WallFragment_POST.newsfeedrefresh.setEnabled(false);

                                }

                                @Override
                                public void onPageSelected(int position1) {
                                    JzvdStd.goOnPlayOnPause();
                                    swipableAdapterPosition = position1;
                                    setupPagerIndidcatorDots(position1, holder.pager_dots, imagesSelectednew.size());
//                                WallFragment_POST.newsfeedrefresh.setEnabled(false);
                                }

                            @Override
                            public void onPageScrollStateChanged(int state) {
                                JzvdStd.goOnPlayOnPause();
//                                WallFragment_POST.newsfeedrefresh.setEnabled(false);

                            }
                        });
                    } else {
                        holder.pager_dots.setVisibility(View.GONE);
                    }

                    holder.viewPager.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {

                            switch (event.getAction()) {
                                case MotionEvent.ACTION_MOVE:
                                    FragmentNewsFeed.newsfeedrefresh.setEnabled(false);
                                    break;
                                case MotionEvent.ACTION_UP:
                                case MotionEvent.ACTION_CANCEL:
                                    FragmentNewsFeed.newsfeedrefresh.setEnabled(true);
                                    break;
                            }
                            return false;
                        }
                    });
                }
            } else {
                holder.feedimageIv.setVisibility(View.GONE);
                holder.playicon.setVisibility(View.GONE);
                holder.feedprogress.setVisibility(View.GONE);
                holder.VideoView.setVisibility(View.GONE);
                holder.viewPager.setVisibility(View.GONE);
                holder.recycler_slider.setVisibility(View.GONE);
                holder.pager_dots.setVisibility(View.GONE);
                holder.card_view.setVisibility(View.GONE);
            }
        } else {
            holder.feedimageIv.setVisibility(View.GONE);
            holder.playicon.setVisibility(View.GONE);
            holder.feedprogress.setVisibility(View.GONE);
            holder.VideoView.setVisibility(View.GONE);
            holder.viewPager.setVisibility(View.GONE);
            holder.recycler_slider.setVisibility(View.GONE);
            holder.pager_dots.setVisibility(View.GONE);
            holder.card_view.setVisibility(View.GONE);
        }

        if (eventSettingLists.size() != 0) {
            applysetting(eventSettingLists);
        }

        if (news_feed_post.equalsIgnoreCase("0")) {
            holder.txtfeedRv.setVisibility(View.GONE);

            holder.view.setVisibility(View.GONE);
        } else {
            holder.txtfeedRv.setVisibility(View.VISIBLE);

            holder.view.setVisibility(View.VISIBLE);
        }

        if (news_feed_images.equalsIgnoreCase("0")) {
            holder.imagefeedRv.setVisibility(View.GONE);
            holder.viewteo.setVisibility(View.GONE);
        } else {
            holder.imagefeedRv.setVisibility(View.VISIBLE);
            holder.viewteo.setVisibility(View.VISIBLE);
        }

        if (news_feed_video.equalsIgnoreCase("0")) {
            holder.videofeedRv.setVisibility(View.GONE);
            holder.viewteo.setVisibility(View.GONE);
        } else {
            holder.videofeedRv.setVisibility(View.VISIBLE);
            holder.viewteo.setVisibility(View.VISIBLE);
        }

        if (news_feed_images.equalsIgnoreCase("0") && news_feed_post.equalsIgnoreCase("0") && news_feed_video.equalsIgnoreCase("0")) {
            holder.mainLLpost.setVisibility(View.GONE);
//            mindTv.setVisibility(View.VISIBLE);
        } else {

            holder.mainLLpost.setVisibility(View.GONE);
//            mindTv.setVisibility(View.VISIBLE);
        }

        holder.feedimageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send selected contact in callback
                listener.onContactSelected(feedLists.get(position), holder.feedimageIv, position);
            }
        });


        holder.img_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    listener.likeTvViewOnClick(v, feedLists.get(position), position, holder.img_like, holder.liketext);
                } else {
                    Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.commentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.commentTvViewOnClick(v, feedLists.get(position), position);

            }
        });
        holder.commenttext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.commentTvViewOnClick(v, feedLists.get(position), position);

            }
        });


        holder.shareTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //listener.shareTvFollowOnClick(v, feedLists.get(position));
                listener.shareTvFollowOnClick(v, feedLists.get(position), holder.feedimageIv, position);
            }
        });

        holder.moreIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.moreTvFollowOnClick(v, feedLists.get(position), position);

//                    feedLists.remove(getAdapterPosition());
//                    notifyItemRemoved(getAdapterPosition());
            }
        });


        holder.liketext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.moreLikeListOnClick(v, feedLists.get(position), position);


            }
        });


        holder.editIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.FeedEditOnClick(v, feedLists.get(position), position);

            }
        });

       /* profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = ApiConstant.profilepic + feedLists.get(position).getProfilePic();
                imagealert(url);
            }
        });
*/
       /* holder.nameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (feedLists.get(position).getAttendee_type().equalsIgnoreCase("A")) {
                    attendeeDBList = dbHelper.getAttendeeDetailsId(feedLists.get(position).getAttendeeId());
                    Intent intent = new Intent(context, AttendeeDetailActivity.class);
                    intent.putExtra("id", feedLists.get(position).getAttendeeId());
                    intent.putExtra("name", feedLists.get(position).getFirstName() + " " + feedLists.get(position).getLastName());
                    intent.putExtra("city", attendeeDBList.get(0).getCity());
                    intent.putExtra("country", attendeeDBList.get(0).getCountry());
                    intent.putExtra("company", feedLists.get(position).getCompanyName());
                    intent.putExtra("designation", feedLists.get(position).getDesignation());
                    intent.putExtra("description", attendeeDBList.get(0).getDescription());
                    intent.putExtra("profile", feedLists.get(position).getProfilePic());
                    context.startActivity(intent);
                }
            }
        });*/


        if (news_feed_like.equalsIgnoreCase("0")) {
            holder.likeTv.setVisibility(View.GONE);
            holder.liketext.setVisibility(View.GONE);
//            viewone.setVisibility(View.GONE);
        } else {
            holder.likeTv.setVisibility(View.VISIBLE);
            holder.liketext.setVisibility(View.VISIBLE);
//            viewone.setVisibility(View.VISIBLE);
        }

        if (news_feed_comment.equalsIgnoreCase("0")) {
            holder.commentTv.setVisibility(View.GONE);
            holder.commenttext.setVisibility(View.GONE);
//            viewtwo.setVisibility(View.GONE);
        } else {
            holder.commentTv.setVisibility(View.VISIBLE);
            holder.commenttext.setVisibility(View.VISIBLE);
//            viewtwo.setVisibility(View.VISIBLE);
        }

        if (news_feed_share.equalsIgnoreCase("0")) {
            holder.shareTv.setVisibility(View.GONE);
//            viewtwo.setVisibility(View.GONE);
        } else {
            holder.shareTv.setVisibility(View.VISIBLE);
//            viewtwo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return feedLists.size();
    }

    private void weightapply(RelativeLayout likeTv, RelativeLayout
            commentTv, RelativeLayout shareTv, View viewone, View viewtwo) {

        if (likeTv.getVisibility() == View.GONE && commentTv.getVisibility() == View.VISIBLE && shareTv.getVisibility() == View.VISIBLE) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.5f
            );
            commentTv.setLayoutParams(param);
            shareTv.setLayoutParams(param);

//            viewone.setVisibility(View.GONE);
//            viewtwo.setVisibility(View.VISIBLE);

        } else if (commentTv.getVisibility() == View.GONE && likeTv.getVisibility() == View.VISIBLE && shareTv.getVisibility() == View.VISIBLE) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.5f
            );
            likeTv.setLayoutParams(param);
            shareTv.setLayoutParams(param);

//            viewone.setVisibility(View.VISIBLE);
//            viewtwo.setVisibility(View.GONE);

        } else if (shareTv.getVisibility() == View.GONE && likeTv.getVisibility() == View.VISIBLE && commentTv.getVisibility() == View.VISIBLE) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.5f
            );

            likeTv.setLayoutParams(param);
            commentTv.setLayoutParams(param);

//            viewone.setVisibility(View.VISIBLE);
//            viewtwo.setVisibility(View.GONE);


        } else if (likeTv.getVisibility() == View.VISIBLE && commentTv.getVisibility() == View.VISIBLE && shareTv.getVisibility() == View.VISIBLE) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            );

            likeTv.setLayoutParams(param);
            commentTv.setLayoutParams(param);
            shareTv.setLayoutParams(param);


//            viewone.setVisibility(View.VISIBLE);
//            viewtwo.setVisibility(View.VISIBLE);

        } else if (shareTv.getVisibility() == View.VISIBLE && commentTv.getVisibility() == View.GONE && likeTv.getVisibility() == View.GONE) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    3.0f
            );
            shareTv.setLayoutParams(param);

//            viewone.setVisibility(View.GONE);
//            viewtwo.setVisibility(View.GONE);
        } else if (shareTv.getVisibility() == View.GONE && commentTv.getVisibility() == View.VISIBLE && likeTv.getVisibility() == View.GONE) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    3.0f
            );
            commentTv.setLayoutParams(param);

//            viewone.setVisibility(View.GONE);
//            viewtwo.setVisibility(View.GONE);

        } else if (shareTv.getVisibility() == View.GONE && commentTv.getVisibility() == View.GONE && likeTv.getVisibility() == View.VISIBLE) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    3.0f
            );

            likeTv.setLayoutParams(param);

//            viewone.setVisibility(View.GONE);
//            viewtwo.setVisibility(View.GONE);
        }

    }

    private void applysetting(List<EventSettingList> eventSettingLists) {

        for (int i = 0; i < eventSettingLists.size(); i++) {

           /* if (eventSettingLists.get(i).getFieldName().equals("news_feed_like")) {
                news_feed_like = eventSettingLists.get(i).getFieldValue();
            }

            if (eventSettingLists.get(i).getFieldName().equals("news_feed_comment")) {
                news_feed_comment = eventSettingLists.get(i).getFieldValue();
            }

            if (eventSettingLists.get(i).getFieldName().equals("news_feed_share")) {
                news_feed_share = eventSettingLists.get(i).getFieldValue();
            }
            if (eventSettingLists.get(i).getFieldName().equalsIgnoreCase("news_feed_video")) {
                news_feed_video = eventSettingLists.get(i).getFieldValue();
            }
            if (eventSettingLists.get(i).getFieldName().equalsIgnoreCase("news_feed_post")) {
                news_feed_post = eventSettingLists.get(i).getFieldValue();
            }
            if (eventSettingLists.get(i).getFieldName().equalsIgnoreCase("news_feed_images")) {
                news_feed_images = eventSettingLists.get(i).getFieldValue();
            }
            if (eventSettingLists.get(i).getFieldName().equalsIgnoreCase("edit_profile_designation")) {
                designatio = eventSettingLists.get(i).getFieldValue();
            }
            if (eventSettingLists.get(i).getFieldName().equalsIgnoreCase("edit_profile_company")) {
                company = eventSettingLists.get(i).getFieldValue();
            }*/
            if (eventSettingLists.get(i).getFieldName().equals("news_feed")) {
                if (eventSettingLists.get(i).getSub_menuList() != null) {
                    if (eventSettingLists.get(i).getSub_menuList().size() > 0) {
                        for (int k = 0; k < eventSettingLists.get(i).getSub_menuList().size(); k++) {
                            if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("news_feed_post")) {

                                news_feed_post = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            } else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("news_feed_images")) {
                                news_feed_images = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            } else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("news_feed_video")) {
                                news_feed_video = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            } else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("news_feed_comment")) {
                                news_feed_comment = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            } else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("news_feed_like")) {
                                news_feed_like = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            } else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("news_feed_share")) {
                                news_feed_share = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            } else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("news_feed_gif")) {
                                news_feed_gif = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            }
                        }
                    }
                }


            }

            if (eventSettingLists.get(i).getFieldName().equalsIgnoreCase("edit_profile_designation")) {
                designatio = eventSettingLists.get(i).getFieldValue();
            }
            if (eventSettingLists.get(i).getFieldName().equalsIgnoreCase("edit_profile_company")) {
                company = eventSettingLists.get(i).getFieldValue();
            }
        }
    }

    private void setupPagerIndidcatorDots(int currentPage, LinearLayout ll_dots, int size) {

        TextView[] dots = new TextView[size];
        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(context);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            dots[i].setTextColor(Color.parseColor("#343434"));
            ll_dots.addView(dots[i]);
        }

        try {
            if (dots.length > 0) {
                if (dots.length != currentPage) {
                    dots[currentPage].setTextColor(Color.parseColor("#A2A2A2"));
                }
            }
        } catch (Exception e) {

        }

    }

    public interface FeedAdapterListner {
        //void onContactSelected(NewsFeedList feed, ImageView imageView);
        void onContactSelected(NewsFeedList feed, ImageView imageView, int position);

        void likeTvViewOnClick(View v, NewsFeedList feed, int position, ImageView likeimage, TextView liketext);

        void commentTvViewOnClick(View v, NewsFeedList feed, int position);

        // void shareTvFollowOnClick(View v, NewsFeedList feed);
        void shareTvFollowOnClick(View v, NewsFeedList feedList, ImageView imageView, int position);

        void moreTvFollowOnClick(View v, NewsFeedList feed, int position);

        void moreLikeListOnClick(View v, NewsFeedList feed, int position);

        void FeedEditOnClick(View v, NewsFeedList feed, int position);

        void load();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv, designationTv, tv_concat, companyTv, dateTv, headingTv, liketext, commenttext, sharetext, testdata;
        public ImageView img_like;
        public ImageView img_vol, img_playback;
        public ProgressBar progressView, feedprogress;
        public ScaledImageView feedimageIv, profilestatus;
        public ImageView profileIv;
        public ImageView playicon, moreIV, editIV;
        public View viewone, viewtwo, viewteo, view;
        public JzvdStd VideoView;
        public ClickableViewPager viewPager;
        RecyclerView recycler_slider;
        RelativeLayout txtfeedRv, imagefeedRv, videofeedRv;
        FrameLayout root;
        LinearLayout pager_dots;
        LinearLayout linear_video;
        public LinearLayout viewForeground;
        CardView card_view;
        private LinearLayout likeTv, commentTv, shareTv, mainLLpost, post_layout, feedll, ll_bottom;

        public MyViewHolder(View convertView) {
            super(convertView);

            viewForeground = convertView.findViewById(R.id.viewForeground);
            nameTv = convertView.findViewById(R.id.nameTv);
            companyTv = convertView.findViewById(R.id.companyTv);
            tv_concat = convertView.findViewById(R.id.tv_concat);
            designationTv = convertView.findViewById(R.id.designationTv);
            dateTv = convertView.findViewById(R.id.dateTv);
            headingTv = convertView.findViewById(R.id.headingTv);
            testdata = convertView.findViewById(R.id.testdata);

            likeTv = convertView.findViewById(R.id.likeTv);
            commentTv = convertView.findViewById(R.id.commentTv);
            shareTv = convertView.findViewById(R.id.shareTv);
            img_like = convertView.findViewById(R.id.img_like);

            liketext = convertView.findViewById(R.id.liketext);
            commenttext = convertView.findViewById(R.id.commenttext);
            pager_dots = convertView.findViewById(R.id.ll_dots);
            card_view = convertView.findViewById(R.id.card_view);
            viewPager = convertView.findViewById(R.id.vp_slider);
            recycler_slider = convertView.findViewById(R.id.recycler_slider);
            feedimageIv = convertView.findViewById(R.id.feedimageIv);
            VideoView = convertView.findViewById(R.id.videoplayer);

            profileIv = convertView.findViewById(R.id.profileIV);


            progressView = convertView.findViewById(R.id.progressView);
            feedprogress = convertView.findViewById(R.id.feedprogress);
            root = convertView.findViewById(R.id.root);
            playicon = convertView.findViewById(R.id.playicon);
            moreIV = convertView.findViewById(R.id.moreIV);
            editIV = convertView.findViewById(R.id.editIV);

            viewone = convertView.findViewById(R.id.viewone);
            viewtwo = convertView.findViewById(R.id.viewtwo);

            txtfeedRv = convertView.findViewById(R.id.txtfeedRv);
//         r.mindTv = convertView.findViewById(R.id.mindTv);
            imagefeedRv = convertView.findViewById(R.id.imagefeedRv);
            videofeedRv = convertView.findViewById(R.id.videofeedRv);
            post_layout = convertView.findViewById(R.id.post_layout);
            feedll = convertView.findViewById(R.id.feedll);

            view = convertView.findViewById(R.id.view);
            viewteo = convertView.findViewById(R.id.viewteo);


            mainLLpost = convertView.findViewById(R.id.mainLLpost);
            ll_bottom = convertView.findViewById(R.id.ll_bottom);


            profilestatus = convertView.findViewById(R.id.profilestatus);
            if (feedLists.size() > 0) {
                feedll.setVisibility(RelativeLayout.VISIBLE);


            } else {
                feedll.setVisibility(RelativeLayout.GONE);

            }


            weightapply(txtfeedRv, imagefeedRv, videofeedRv, viewone, viewteo);

        }
    }

}
