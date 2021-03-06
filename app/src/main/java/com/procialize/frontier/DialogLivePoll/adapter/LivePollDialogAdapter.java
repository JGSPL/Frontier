package com.procialize.frontier.DialogLivePoll.adapter;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.procialize.frontier.GetterSetter.LivePollList;
import com.procialize.frontier.GetterSetter.LivePollOptionList;
import com.procialize.frontier.R;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class LivePollDialogAdapter extends BaseAdapter {
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    private List<LivePollList> pollLists;
    private List<LivePollOptionList> optionLists;
    private Context context;
    private LayoutInflater inflater;

    public LivePollDialogAdapter(Context context, List<LivePollList> pollLists, List<LivePollOptionList> optionLists) {
        this.pollLists = pollLists;
        this.optionLists = optionLists;
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
            holder.mainLL = convertView.findViewById(R.id.mainLL);
            holder.linMain = convertView.findViewById(R.id.linMain);


            holder.relative = (RelativeLayout) convertView
                    .findViewById(R.id.relative);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //holder.mainLL.setBackgroundColor(Color.parseColor(colorActive));

        holder.nameTv.setText(StringEscapeUtils.unescapeJava(pollList.getQuestion()));
/*        holder.linMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onContactSelected(pollLists.get(position));
            }
        });*/

        if(position==0){
            holder.relative.setVisibility(View.VISIBLE);
        }else{
            holder.relative.setVisibility(View.GONE);

        }
        return convertView;
    }

    public interface PollAdapterListner {
        void onContactSelected(LivePollList pollList);
    }

    static class ViewHolder {
        public TextView nameTv;
        public ImageView imageIv;
        public LinearLayout mainLL,linMain;
        RelativeLayout relative;
    }
}