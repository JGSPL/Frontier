package com.procialize.frontier.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.procialize.frontier.GetterSetter.LivePollList;
import com.procialize.frontier.GetterSetter.LivePollOptionList;
import com.procialize.frontier.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Naushad on 10/31/2017.
 */

public class PollAdapter extends RecyclerView.Adapter<PollAdapter.MyViewHolder> {

    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    private List<LivePollList> pollLists;
    private List<LivePollOptionList> optionLists;
    private Context context;
    private PollAdapterListner listener;


    public PollAdapter(Context context, List<LivePollList> pollLists, List<LivePollOptionList> optionLists, PollAdapterListner listener) {
        this.pollLists = pollLists;
        this.optionLists = optionLists;
        this.listener = listener;
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.poll_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final LivePollList pollList = pollLists.get(position);

        holder.mainLL.setBackgroundColor(Color.parseColor(colorActive));

        holder.nameTv.setText(pollList.getQuestion());

    }

    @Override
    public int getItemCount() {
        return pollLists.size();
    }

    public interface PollAdapterListner {
        void onContactSelected(LivePollList pollList);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv;
        public ImageView imageIv;
        public LinearLayout mainLL;

        public MyViewHolder(View view) {
            super(view);
            nameTv = view.findViewById(R.id.nameTv);
            imageIv = view.findViewById(R.id.imageIv);
            mainLL = view.findViewById(R.id.mainLL);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(pollLists.get(getAdapterPosition()));
                }
            });
        }
    }
}