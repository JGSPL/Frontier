package com.procialize.frontier.Speaker.Views.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.procialize.frontier.R;
import com.procialize.frontier.Speaker.Models.PdfList;

import java.util.List;


/**
 * Created by Naushad on 10/31/2017.
 */

public class PdfListAdapter extends RecyclerView.Adapter<PdfListAdapter.MyViewHolder> {


    List<PdfList> pdfLists;
    private Context context;
    private PdfListAdapterListner listener;

    public PdfListAdapter(Context context, List<PdfList> pdfLists, PdfListAdapterListner listener) {
        this.pdfLists = pdfLists;
        this.listener = listener;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.speaker_pdf_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.tv_pdf_name.setPaintFlags(holder.tv_pdf_name.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        String pdfName = pdfLists.get(position).getPdf_name();
        String s1 = pdfName.substring(0, 1).toUpperCase() + pdfName.substring(1);
        holder.tv_pdf_name.setText(s1 );
        holder.tv_pdf_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onContactSelected(pdfLists.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return pdfLists.size();
    }

    public interface PdfListAdapterListner {
        void onContactSelected(PdfList pdfList);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_pdf_name;

        public MyViewHolder(View view) {
            super(view);
            tv_pdf_name = view.findViewById(R.id.tv_pdf_name);
        }
    }
}