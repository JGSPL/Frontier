package com.procialize.mrgeApp20.Exhibitor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.procialize.mrgeApp20.Activity.ExhibitorListingActivity;
import com.procialize.mrgeApp20.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ExhibitorListingAdapter extends RecyclerView.Adapter<ExhibitorListingAdapter.MyViewHolder> implements Filterable {

    List<com.procialize.mrgeApp20.GetterSetter.ExhibitorDataList> ExhibitorDataList;
    private List<com.procialize.mrgeApp20.GetterSetter.ExhibitorCatList> ExhibitorCatList;
    private List<com.procialize.mrgeApp20.GetterSetter.ExhibitorAttendeeList> ExhibitorAttendeeList;
    private Context context;
    private ExhibitorAdapterListner listener;
    private List<com.procialize.mrgeApp20.GetterSetter.ExhibitorCatList> eventListfilter;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;

    public ExhibitorListingAdapter(Context context, List<com.procialize.mrgeApp20.GetterSetter.ExhibitorCatList> ExhibitorCatList, List<com.procialize.mrgeApp20.GetterSetter.ExhibitorDataList> ExhibitorDataList, ExhibitorAdapterListner listener) {
        this.ExhibitorCatList = ExhibitorCatList;
        this.ExhibitorDataList = ExhibitorDataList;
        this.eventListfilter = ExhibitorCatList;
        this.listener = listener;
        this.context = context;

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

    }

    @NonNull
    @Override
    public ExhibitorListingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.exhibitor_item, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExhibitorListingAdapter.MyViewHolder holder, int position) {

       /* if (position == 0) {
            holder.txt_exibitorname.setText("All Exhibitor");
            holder.txt_exibitornumber.setText(ExhibitorDataList.size() + " Exhibitors");

        } else{*/
        final com.procialize.mrgeApp20.GetterSetter.ExhibitorCatList evnt = ExhibitorCatList.get(position);

        holder.txt_exibitorname.setText(evnt.getName());
        holder.txt_exibitornumber.setText(evnt.getTotal_exhibitor_count() + " Exhibitors");
        holder.linCat.setBackgroundColor(Color.parseColor(colorActive));

        // }
        holder.linitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ExhibitorCatList.size() > 0) {
                    // send selected contact in callback
                    Intent quizOptionIntent = new Intent(context, ExhibitorListingActivity.class);
                    quizOptionIntent.putExtra("ExhiName", evnt.getName());
                    quizOptionIntent.putExtra("ExhiId", evnt.getExhibitor_category_id());

                    context.startActivity(quizOptionIntent);
                } else {
                    Toast.makeText(context, "Exhibitor not available", Toast.LENGTH_SHORT).show();

                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return ExhibitorCatList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    eventListfilter = ExhibitorCatList;
                } else {
                    List<com.procialize.mrgeApp20.GetterSetter.ExhibitorCatList> filteredList = new ArrayList<>();
                    for (com.procialize.mrgeApp20.GetterSetter.ExhibitorCatList row : ExhibitorCatList) {

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
            protected void publishResults(CharSequence charSequence, FilterResults
                    filterResults) {
                eventListfilter = (ArrayList<com.procialize.mrgeApp20.GetterSetter.ExhibitorCatList>) filterResults.values;

                if (eventListfilter.size() == 0) {
                    Toast.makeText(context, "No Event Found", Toast.LENGTH_SHORT).show();
                }
                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    public interface ExhibitorAdapterListner {
        void onContactSelected(com.procialize.mrgeApp20.GetterSetter.ExhibitorCatList eventList);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_exibitorname, txt_exibitornumber;
        LinearLayout linitem, linCat;
        public ProgressBar progressBar;

        public MyViewHolder(View view) {
            super(view);
            txt_exibitorname = view.findViewById(R.id.txt_exibitorname);
            txt_exibitornumber = view.findViewById(R.id.txt_exibitornumber);
            linitem = view.findViewById(R.id.linitem);
            linCat = view.findViewById(R.id.linCat);



/*
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    Intent quizOptionIntent = new Intent(getContext(), ExhibitorListingActivity.class);
                    quizOptionIntent.putExtra("ExhiName", eventList.getName());
                    quizOptionIntent.putExtra("ExhiId", eventList.getExhibitor_category_id());

                    startActivity(quizOptionIntent);


                }
            });
*/


        }
    }

}
