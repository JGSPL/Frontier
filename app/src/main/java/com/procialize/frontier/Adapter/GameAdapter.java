package com.procialize.frontier.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.procialize.frontier.Activity.WebGameActivity;
import com.procialize.frontier.GetterSetter.Gameitem;
import com.procialize.frontier.R;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.MyViewHolder> {

    Context context;
    List<Gameitem> gameList;
    private GameAdapter.GameAdapterListner listener;

    public GameAdapter(Context context, List<Gameitem> gameList) {
        this.gameList = gameList;
//        this.listener = listener;
        this.context = context;
    }

    @Override
    public GameAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_game, parent, false);

        return new GameAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final GameAdapter.MyViewHolder holder, int position) {
        final Gameitem attendee = gameList.get(position);

        holder.nameTv.setText(gameList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }


    public interface GameAdapterListner {
        void onContactSelected(Gameitem attendee);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv;


        public MyViewHolder(View view) {
            super(view);
            nameTv = view.findViewById(R.id.nameTV);

            nameTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    Intent intent = new Intent(context, WebGameActivity.class);
                    intent.putExtra("url", gameList.get(getAdapterPosition()).getUrl());
                    context.startActivity(intent);
                }
            });
        }
    }
}