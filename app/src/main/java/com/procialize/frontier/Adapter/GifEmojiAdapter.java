package com.procialize.frontier.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.frontier.GetterSetter.Result;
import com.procialize.frontier.R;

import java.util.List;


/**
 * Created by Naushad on 10/31/2017.
 */

public class GifEmojiAdapter extends RecyclerView.Adapter<GifEmojiAdapter.MyViewHolder> {

    public List<Result> resultsList;
    private Context context;
    private GifEmojiAdapterListner listener;

    public GifEmojiAdapter(Context context, List<Result> commentLists, GifEmojiAdapterListner listener) {
        this.resultsList = commentLists;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.giflistingrow, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Result result = resultsList.get(position);


        String url = result.getMedia().get(0).getGif().getUrl();
        Log.e("url gif", url);

//        Glide.with(context)
//                .load("http://artfcity.com/wp- content/uploads/2017/07/tumblr_osmx1ogeOD1r2geqjo1_540.gif")
//                                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
//                                .into(image);
//


        Glide.with(context)
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressView.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressView.setVisibility(View.GONE);
                        return false;
                    }
                }).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return resultsList.size();
    }

    public interface GifEmojiAdapterListner {
        void onGifSelected(Result result);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private ProgressBar progressView;

        public MyViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.image);

            progressView = view.findViewById(R.id.progressView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onGifSelected(resultsList.get(getAdapterPosition()));
                }
            });

        }
    }
}