package com.procialize.mrgeApp20.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.GetterSetter.DeleteExhibitorBrochure;
import com.procialize.mrgeApp20.GetterSetter.EventSettingList;
import com.procialize.mrgeApp20.GetterSetter.ExhibitorBrochureList;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class ExhibitorEditDocumentAdapter extends RecyclerView.Adapter<ExhibitorEditDocumentAdapter.MyViewHolder> {

    List<EventSettingList> eventSettingLists;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive, eventid, accessToken;
    private List<ExhibitorBrochureList> attendeeLists;
    private Context context;
    private List<ExhibitorBrochureList> attendeeListFiltered;
    private ExhibitorEditDocumentAdapter.MyTravelAdapterListner listener;
    public static String BrocherId, BrocherName;
    SessionManager sessionManager;
    APIService mAPIService;
    AlertDialog alertDialog;
    private DBHelper dbHelper;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    String br_id;

    public ExhibitorEditDocumentAdapter(Context context, List<ExhibitorBrochureList> attendeeLists, ExhibitorEditDocumentAdapter.MyTravelAdapterListner listener) {
        this.attendeeLists = attendeeLists;
        this.attendeeListFiltered = attendeeLists;
        this.context = context;
        this.listener = listener;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");
        sessionManager = new SessionManager(context.getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        accessToken = user.get(SessionManager.KEY_TOKEN);
        mAPIService = ApiUtils.getAPIService();

        dbHelper = new DBHelper(context);
        procializeDB = new DBHelper(context);
        db = procializeDB.getWritableDatabase();
    }

    @Override
    public ExhibitorEditDocumentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_edit_document2, parent, false);

        return new ExhibitorEditDocumentAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ExhibitorEditDocumentAdapter.MyViewHolder holder, final int position) {
        final ExhibitorBrochureList travel = attendeeListFiltered.get(position);

        holder.linTicket.setBackgroundColor(Color.parseColor(colorActive));
//        int colorInt = Color.parseColor(colorActive);
//
//        ColorStateList csl = ColorStateList.valueOf(colorInt);
//        Drawable drawable = DrawableCompat.wrap(holder.ic_rightarrow.getDrawable());
//        DrawableCompat.setTintList(drawable, csl);
//        holder.ic_rightarrow.setImageDrawable(drawable);

        holder.nameTv.setText(StringEscapeUtils.unescapeJava(travel.getBrochure_title()));
        holder.txt_docname.setText(StringEscapeUtils.unescapeJava(travel.getBrochure_title()));

        if (travel.getFile_type().equalsIgnoreCase("video")) {
            holder.imgTvel.setBackgroundResource(R.drawable.video_ic);
        } else if (travel.getFile_type().equalsIgnoreCase("image")) {
            holder.imgTvel.setBackgroundResource(R.drawable.image_icon);
        } else {
            holder.imgTvel.setBackgroundResource(R.drawable.pdf_icon);
        }


        holder.nameTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (position == 0) {
                    BrocherId = travel.getBrochure_id();
                    BrocherName = s.toString();
                } else {
                    BrocherId = BrocherId + "$#" + travel.getBrochure_id();
                    BrocherName = BrocherName + "$#" + s.toString();
                }
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                br_id = travel.getBrochure_id();
                open(br_id, position);

            }
        });
    }

    public void open(final String brocherId, final int pos) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("Are you sure, You want to Delete this brochure");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        DeleteExhibitorBrochure(accessToken, eventid, brocherId);
                        attendeeListFiltered.remove(pos);
                        notifyItemRemoved(pos);
                        notifyItemRangeChanged(pos, attendeeListFiltered.size());
                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void DeleteExhibitorBrochure(String accesstoken, String eventid, final String br_id) {

        mAPIService.DeleteExhibitorBrochure(accesstoken, br_id, eventid).enqueue(new Callback<DeleteExhibitorBrochure>() {
            @Override
            public void onResponse(Call<DeleteExhibitorBrochure> call, Response<DeleteExhibitorBrochure> response) {

                alertDialog.dismiss();
                if (response.isSuccessful()) {

                    dbHelper.DeleteBrochure(br_id);
                    notifyDataSetChanged();

                } else {


                }
            }

            @Override
            public void onFailure(Call<DeleteExhibitorBrochure> call, Throwable t) {
                alertDialog.cancel();
                Log.e("hit", "Low network or no network");
                Log.e("hit", t.getMessage());
                Toast.makeText(context, "Please try again", Toast.LENGTH_SHORT).show();
//
            }
        });
    }

    @Override
    public int getItemCount() {
        return attendeeListFiltered.size();
    }

    public static String getBrocherId() {
        return BrocherId;
    }

    public static String getBrocherName() {
        return BrocherName;
    }

    public interface MyTravelAdapterListner {
        void onContactSelected(ExhibitorBrochureList travel);
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_docname;
        public EditText nameTv;
        ImageView imgTvel, remove;
        LinearLayout linTicket;


        public MyViewHolder(View view) {
            super(view);
            nameTv = view.findViewById(R.id.nameTv);
            txt_docname = view.findViewById(R.id.txt_docname);

            linTicket = view.findViewById(R.id.linTicket);
            remove = view.findViewById(R.id.img_remove);

            imgTvel = view.findViewById(R.id.imgTvel);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // send selected contact in callback
//                    listener.onContactSelected(attendeeListFiltered.get(getAdapterPosition()));
//                }
//            });
        }
    }
}
