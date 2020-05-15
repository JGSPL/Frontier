package com.procialize.mrgeApp20.MrgeInnerFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.procialize.mrgeApp20.MergeMain.MrgeHomeActivity;
import com.procialize.mrgeApp20.R;

public class EmergencyFragment extends Fragment {

    View rootView;


    public EmergencyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_second, container, false);

        MrgeHomeActivity.headerlogoIv.setVisibility(View.GONE);
        MrgeHomeActivity.txtMainHeader.setVisibility(View.VISIBLE);

        //Util.logomethodwithText(getContext(), MrgeHomeActivity.txtMainHeader, "Event Info");


        return rootView;
    }
}