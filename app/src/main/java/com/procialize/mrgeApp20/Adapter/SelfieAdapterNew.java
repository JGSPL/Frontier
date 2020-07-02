package com.procialize.mrgeApp20.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.github.barteksc.pdfviewer.util.Constants;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.GetterSetter.SelfieList;
import com.procialize.mrgeApp20.R;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.procialize.mrgeApp20.Session.ImagePathConstants.KEY_SELFIE_URL_PATH;

public class SelfieAdapterNew extends BaseAdapter {

    List<SelfieList> selfieList;
    Activity activity;
    Constants constant = new Constants();
    String MY_PREFS_NAME = "ProcializeInfo";
    String colorActive,picPath;
    private LayoutInflater inflater;

    public SelfieAdapterNew(Activity context, List<SelfieList> selfieList) {
        activity = context;
        this.selfieList = selfieList;

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");
        picPath = prefs.getString(KEY_SELFIE_URL_PATH, "");
    }

    public static String getEmojiFromString(String emojiString) {

        if (!emojiString.contains("\\u")) {

            return emojiString;
        }
        String EmojiEncodedString = "";

        int position = emojiString.indexOf("\\u");

        while (position != -1) {

            if (position != 0) {
                EmojiEncodedString += emojiString.substring(0, position);
            }

            String token = emojiString.substring(position + 2, position + 6);
            emojiString = emojiString.substring(position + 6);
            EmojiEncodedString += (char) Integer.parseInt(token, 16);
            position = emojiString.indexOf("\\u");
        }
        EmojiEncodedString += emojiString;

        return EmojiEncodedString;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return selfieList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public SelfieList getImageFromList(int position) {
        return selfieList.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//        if (convertView == null) {

        convertView = inflater.inflate(R.layout.selfierow, null);

        holder = new ViewHolder();

        Display dispDefault = ((WindowManager) activity
                .getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        int totalwidth = dispDefault.getWidth();

//        Typeface typeFace = Typeface.createFromAsset(activity.getAssets(),
//                "fonts/FuturaStd-Medium.ttf");


        SelfieList selfie = selfieList.get(position);


        holder.dataTv = convertView.findViewById(R.id.dataTv);
        holder.countTv = convertView.findViewById(R.id.countTv);
        holder.imageIv = convertView.findViewById(R.id.imageIv);
        holder.likeIv = convertView.findViewById(R.id.likeIv);
        holder.moreIV = convertView.findViewById(R.id.moreIV);
        holder.progressBar = convertView.findViewById(R.id.progressBar);

        holder.imageIv.setLayoutParams(new LinearLayout.LayoutParams(
                totalwidth / 2, totalwidth / 2));

        convertView.setTag(holder);

        SharedPreferences prefs = activity.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");


        holder.dataTv.setText(StringEscapeUtils.unescapeJava(selfie.getTitle()));
        holder.countTv.setText(selfie.getTotalLikes());
        Glide.with(activity).load(picPath/*ApiConstant.selfieimage*/ + selfie.getFileName())
                .apply(RequestOptions.skipMemoryCacheOf(false))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource
                    dataSource, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.imageIv).onLoadStarted(activity.getDrawable(R.drawable.gallery_placeholder));

        holder.moreIV.setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_ATOP);


        if (selfie.getLikeFlag().equals("1")) {


            holder.likeIv.setImageResource(R.drawable.ic_afterlike);
            holder.likeIv.setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_ATOP);


        } else {
            holder.likeIv.setImageResource(R.drawable.ic_like);
            holder.likeIv.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        }


        return convertView;
    }

    static class ViewHolder {
        TextView countTv, txtLike, dataTv;
        ImageView imageIv, likeIv, moreIV;
        private ProgressBar progressBar;

    }
}
