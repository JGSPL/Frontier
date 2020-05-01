package com.procialize.mrgeApp20.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.procialize.mrgeApp20.GetterSetter.Brochure_Anlytics_Result;
import com.procialize.mrgeApp20.R;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class DocumentAnalyticAdapter extends RecyclerView.Adapter<DocumentAnalyticAdapter.MyViewHolder> {

    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    private List<Brochure_Anlytics_Result> ExhibitorDashboards;
    private Context context;
    private DocumentAnalyticAdapter.DocumentAnalyticAdapterListner listener;

    public DocumentAnalyticAdapter(Context context, List<Brochure_Anlytics_Result> ExhibitorDashboard, DocumentAnalyticAdapter.DocumentAnalyticAdapterListner listener) {
        this.ExhibitorDashboards = ExhibitorDashboard;
        this.listener = listener;
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

    }

    @Override
    public DocumentAnalyticAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.documentanalyticitem, parent, false);

        return new DocumentAnalyticAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DocumentAnalyticAdapter.MyViewHolder holder, int position) {
        final Brochure_Anlytics_Result travel = ExhibitorDashboards.get(position);

        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(80);
        shape.setColor(Color.parseColor(colorActive));

        holder.linear.setBackground(shape);

        holder.txt_cnt.setText(travel.getTotal_brochure_visits());
        holder.txt_cnt.setTextColor(Color.parseColor(colorActive));
        holder.txt_name.setText(StringEscapeUtils.unescapeJava(travel.getBrochure_title()));

        if (travel.getFile_type().equalsIgnoreCase("pdf")) {
            holder.img_analytic.setImageResource(R.drawable.pdf_icon);
        } else if (travel.getFile_type().equalsIgnoreCase("image")) {
            holder.img_analytic.setImageResource(R.drawable.image_icon);
        } else if (travel.getFile_type().equalsIgnoreCase("video")) {
            holder.img_analytic.setImageResource(R.drawable.video_ic);
        }


    }

    @Override
    public int getItemCount() {
        return ExhibitorDashboards.size();
    }

    public interface DocumentAnalyticAdapterListner {
        void onContactSelected(Brochure_Anlytics_Result travel);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_cnt, txt_name;
        ImageView img_analytic;
        LinearLayout linear;


        public MyViewHolder(View view) {
            super(view);
            txt_cnt = view.findViewById(R.id.txt_cnt);
            txt_name = view.findViewById(R.id.txt_name);
            img_analytic = view.findViewById(R.id.img_analytic);
            linear = view.findViewById(R.id.linear);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(ExhibitorDashboards.get(getAdapterPosition()));
                }
            });
        }
    }
}