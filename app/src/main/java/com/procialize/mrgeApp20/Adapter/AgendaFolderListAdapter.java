package com.procialize.mrgeApp20.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.GetterSetter.Agenda;
import com.procialize.mrgeApp20.R;

import java.util.Date;
import java.util.List;

public class AgendaFolderListAdapter extends BaseAdapter {
    Date d1, d2;
    ApiConstant constant = new ApiConstant();
    private Activity activity;
    private LayoutInflater inflater;
    private List<Agenda> agendaList;

    public AgendaFolderListAdapter(Activity activity, List<Agenda> agendaList) {
        this.activity = activity;
        this.agendaList = agendaList;
    }

    @Override
    public int getCount() {
        return agendaList.size();
    }

    @Override
    public Object getItem(int location) {
        return agendaList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Agenda getAttendeeFromList(int position) {
        return agendaList.get(position);
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.agenda_list_row, null);

            holder = new ViewHolder();

            holder.agenda_time_slot = convertView
                    .findViewById(R.id.agenda_time_slot);
            holder.agenda_title = convertView
                    .findViewById(R.id.agenda_title);

            holder.agenda_desc = convertView
                    .findViewById(R.id.agenda_desc);


            holder.agenda_list_layout = convertView
                    .findViewById(R.id.agenda_list_layout);


            holder.webviewAgenda = convertView
                    .findViewById(R.id.webviewAgenda);

            Typeface typeFace = Typeface.createFromAsset(activity.getAssets(),
                    "fonts/DINPro-Regular.ttf");
            Typeface typeFace1 = Typeface.createFromAsset(activity.getAssets(),
                    "fonts/Roboto-Bold.ttf");

            holder.agenda_time_slot.setTypeface(typeFace);
            holder.agenda_title.setTypeface(typeFace1);
            holder.agenda_desc.setTypeface(typeFace);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//		String startTime = agendaList.get(position).getSession_start_time();
//		String endTime = agendaList.get(position).getSession_end_time();

//		Log.e("time",endTime);
//		// SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
//
//		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd KK:mm:ss");
//
//		try {
//			d1 = f.parse(startTime);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		long millisecondsStart = d1.getTime();
//
//		try {
//			d2 = f.parse(endTime);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		long millisecondsEnd = d2.getTime();
//
//		SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a", Locale.US);
//		SimpleDateFormat formatter1 = new SimpleDateFormat("hh:mm a",Locale.US);
//
//
//		String finalStartTime = formatter.format(new Date(millisecondsStart));
//		String finalEndTime = formatter1.format(new Date(millisecondsEnd));

		/*if((position==0)||(position==2)||(position==4)||(position==6)||(position==8)||(position==10)||(position==12)
				||(position==14)||(position==16)||(position==18)||(position==20)||(position==22)||(position==24)||(position==26)
				||(position==28)||(position==30)||(position==32)||(position==34)){
			holder.agenda_list_layout.setBackgroundResource(R.drawable.edt_background_new1);

		} else if (position == 1||(position==3)||(position==5)||(position==7)||(position==9)||(position==11)||(position==13)
				||(position==15)||(position==17)||(position==19)||(position==20)||(position==21)||(position==23)||(position==25)
				||(position==27)||(position==29)||(position==31)||(position==33)||(position==35)||(position==37)||(position==39)) {
			holder.agenda_list_layout.setBackgroundResource(R.drawable.edt_background_new2);
		}*/

//		holder.agenda_time_slot.setText(finalStartTime+" - "+finalEndTime);
        //holder.agenda_title.setText(agendaList.get(position).getSession_name());
        //holder.agenda_desc.setText(agendaList.get(position).getSession_description());
//		String bodyData = agendaList.get(position).getSession_description();
        holder.webviewAgenda.getSettings().setLoadsImagesAutomatically(true);
        holder.webviewAgenda.clearCache(true);
        holder.webviewAgenda.getSettings().setJavaScriptEnabled(true);
        //holder.webviewAgenda.setBackgroundColor(Color.TRANSPARENT);
        //holder.webviewAgenda.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        holder.webviewAgenda.clearCache(true);

//		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//			holder.agenda_desc.setText(Html.fromHtml(bodyData,Html.FROM_HTML_MODE_LEGACY));
//		} else {
//			holder.agenda_desc.setText(Html.fromHtml(bodyData));
//		}
//		holder.webviewAgenda.loadDataWithBaseURL("", bodyData, "text/html", "UTF-8", "");
        //holder.webviewAgenda.loadData(bodyData, "text/html", "UTF-8");

        return convertView;
    }

    static class ViewHolder {
        TextView agenda_time_slot, agenda_title, attendee_comp_name,
                agenda_desc;
        LinearLayout agenda_list_layout;
        WebView webviewAgenda;

    }

}
