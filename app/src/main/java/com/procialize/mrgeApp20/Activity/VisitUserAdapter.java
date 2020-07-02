package com.procialize.mrgeApp20.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.GetterSetter.ExhibitorView_User_List;
import com.procialize.mrgeApp20.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.procialize.mrgeApp20.Session.ImagePathConstants.KEY_ATTENDEE_PIC_PATH;

public class VisitUserAdapter extends RecyclerView.Adapter<VisitUserAdapter.MyViewHolder> implements Filterable {

    private List<ExhibitorView_User_List> ExhibitorView_User_ListList;
    private List<ExhibitorView_User_List> attendeeListFiltered;
    private Context context;
    //    private VisitUserAdapterListner listener;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv, dateTv, txt_city;
        public ImageView imageIv;

        public MyViewHolder(View view) {
            super(view);
            nameTv = view.findViewById(R.id.nameTv);
            imageIv = view.findViewById(R.id.profileIV);
            dateTv = view.findViewById(R.id.designationTv);
            txt_city = view.findViewById(R.id.txt_city);
        }
    }


    public VisitUserAdapter(Context context, List<ExhibitorView_User_List> ExhibitorView_User_ListList) {
        this.ExhibitorView_User_ListList = ExhibitorView_User_ListList;
        this.attendeeListFiltered = ExhibitorView_User_ListList;
//        this.listener=listener;
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

    }

    @Override
    public VisitUserAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.analytic_item, parent, false);

        return new VisitUserAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VisitUserAdapter.MyViewHolder holder, int position) {
        ExhibitorView_User_List ExhibitorView_User_List = attendeeListFiltered.get(position);

        if (attendeeListFiltered.get(position).getProfile_pic() != null) {
            SharedPreferences prefs1 = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String picPath =  prefs1.getString(KEY_ATTENDEE_PIC_PATH,"");
            Glide.with(context).load(/*ApiConstant.profilepic*/picPath + attendeeListFiltered.get(position).getProfile_pic()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    holder.imageIv.setImageResource(R.drawable.profilepic_placeholder);
                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(holder.imageIv);
        } else {
            holder.imageIv.setImageResource(R.drawable.profilepic_placeholder);
        }

        holder.nameTv.setTextColor(Color.parseColor(colorActive));
        holder.txt_city.setTextColor(Color.parseColor(colorActive));
        holder.txt_city.setText(ExhibitorView_User_List.getTotal_exhibitor_visits());
        holder.dateTv.setText(ExhibitorView_User_List.getCity());
        holder.nameTv.setText(ExhibitorView_User_List.getFirst_name() + " " + ExhibitorView_User_List.getLast_name());

//        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
//        SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMM HH:mm");
//        try {
//            Date startdate = originalFormat.parse(ExhibitorView_User_List.ge());
//            String startdatestr = targetFormat.format(startdate);
//            holder.dateTv.setText(startdatestr);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public int getItemCount() {
        return attendeeListFiltered.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    attendeeListFiltered = ExhibitorView_User_ListList;
                } else {
                    if (ExhibitorView_User_ListList.size() == 0) {

                    } else {
                        List<ExhibitorView_User_List> filteredList = new ArrayList<>();
                        for (ExhibitorView_User_List row : ExhibitorView_User_ListList) {

                            String name = row.getFirst_name().toLowerCase() + " " + row.getLast_name().toLowerCase();

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
                attendeeListFiltered = (ArrayList<ExhibitorView_User_List>) filterResults.values;


                    if (attendeeListFiltered.size() == 0) {
//                    Toast.makeText(context, "No Attendee Found", Toast.LENGTH_SHORT).show();
                    }


                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }
}
