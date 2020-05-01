package com.procialize.mrgeApp20.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.procialize.mrgeApp20.GetterSetter.EventMenuSettingList;
import com.procialize.mrgeApp20.GetterSetter.EventSettingList;
import com.procialize.mrgeApp20.R;

import java.util.List;


/**
 * Created by Naushad on 10/31/2017.
 */

public class CustomMenuAdapter extends RecyclerView.Adapter<CustomMenuAdapter.MyViewHolder> {

    List<EventSettingList> eventSettingLists;
    String agenda_conference;
    String side_menu_agenda;
    private List<EventMenuSettingList> eventMenuSettingLists;
    private Context context;
    private CustomMenuAdapterListner listener;


    public CustomMenuAdapter(Context context, List<EventMenuSettingList> eventMenuSettingLists, CustomMenuAdapterListner listener, String side_menu_agenda) {
        this.eventMenuSettingLists = eventMenuSettingLists;
        this.listener = listener;
        this.context = context;
        this.side_menu_agenda = side_menu_agenda;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menulistingrow, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        EventMenuSettingList menuSettingList = eventMenuSettingLists.get(position);


        if (menuSettingList.getFieldValue().equalsIgnoreCase("0")) {
            holder.topll.setVisibility(View.GONE);

            holder.nameTv.setVisibility(View.GONE);
            holder.profileIv.setVisibility(View.GONE);
            holder.mainLL.setVisibility(View.GONE);
            holder.relative.setVisibility(View.GONE);


        } else {
            if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu")) {
                holder.nameTv.setVisibility(View.GONE);
                holder.profileIv.setVisibility(View.GONE);
                holder.mainLL.setVisibility(View.GONE);
                holder.topll.setVisibility(View.GONE);
                holder.relative.setVisibility(View.GONE);

            } else if (side_menu_agenda.equalsIgnoreCase("0") &&
                    menuSettingList.getFieldName().equalsIgnoreCase("side_menu_agenda")) {
                holder.nameTv.setVisibility(View.GONE);
                holder.profileIv.setVisibility(View.GONE);
                holder.mainLL.setVisibility(View.GONE);
                holder.topll.setVisibility(View.GONE);
                holder.relative.setVisibility(View.GONE);

            } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_event_info") ||
                    menuSettingList.getFieldName().equalsIgnoreCase("side_menu_notification")) {
                holder.nameTv.setVisibility(View.GONE);
                holder.profileIv.setVisibility(View.GONE);
                holder.mainLL.setVisibility(View.GONE);
                holder.topll.setVisibility(View.GONE);
                holder.relative.setVisibility(View.GONE);

            } else {
                holder.nameTv.setVisibility(View.VISIBLE);
                holder.profileIv.setVisibility(View.VISIBLE);
                holder.mainLL.setVisibility(View.VISIBLE);
                holder.topll.setVisibility(View.VISIBLE);
                holder.relative.setVisibility(View.VISIBLE);

            }
        }

        if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_my_travel")) {
            holder.nameTv.setText("My Travel");
            holder.profileIv.setImageResource(R.drawable.ic_travel);
        } /*else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_notification")) {
            holder.nameTv.setText("Notifications");
            holder.profileIv.setImageResource(R.drawable.notification_icon);
        }*/ else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_display_qr")) {
            holder.nameTv.setText("Qr Badge");
            holder.profileIv.setImageResource(R.drawable.qr_badge);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_qr_scanner")) {
            holder.nameTv.setText("Scan ID Card");
            holder.profileIv.setImageResource(R.drawable.ic_scan);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_quiz")) {
            holder.nameTv.setText("Quiz");
            holder.profileIv.setImageResource(R.drawable.quiz);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_live_poll")) {
            holder.nameTv.setText("Live Poll");
            holder.profileIv.setImageResource(R.drawable.live_poll);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_survey")) {
            holder.nameTv.setText("Survey");
            holder.profileIv.setImageResource(R.drawable.survey);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_feedback")) {
            holder.nameTv.setText("Feedback");
            holder.profileIv.setImageResource(R.drawable.ic_feedback);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_gallery_video")) {
            holder.nameTv.setText("Video Gallery");
            holder.profileIv.setImageResource(R.drawable.gallery_videos);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_image_gallery")) {
            holder.nameTv.setText("Image Gallery");
            holder.profileIv.setImageResource(R.drawable.gallery_icon);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_document")) {
            holder.nameTv.setText("Documents");
            holder.profileIv.setImageResource(R.drawable.ic_documents);
        } /*else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_event_info")) {
            holder.nameTv.setText("Event Info");
            holder.profileIv.setImageResource(R.drawable.ic_info);
        } */ else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_q&a")) {
            holder.nameTv.setText("Q & A");
            holder.profileIv.setImageResource(R.drawable.ic_question);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_engagement")) {
            holder.nameTv.setText("Engagement");
            holder.profileIv.setImageResource(R.drawable.engagement);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_attendee")) {
            holder.nameTv.setText("Attendee");
            holder.profileIv.setImageResource(R.drawable.attendees);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_speaker")) {
            holder.nameTv.setText("Speaker");
            holder.profileIv.setImageResource(R.drawable.speaker);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_gen_info")) {
            holder.nameTv.setText("General Info");
            holder.profileIv.setImageResource(R.drawable.general);
        } /*else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_agenda")) {
            if(agenda_vacation.equalsIgnoreCase("1") ||agenda_conference.equalsIgnoreCase("1")) {
                holder.nameTv.setText("Agenda");
                holder.profileIv.setImageResource(R.drawable.agenda);
            }else{
                holder.topll.setVisibility(View.GONE);
            }
        }*/ else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_agenda")) {
            holder.nameTv.setText("Agenda");
            holder.profileIv.setImageResource(R.drawable.agenda);
        } /*else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_contact_us")) {
            holder.nameTv.setText("Contact Us");
            holder.profileIv.setImageResource(R.drawable.contact_us);
        } */ else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_email_template")) {
            holder.nameTv.setText("Email Template");
            holder.profileIv.setImageResource(R.drawable.ic_info);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_leaderboard")) {
            holder.nameTv.setText("LeaderBoard");
            holder.profileIv.setImageResource(R.drawable.leaderboard);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_exhibitor")) {
            holder.nameTv.setText("Exhibitors");
            holder.profileIv.setImageResource(R.drawable.ic_exhibitor);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_sponsor")) {
            holder.nameTv.setText("Sponsors & Partners");
            holder.profileIv.setImageResource(R.drawable.sponsor);
        } else {
            holder.nameTv.setVisibility(View.GONE);
            holder.profileIv.setVisibility(View.GONE);
            holder.mainLL.setVisibility(View.GONE);
            holder.topll.setVisibility(View.GONE);
            holder.relative.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return eventMenuSettingLists.size();
    }

    public interface CustomMenuAdapterListner {
        void onContactSelected(EventMenuSettingList eventMenuSettingList);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv, locationTv, designationTv;
        public ImageView profileIv;
        public LinearLayout mainLL, topll;
        RelativeLayout relative;

        public MyViewHolder(View view) {
            super(view);
            nameTv = view.findViewById(R.id.nameTv);
            locationTv = view.findViewById(R.id.locationTv);
            relative = view.findViewById(R.id.relative);
            designationTv = view.findViewById(R.id.designationTv);
            topll = view.findViewById(R.id.topll);
            profileIv = view.findViewById(R.id.profileIV);

            mainLL = view.findViewById(R.id.mainLL);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(eventMenuSettingLists.get(getAdapterPosition()));
                }
            });
        }
    }
}