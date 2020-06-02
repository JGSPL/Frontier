package com.procialize.mrgeApp20.Speaker.Views.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.GetterSetter.EventSettingList;
import com.procialize.mrgeApp20.Speaker.Models.SpeakerList;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Naushad on 10/31/2017.
 */

public class SpeakerAdapter extends RecyclerView.Adapter<SpeakerAdapter.MyViewHolder> implements Filterable {

    public String speaker_rating, speaker_designation, speaker_company, speaker_location, speaker_mobile, speaker_save_contact;
    List<EventSettingList> eventSettingLists;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    private List<SpeakerList> speakerLists;
    private Context context;
    private List<SpeakerList> speakerListFiltered;
    private SpeakerAdapterListner listener;


    public SpeakerAdapter(Context context, List<SpeakerList> speakerLists, SpeakerAdapterListner listener) {
        this.speakerLists = speakerLists;
        this.speakerListFiltered = speakerLists;
        this.listener = listener;
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.speakerlistingrow, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final SpeakerList speaker = speakerListFiltered.get(position);
        SessionManager sessionManager = new SessionManager(context);
        eventSettingLists = SessionManager.loadEventList();
        applySetting(eventSettingLists);

        int colorInt = Color.parseColor(colorActive);

        /*ColorStateList csl = ColorStateList.valueOf(colorInt);
        Drawable drawable = DrawableCompat.wrap(holder.ic_rightarrow.getDrawable());
        DrawableCompat.setTintList(drawable, csl);
        holder.ic_rightarrow.setImageDrawable(drawable);*/


        if (speaker_designation.equalsIgnoreCase("0")) {
            holder.designtionTv.setVisibility(View.GONE);
        } else {
            holder.designtionTv.setVisibility(View.VISIBLE);
        }
        if (speaker_company.equalsIgnoreCase("0")) {
            holder.locationTv.setVisibility(View.GONE);
        } else {
            holder.locationTv.setVisibility(View.VISIBLE);
        }

        //holder.nameTv.setTextColor(Color.parseColor(colorActive));

//        if (speaker.getFirstName().equalsIgnoreCase("N A")) {
//            holder.nameTv.setText("");
//        } else {

        holder.nameTv.setText(speaker.getFirstName() + " " + speaker.getLastName());
//        }

        try {
            if (speaker.getCity().equalsIgnoreCase("N A")) {
                holder.locationTv.setText("");
            } else {
                holder.locationTv.setText(speaker.getCity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


//        if (speaker.getDesignation().equalsIgnoreCase("N A")) {
//            holder.locationTv.setText("");
//        } else {
        holder.locationTv.setText(speaker.getDesignation());
//        }
//        if (speaker.getCompany().equalsIgnoreCase("N A")) {
//            holder.designtionTv.setText("");
//        } else {
      //  holder.designtionTv.setText(speaker.getCompany());
//        }

        if (speaker.getProfilePic() != null) {


            Glide.with(context).load(ApiConstant.profilepic + speaker.getProfilePic())
                    .apply(RequestOptions.skipMemoryCacheOf(false))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).circleCrop()
                    .listener(new RequestListener<Drawable>() {
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
            }).into(holder.profileIv);
        } else {
            holder.progressView.setVisibility(View.GONE);
            holder.profileIv.setImageResource(R.drawable.profilepic_placeholder);


        }
//        ImageSize targetSize = new ImageSize(10, 10);
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    speakerListFiltered = speakerLists;
                } else {
                    List<SpeakerList> filteredList = new ArrayList<>();
                    for (SpeakerList row : speakerLists) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        String name = row.getFirstName().toLowerCase() + " " + row.getLastName().toLowerCase();

                        if (name.contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    speakerListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = speakerListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                speakerListFiltered = (ArrayList<SpeakerList>) filterResults.values;

                if (speakerListFiltered.size() == 0) {
//                    Toast.makeText(context, "No Speaker Found", Toast.LENGTH_SHORT).show();
                }
                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return speakerListFiltered.size();
    }

    public void applySetting(List<EventSettingList> eventSettingLists) {
        for (int i = 0; i < eventSettingLists.size(); i++) {
            /*if (eventSettingLists.get(i).getFieldName().equalsIgnoreCase("speaker_rating")) {
                speaker_rating = eventSettingLists.get(i).getFieldValue();
            }
            if (eventSettingLists.get(i).getFieldName().equalsIgnoreCase("speaker_designation")) {
                speaker_designation = eventSettingLists.get(i).getFieldValue();
            }
            if (eventSettingLists.get(i).getFieldName().equalsIgnoreCase("speaker_company")) {
                speaker_company = eventSettingLists.get(i).getFieldValue();
            }
            if (eventSettingLists.get(i).getFieldName().equalsIgnoreCase("speaker_location")) {
                speaker_location = eventSettingLists.get(i).getFieldValue();
            }
            if (eventSettingLists.get(i).getFieldName().equalsIgnoreCase("speaker_mobile")) {
                speaker_mobile = eventSettingLists.get(i).getFieldValue();
            }*/
           if (eventSettingLists.get(i).getFieldName().equals("event_details")) {

               // event_details = eventSettingLists.get(i).getFieldValue();
                if(eventSettingLists.get(i).getSub_menuList()!=null) {
                    if (eventSettingLists.get(i).getSub_menuList().size() > 0) {
                        for (int k = 0; k < eventSettingLists.get(i).getSub_menuList().size(); k++) {
                            if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("speaker_rating")) {
                                speaker_rating = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("speaker_designation")) {
                                speaker_designation = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("speaker_company")) {
                                speaker_company = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("speaker_location")) {
                                speaker_location = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("speaker_mobile")) {
                                speaker_mobile = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("speaker_save_contact")) {
                                speaker_save_contact = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            }

                        }
                    }
                }
            }
        }
    }

    public interface SpeakerAdapterListner {
        void onContactSelected(SpeakerList attendee);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv, locationTv, designtionTv;
        public ImageView profileIv, ic_rightarrow;
        public ProgressBar progressView;
        private LinearLayout mainLL;

        public MyViewHolder(View view) {
            super(view);
            nameTv = view.findViewById(R.id.nameTv);
            locationTv = view.findViewById(R.id.locationTv);
            designtionTv = view.findViewById(R.id.designtionTv);

            profileIv = view.findViewById(R.id.profileIV);
            ic_rightarrow = view.findViewById(R.id.ic_rightarrow);

            progressView = view.findViewById(R.id.progressView);

            mainLL = view.findViewById(R.id.mainLL);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(speakerListFiltered.get(getAdapterPosition()));
                }
            });

        }
    }
}