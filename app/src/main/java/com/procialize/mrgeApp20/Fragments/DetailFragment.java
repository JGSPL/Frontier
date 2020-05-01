package com.procialize.mrgeApp20.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.flexbox.FlexboxLayout;
import com.procialize.mrgeApp20.Activity.ExhibitorDetailActivity;
import com.procialize.mrgeApp20.Activity.ExhibitorListingActivity;
import com.procialize.mrgeApp20.Adapter.DetailCatListAdapter;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.GetterSetter.ExhiCatDetailList;
import com.procialize.mrgeApp20.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class DetailFragment extends Fragment implements DetailCatListAdapter.MyTravelAdapterListner {
    private TextView tvStallname, tvAddress;
    String eventid, colorActive;
    String MY_PREFS_NAME = "ProcializeInfo";
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private DBHelper dbHelper;
    private List<ExhiCatDetailList> catDBList;
    RecyclerView catList;
    DetailCatListAdapter adapter;
    View viewone, viewm;
    String catcnt;
    String MY_PREFS_CATEGORY = "categorycnt";
    FlexboxLayout container1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        ExhibitorDetailActivity exhiDetail = new ExhibitorDetailActivity();
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");
        tvStallname = view.findViewById(R.id.tvStallname);
        tvAddress = view.findViewById(R.id.tvAddress);
        catList = view.findViewById(R.id.catList);
        viewone = view.findViewById(R.id.viewone);
        viewm = view.findViewById(R.id.view);
        container1 = view.findViewById(R.id.v_container);
        tvAddress.setMovementMethod(new ScrollingMovementMethod());

        SharedPreferences prefs8 = getActivity().getSharedPreferences(MY_PREFS_CATEGORY, MODE_PRIVATE);
        catcnt = prefs8.getString("categorycnt", "");

        if (exhiDetail.StallNum.equalsIgnoreCase("")) {
            tvStallname.setVisibility(View.GONE);
//            viewone.setVisibility(View.GONE);
        } else {
            tvStallname.setVisibility(View.VISIBLE);
//            viewone.setVisibility(View.VISIBLE);
            tvStallname.setText(exhiDetail.StallNum);
        }


        tvAddress.setText(exhiDetail.address);

//        tvStallname.setTextColor(Color.parseColor(colorActive));

        procializeDB = new DBHelper(getActivity());
        db = procializeDB.getWritableDatabase();
        dbHelper = new DBHelper(getActivity());

        catDBList = dbHelper.getEXCatList(exhiDetail.Exhi_id);

        int mNoOfColumns = calculateNoOfColumns(getActivity().getApplicationContext(), TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        catList.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
//        int mNoOfColumns = calculateNoOfColumns(getActivity().getApplicationContext(), 150);
//        catList.setLayoutManager(new GridLayoutManager(getActivity(), mNoOfColumns));


//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//        catList.setLayoutManager(mLayoutManager);

        if (catDBList.isEmpty()) {
            catList.setVisibility(View.GONE);
            viewm.setVisibility(View.GONE);
        } else {
            catList.setVisibility(View.GONE);
            viewm.setVisibility(View.VISIBLE);
            adapter = new DetailCatListAdapter(getActivity(), catDBList, this);
            adapter.notifyDataSetChanged();
            catList.setAdapter(adapter);
        }

        inflatelayout();
        return view;
    }


    public static int calculateNoOfColumns(Context context, float columnWidthDp) { // For example columnWidthdp=180
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
        return noOfColumns;
    }

    @Override
    public void onContactSelected(ExhiCatDetailList travel) {
        if (Integer.parseInt(catcnt) <= 3) {
            Toast.makeText(getActivity(), "No Exhibitor List is there.", Toast.LENGTH_SHORT).show();
        } else {
            Intent quizOptionIntent = new Intent(getActivity(), ExhibitorListingActivity.class);
            quizOptionIntent.putExtra("ExhiName", travel.getName());
            quizOptionIntent.putExtra("ExhiId", travel.getExhibitor_category_id());

            getActivity().startActivity(quizOptionIntent);
            getActivity().finish();
        }
    }

    private void inflatelayout() {

        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonLayoutParams.setMargins(10, 5, 10, 10);
//        buttonLayoutParams.setLayoutDirection(buttonLayoutParams.gravity);

        for (int i = 0; i < catDBList.size(); i++) {
            final TextView tv = new TextView(getActivity());
            tv.setText(catDBList.get(i).getName());
            tv.setHeight(60);
            tv.setTextSize(16.0f);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(Color.parseColor("#000000"));
            ShapeDrawable sd = new ShapeDrawable();
            // Specify the shape of ShapeDrawable
            sd.setShape(new RectShape());

            // Specify the border color of shape
            sd.getPaint().setColor(Color.parseColor(colorActive));

            // Set the border width
            sd.getPaint().setStrokeWidth(10f);

            // Specify the style is a Stroke
            sd.getPaint().setStyle(Paint.Style.STROKE);

            // Finally, add the drawable background to TextView
            tv.setBackground(sd);
            tv.setTextColor(Color.parseColor(colorActive));
            tv.setId(i);
            tv.setLayoutParams(buttonLayoutParams);
            tv.setTag(i);
            tv.setPadding(20, 0, 20, 0);
            container1.addView(tv);

            final int finalI = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Integer.parseInt(catcnt) <= 3) {
                        Toast.makeText(getActivity(), "No Exhibitor List is there.", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent quizOptionIntent = new Intent(getActivity(), ExhibitorListingActivity.class);
                        quizOptionIntent.putExtra("ExhiName", catDBList.get(finalI).getName());
                        quizOptionIntent.putExtra("ExhiId", catDBList.get(finalI).getExhibitor_category_id());
                        getActivity().startActivity(quizOptionIntent);

                    }
                }
            });

        }
    }
}
