package com.procialize.frontier.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.procialize.frontier.GetterSetter.LivePollList;
import com.procialize.frontier.GetterSetter.LivePollOptionList;
import com.procialize.frontier.R;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.procialize.frontier.Session.ImagePathConstants.KEY_LIVE_POLL_LOGO;
import static com.procialize.frontier.Session.ImagePathConstants.KEY_LIVE_POLL_LOGO_PATH;

public class PollNewAdapter extends BaseAdapter {
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    private List<LivePollList> pollLists;
    private List<LivePollOptionList> optionLists;
    private Context context;
    private PollAdapterListner listener;
    private LayoutInflater inflater;

    public PollNewAdapter(Context context, List<LivePollList> pollLists, List<LivePollOptionList> optionLists, PollAdapterListner listener) {
        this.pollLists = pollLists;
        this.optionLists = optionLists;
        this.listener = listener;
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

    }

    @Override
    public int getCount() {
        return pollLists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        if (getCount() > 0) {
            return getCount();
        } else {
            return super.getViewTypeCount();
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final LivePollList pollList = pollLists.get(position);
        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.poll_row, null);

            holder = new ViewHolder();

            Display dispDefault = ((WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay();
            int totalwidth = dispDefault.getWidth();
            holder.nameTv = convertView.findViewById(R.id.nameTv);
            holder.imageIv = convertView.findViewById(R.id.imageIv);
            holder.profileIV = convertView.findViewById(R.id.profileIV);
            holder.mainLL = convertView.findViewById(R.id.mainLL);
            holder.linMain = convertView.findViewById(R.id.linMain);
            holder.ivewComplete = convertView.findViewById(R.id.ivewComplete);
            holder.statusTv = convertView.findViewById(R.id.statusTv);


            holder.relative = (RelativeLayout) convertView
                    .findViewById(R.id.relative);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //holder.mainLL.setBackgroundColor(Color.parseColor(colorActive));

        if(pollList.getStatus().equalsIgnoreCase("Tap To Participate")){
            holder.statusTv.setVisibility(View.VISIBLE);
            holder.statusTv.setText("Tap To Participate");

            holder.ivewComplete.setVisibility(View.GONE);
        }else{
            holder.statusTv.setVisibility(View.VISIBLE);
            holder.statusTv.setText("Participated");
            holder.ivewComplete.setVisibility(View.VISIBLE);
            holder.ivewComplete.setBackgroundColor(Color.parseColor(colorActive));
        }
        try{
        holder.nameTv.setText(StringEscapeUtils.unescapeJava(pollList.getQuestion()));
        }catch (IllegalArgumentException e){
            e.printStackTrace();

        }
        holder.linMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onContactSelected(pollLists.get(position));
            }
        });

        if(position==0){
            holder.relative.setVisibility(View.VISIBLE);
            SharedPreferences prefs1 = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String logoPath = prefs1.getString(KEY_LIVE_POLL_LOGO_PATH,"");
            String strAppLivePollLogo =  prefs1.getString(KEY_LIVE_POLL_LOGO,"");

            Glide.with(context).load(logoPath + strAppLivePollLogo)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).circleCrop().centerCrop().listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    //progressBar.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    // progressBar.setVisibility(View.GONE);
                    return false;
                }
            }).into(holder.profileIV);
        }else{
            holder.relative.setVisibility(View.GONE);

        }




        return convertView;
    }

    public interface PollAdapterListner {
        void onContactSelected(LivePollList pollList);
    }

    static class ViewHolder {
        public TextView nameTv,statusTv;
        public ImageView imageIv,profileIV;
        public LinearLayout mainLL,linMain;
        RelativeLayout relative;
        View ivewComplete;
    }
}