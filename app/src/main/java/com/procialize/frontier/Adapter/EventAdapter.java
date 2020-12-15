package com.procialize.frontier.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.procialize.frontier.ApiConstant.ApiConstant;
import com.procialize.frontier.GetterSetter.EventSettingList;
import com.procialize.frontier.GetterSetter.UserEventList;
import com.procialize.frontier.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.procialize.frontier.Session.ImagePathConstants.KEY_EVENT_LIST_LOGO_PATH;


/**
 * //Created by Naushad on 10/31/2017.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> implements Filterable {

    List<EventSettingList> eventSettingLists;
    private List<UserEventList> eventList;
    private List<UserEventList> eventListfilter;
    private Context context;
    private EventAdapterListner listener;
    String MY_PREFS_NAME = "ProcializeInfo";
    String logoPath;
    public EventAdapter(Context context, List<UserEventList> eventList, EventAdapterListner listener) {
        this.eventListfilter = eventList;
        this.eventList = eventList;
        this.listener = listener;
        this.context = context;

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        logoPath = prefs.getString(KEY_EVENT_LIST_LOGO_PATH,"");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.eventrow, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final UserEventList evnt = eventListfilter.get(position);


        /*try {
            holder.eventnaem.setBackgroundColor(Color.parseColor(evnt.getPrimary_color_code()));
        }catch (Exception e){
            e.printStackTrace();
        }*/

        /*int colorInt = Color.parseColor(evnt.getPrimary_color_code());

        //ColorStateList csl = ColorStateList.valueOf(colorInt);
        Drawable drawable = DrawableCompat.wrap(holder.imgArrow.getDrawable());
        DrawableCompat.setTintList(drawable, csl);
        holder.imgArrow.setImageDrawable(drawable);*/
       // holder.imgArrow.setColorFilter(Color.parseColor(evnt.getPrimary_color_code()), PorterDuff.Mode.SRC_ATOP);


        holder.nameTv.setText(evnt.getName());
        holder.locationTv.setText(evnt.getLocation());
//        holder.designationTv.setText(evnt.getEventStart() + "-" + evnt.getEvent_end());
        holder.eventnaem.setText(evnt.getName());
        SimpleDateFormat formatter = new SimpleDateFormat(ApiConstant.dateformat );
        try {
            Date date1 = formatter.parse(evnt.getEventStart());
            Date date2 = formatter.parse(evnt.getEvent_end());

            DateFormat originalFormat = new SimpleDateFormat("dd MMM yyyy", Locale.UK);

            String date = originalFormat.format(date1);
            String date3 = originalFormat.format(date2);
            if (date.equalsIgnoreCase(date3)) {
                holder.designationTv.setText(date);

            } else {

                holder.designationTv.setText(date + " - " + date3);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (evnt.getLogo() != null) {

            Glide.with(context).load(logoPath/*ApiConstant.eventpic*/ + evnt.getLogo())
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).circleCrop()
                    .listener(new RequestListener<Drawable>() {

                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                    holder.progressBar.setVisibility(View.GONE);
                    holder.profileIv.setImageResource(R.drawable.profilepic_placeholder);
                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    holder.progressBar.setVisibility(View.GONE);
                    return false;
                }
            }).into(holder.profileIv);

        } else {
            holder.progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return eventListfilter.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    eventListfilter = eventList;
                } else {
                    List<UserEventList> filteredList = new ArrayList<>();
                    for (UserEventList row : eventList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        String name = row.getName().toLowerCase();

                        if (name.contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    eventListfilter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = eventListfilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                eventListfilter = (ArrayList<UserEventList>) filterResults.values;

                if (eventListfilter.size() == 0) {
                    Toast.makeText(context, "No Event Found", Toast.LENGTH_SHORT).show();
                }
                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }


    public interface EventAdapterListner {
        void onContactSelected(UserEventList eventList);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv, locationTv, designationTv, eventnaem;
        public ImageView profileIv, imgArrow;
        public LinearLayout mainLL;
        public ProgressBar progressBar;

        public MyViewHolder(View view) {
            super(view);
            nameTv = view.findViewById(R.id.nameTv);
            locationTv = view.findViewById(R.id.locationTv);
            designationTv = view.findViewById(R.id.designationTv);
            eventnaem = view.findViewById(R.id.eventnaem);
            imgArrow = view.findViewById(R.id.imgArrow);

            profileIv = view.findViewById(R.id.profileIV);

            mainLL = view.findViewById(R.id.mainLL);

            progressBar = view.findViewById(R.id.progressBar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(eventListfilter.get(getAdapterPosition()));

                    String imgname = "background";//url.substring(58, 60);


                }
            });
        }
    }
}