package com.procialize.mrgeApp20.Fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.procialize.mrgeApp20.Activity.ExhibitorDetailActivity;
import com.procialize.mrgeApp20.R;

public class EditDetailsFragment extends Fragment {
    public static EditText edit_address_detail, edit_stall;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_edit_document,
                container, false);
        edit_address_detail = rootView.findViewById(R.id.edit_address_detail);
        edit_stall = rootView.findViewById(R.id.edit_stall);

        edit_stall.setText(ExhibitorDetailActivity.StallNum);
        edit_address_detail.setText(ExhibitorDetailActivity.address);

        return rootView;

    }
}
