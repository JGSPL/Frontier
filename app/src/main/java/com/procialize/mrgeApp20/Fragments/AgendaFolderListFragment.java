package com.procialize.mrgeApp20.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.procialize.mrgeApp20.Adapter.AgendaFolderListAdapter;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.GetterSetter.Agenda;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class AgendaFolderListFragment extends Fragment {

    // Session Manager Class
    SessionManager session;
    String topMgmtFlag;
    private ListView agendaList;
    private List<Agenda> agendaDBList;
    private List<Agenda> agendaOneList;
    private DBHelper dbHelper;
    private AgendaFolderListAdapter adapter;
    private String sessionDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //mixpanel.track("Agenda Detail Page");


        // mixpanel.track("Agenda List Page");

        View rootView = inflater.inflate(R.layout.agenda_list_fragment,
                container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Session Manager
        session = new SessionManager(getActivity().getApplicationContext());

        topMgmtFlag = session.getSkipFlag();

        Bundle b = getArguments();
        if (b != null)
            sessionDate = b.getString("sessionDate");

        // int current = tabHost.getTabHost() ;

        // Toast.makeText(getActivity(), "Inside Agenda Fragment One",
        // Toast.LENGTH_LONG).show();
        System.out.println("Inside Agenda Fragment One");
        // Runtime rt = Runtime.getRuntime();
        // long maxMemory = rt.maxMemory();
        // Log.v("onCreate", "maxMemory:" + Long.toString(maxMemory));

        dbHelper = new DBHelper(getActivity());
        agendaDBList = new ArrayList<Agenda>();
        agendaOneList = new ArrayList<Agenda>();

        agendaList = getActivity().findViewById(R.id.agenda_list);
        agendaList.setScrollingCacheEnabled(false);
        agendaList.setAnimationCacheEnabled(false);
//		agendaDBList = dbHelper.getAgendaList();

        // TODO
//		for (int i = 0; i < agendaDBList.size(); i++) {
//			if (agendaDBList.get(i).getFolder_name()
//					.equalsIgnoreCase(sessionDate)) {
//				agendaOneList.add(agendaDBList.get(i));
//			}
//		}

//		ArrayList<String> ratedList = new ArrayList<String>();
//		for (int j = 0; j < agendaOneList.size(); j++) {
//
//			ratedList.add(agendaOneList.get(j).getRated());
//
//		}

        // Strore Rated List to Shared Preference
        SharedPreferences prefs = getActivity().getApplicationContext()
                .getSharedPreferences("RatedList" + sessionDate,
                        Context.MODE_PRIVATE);
        Editor edit = prefs.edit();
        edit.clear();

        int size = prefs.getInt("size", 0);

//		if (size == 0) {
//			for (int i = 0; i < ratedList.size(); i++) {
//				edit.putString("val" + i, ratedList.get(i));
//			}
//			edit.putInt("size", ratedList.size());
//			edit.commit();
//
//		}

        // Set<String> set = new HashSet<String>();
        // set.addAll(ratedList);
        // edit.putStringSet("RatedArraylistAgenda", set);
        // edit.commit();

        // Set<String> set1 = prefs.getStringSet("RatedArraylistAgenda", null);
        // ratedList = new ArrayList<String>(set);

        adapter = new AgendaFolderListAdapter(getActivity(), agendaOneList);
        agendaList.setAdapter(adapter);

		/*agendaList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

					Agenda specificAgenda = adapter.getAttendeeFromList(position);
					Intent agendaDetail = new Intent(getActivity(),
							AgendaDetailActivity.class);
					agendaDetail.putExtra("ratePosition", position + "");
					agendaDetail.putExtra("sessionDate", sessionDate);
					agendaDetail.putExtra("SpecificAgenda", specificAgenda);
					getActivity().startActivity(agendaDetail);

					*//*
         * Apply our splash exit (fade out) and main entry (fade in)
         * animation transitions.
         *//*
					getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);


			}
		});*/

    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }
}
