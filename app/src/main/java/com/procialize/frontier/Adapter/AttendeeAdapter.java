package com.procialize.frontier.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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
import com.procialize.frontier.DbHelper.DBHelper;
import com.procialize.frontier.GetterSetter.AttendeeList;
import com.procialize.frontier.GetterSetter.EventSettingList;
import com.procialize.frontier.R;
import com.procialize.frontier.Session.SessionManager;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Naushad on 10/31/2017.
 */

public class AttendeeAdapter extends RecyclerView.Adapter<AttendeeAdapter.MyViewHolder> implements Filterable {

    List<EventSettingList> eventSettingLists;
    String attendee_design, attendee_company, attendee_location, attendee_mobile, attendee_save_contact;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    private List<AttendeeList> attendeeLists;
    private Context context;
    private List<AttendeeList> attendeeListFiltered;
    private AttendeeAdapterListner listener;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private String picPath;


    public AttendeeAdapter(Context context, List<AttendeeList> attendeeLists,String picPath, AttendeeAdapterListner listener) {
        this.attendeeLists = attendeeLists;
        this.attendeeListFiltered = attendeeLists;
        this.picPath = picPath;
        this.listener = listener;
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");
        procializeDB = new DBHelper(context);
        db = procializeDB.getWritableDatabase();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attendeelistingrow, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final AttendeeList attendee = attendeeListFiltered.get(position);

        SessionManager sessionManager = new SessionManager(context);
        eventSettingLists = SessionManager.loadEventList();
        applySetting(eventSettingLists);


        /*int colorInt = Color.parseColor(colorActive);

        ColorStateList csl = ColorStateList.valueOf(colorInt);
        Drawable drawable = DrawableCompat.wrap(holder.ic_rightarrow.getDrawable());
        DrawableCompat.setTintList(drawable, csl);
        holder.ic_rightarrow.setImageDrawable(drawable);*/


//        try {
//            if (attendee_location.equalsIgnoreCase("0")) {
//                holder.locationTv.setVisibility(View.INVISIBLE);
//            } else {
//                if (attendee.getCompanyName().equalsIgnoreCase("N A")) {
//                    holder.locationTv.setVisibility(View.INVISIBLE);
//                }
//                if (attendee.getCompanyName().equalsIgnoreCase("")) {
//                    holder.locationTv.setVisibility(View.INVISIBLE);
//                }
//                if (attendee.getCompanyName().equalsIgnoreCase(" ")) {
//                    holder.locationTv.setVisibility(View.INVISIBLE);
//                } else {
//                    holder.locationTv.setVisibility(View.VISIBLE);
//                    holder.locationTv.setText(attendee.getCity());
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            holder.locationTv.setVisibility(View.INVISIBLE);
//        }
//
//
//        try {
//            if (attendee_design.equalsIgnoreCase("0")) {
//                holder.designationTv.setVisibility(View.INVISIBLE);
//            } else {
//                if (attendee.getDesignation().equalsIgnoreCase("N A")) {
//                    holder.designationTv.setVisibility(View.INVISIBLE);
//                }
//                if (attendee.getDesignation().equalsIgnoreCase("")) {
//                    holder.designationTv.setVisibility(View.INVISIBLE);
//                }
//                if (attendee.getDesignation().equalsIgnoreCase(" ")) {
//                    holder.designationTv.setVisibility(View.INVISIBLE);
//                } else {
//                    holder.designationTv.setVisibility(View.VISIBLE);
//                    holder.designationTv.setText(attendee.getDesignation());
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            holder.designationTv.setVisibility(View.INVISIBLE);
//        }


//        if(attendee_location.equalsIgnoreCase("0")){
//            holder.locationTv.setVisibility(View.GONE);
//        }else {
//            holder.locationTv.setVisibility(View.VISIBLE);
//        }


        if (attendee.getFirstName().equalsIgnoreCase("N A")) {
            holder.nameTv.setText("");
        } else {
            holder.nameTv.setText(attendee.getFirstName() + " " + attendee.getLastName());
           // holder.nameTv.setTextColor(Color.parseColor(colorActive));
        }
        try {


            if (attendee.getCompanyName().equalsIgnoreCase("N A")) {
                holder.locationTv.setText("");
            } else {
                holder.locationTv.setText(attendee.getCompanyName());
            }


        }catch (Exception e){
            e.printStackTrace();
        }


        if (attendee.getProfilePic() != null) {


            Glide.with(context).load(picPath/*ApiConstant.profilepic*/ + attendee.getProfilePic())
                    .apply(RequestOptions.skipMemoryCacheOf(false))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).circleCrop()
                    .listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    holder.profileIv.setImageResource(R.drawable.profilepic_placeholder);

                    holder.progressBar.setVisibility(View.GONE);
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



        /*if (attendee.getProfilePic() != null) {


            Glide.with(context).load(ApiConstant.profilepic + attendee.getProfilePic())
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<Drawable>() {
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
        }*/

        String attendee_id = attendee.getAttendeeId();
        String unreadMsgCount = procializeDB.getAttendeeChatForAttendeeId(attendee_id);
        if(!unreadMsgCount.equalsIgnoreCase("0"))
        {
            holder.tv_count.setText(unreadMsgCount);
            holder.tv_count.setVisibility(View.VISIBLE);
            holder.ic_rightarrow.setVisibility(View.GONE);
        }
        else
        {
            holder.tv_count.setText("");
            holder.tv_count.setVisibility(View.GONE);
            holder.ic_rightarrow.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return attendeeListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    attendeeListFiltered = attendeeLists;
                } else {
                    if (attendeeLists.size() == 0) {

                    } else {
                        List<AttendeeList> filteredList = new ArrayList<>();
                        for (AttendeeList row : attendeeLists) {

                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match
                            String name="";
                            if(row.getLastName()!=null) {
                                 name = row.getFirstName().toLowerCase() + " " + row.getLastName().toLowerCase();
                            }else
                            {
                                 name = row.getFirstName().toLowerCase();
                            }
                            if (name.contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }
                        attendeeListFiltered = filteredList;
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = attendeeListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                attendeeListFiltered = (ArrayList<AttendeeList>) filterResults.values;

                if (attendeeListFiltered.size() == 0) {
//                    Toast.makeText(context, "No Attendee Found", Toast.LENGTH_SHORT).show();
                }
                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    public void applySetting(List<EventSettingList> eventSettingLists) {
        for (int i = 0; i < eventSettingLists.size(); i++) {
            /*if (eventSettingLists.get(i).getFieldName().equalsIgnoreCase("attendee_designation")) {
                attendee_design = eventSettingLists.get(i).getFieldValue();
            }
            if (eventSettingLists.get(i).getFieldName().equalsIgnoreCase("attendee_location")) {
                attendee_location = eventSettingLists.get(i).getFieldValue();
            }
            if (eventSettingLists.get(i).getFieldName().equalsIgnoreCase("attendee_company")) {
                attendee_company = eventSettingLists.get(i).getFieldValue();
            }
            if (eventSettingLists.get(i).getFieldName().equalsIgnoreCase("attendee_mobile")) {
                attendee_mobile = eventSettingLists.get(i).getFieldValue();
            }
            if (eventSettingLists.get(i).getFieldName().equalsIgnoreCase("attendee_save_contact")) {
                attendee_save_contact = eventSettingLists.get(i).getFieldValue();
            }*/
            if (eventSettingLists.get(i).getFieldName().equals("event_details")) {
                if(eventSettingLists.get(i).getSub_menuList()!=null) {
                    if (eventSettingLists.get(i).getSub_menuList().size() > 0) {
                        for (int k = 0; k < eventSettingLists.get(i).getSub_menuList().size(); k++) {
                             if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("attendee_company")) {
                                attendee_company = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("attendee_location")) {
                                attendee_location = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("attendee_mobile")) {
                                attendee_mobile = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("attendee_save_contact")) {
                                attendee_save_contact = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("attendee_designation")) {
                                 attendee_design = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                             }

                        }
                    }
                }
            }
        }
    }

    public interface AttendeeAdapterListner {
        void onContactSelected(AttendeeList attendee);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv, locationTv, designationTv,tv_count;
        public ImageView profileIv, ic_rightarrow;
        public LinearLayout mainLL;
        public ProgressBar progressBar;

        public MyViewHolder(View view) {
            super(view);
            nameTv = view.findViewById(R.id.nameTv);
            tv_count = view.findViewById(R.id.tv_count);
            locationTv = view.findViewById(R.id.locationTv);
            designationTv = view.findViewById(R.id.designationTv);

            profileIv = view.findViewById(R.id.profileIV);

            ic_rightarrow = view.findViewById(R.id.ic_rightarrow);

            mainLL = view.findViewById(R.id.mainLL);

            progressBar = view.findViewById(R.id.progressBar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(attendeeListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
}