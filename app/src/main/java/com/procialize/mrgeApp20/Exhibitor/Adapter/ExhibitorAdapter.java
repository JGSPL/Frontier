package com.procialize.mrgeApp20.Exhibitor.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ExhibitorAdapter extends RecyclerView.Adapter<ExhibitorAdapter.MyViewHolder> implements Filterable {

    List<com.procialize.mrgeApp20.GetterSetter.ExhibitorDataList> ExhibitorDataList;
    private List<com.procialize.mrgeApp20.GetterSetter.ExhibitorCatList> ExhibitorCatList;
    private List<com.procialize.mrgeApp20.GetterSetter.ExhibitorAttendeeList> ExhibitorAttendeeList;
    private Context context;
    private ExhibitorAdapterListner listener;
    private List<com.procialize.mrgeApp20.GetterSetter.ExhibitorDataList> eventListfilter;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive, CatName;

    public ExhibitorAdapter(Context context, List<com.procialize.mrgeApp20.GetterSetter.ExhibitorDataList> ExhibitorDataList, ExhibitorAdapterListner listener, String CatName) {
        //this.ExhibitorCatList = ExhibitorCatList;
        this.ExhibitorDataList = ExhibitorDataList;
        this.eventListfilter = ExhibitorDataList;
        this.listener = listener;
        this.context = context;
        this.CatName = CatName;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

    }

    @NonNull
    @Override
    public ExhibitorAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.exhibitor_detail_listingdetail_item, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ExhibitorAdapter.MyViewHolder holder, int position) {

        final com.procialize.mrgeApp20.GetterSetter.ExhibitorDataList evnt = eventListfilter.get(position);

        int colorInt = Color.parseColor(colorActive);

        ColorStateList csl = ColorStateList.valueOf(colorInt);
        Drawable drawable = DrawableCompat.wrap(holder.arrow.getDrawable());
        DrawableCompat.setTintList(drawable, csl);
        holder.arrow.setImageDrawable(drawable);
        holder.txt_exibitorname.setText(evnt.getName());
        holder.txt_exibitorname.setTextColor(Color.parseColor(colorActive));

        holder.linExhilogo.setBackgroundColor(Color.parseColor(colorActive));

        if (evnt.getLogo() != null) {


            Glide.with(context).load(ApiConstant.exhiilogo + evnt.getLogo())
                    .apply(RequestOptions.skipMemoryCacheOf(false))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    // holder.progressBar.setVisibility(View.GONE);
                      holder.exebitor_logo.setImageResource(R.drawable.ex_logo);
                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    //  holder.progressBar.setVisibility(View.GONE);
                    return false;
                }
            }).into(holder.exebitor_logo);

        } else {
            // holder.progressBar.setVisibility(View.GONE);
            //holder.exebitor_logo.setImageResource(R.drawable.profilepic_placeholder);

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
                    eventListfilter = ExhibitorDataList;
                } else {
                    List<com.procialize.mrgeApp20.GetterSetter.ExhibitorDataList> filteredList = new ArrayList<>();
                    for (com.procialize.mrgeApp20.GetterSetter.ExhibitorDataList row : ExhibitorDataList) {

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
                eventListfilter = (ArrayList<com.procialize.mrgeApp20.GetterSetter.ExhibitorDataList>) filterResults.values;

                if (eventListfilter.size() == 0) {
                    Toast.makeText(context, "No Event Found", Toast.LENGTH_SHORT).show();
                }
                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    public interface ExhibitorAdapterListner {
        void onContactSelected(com.procialize.mrgeApp20.GetterSetter.ExhibitorDataList eventList);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_exibitorname, txt_exibitornumber;
        ImageView exebitor_logo, arrow;
        LinearLayout linExhilogo, linExhimain;

        public ProgressBar progressBar;

        public MyViewHolder(View view) {
            super(view);
            txt_exibitorname = view.findViewById(R.id.txt_exhebitor_name);
            exebitor_logo = view.findViewById(R.id.exebitor_logo);
            arrow = view.findViewById(R.id.arrow);
            linExhilogo = view.findViewById(R.id.linExhilogo);
            linExhimain = view.findViewById(R.id.linExhimain);


            //txt_exibitornumber = view.findViewById(R.id.txt_exibitornumber);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(eventListfilter.get(getAdapterPosition()));

//                    String imgname = "background";//url.substring(58, 60);


                }
            });

        }
    }

}

