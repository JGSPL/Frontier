//package com.procialize.eventsapp.InnerDrawerActivity;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.database.sqlite.SQLiteDatabase;
//import android.graphics.Typeface;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import androidx.appcompat.app.AppCompatActivity;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.text.method.ScrollingMovementMethod;
//import android.util.Log;
//import android.view.View;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//public class TaggingDetailActivity extends AppCompatActivity implements View.OnClickListener {
//    ImageView attendee_thumbnail;
//    TextView attendee_detail_header;
//    TextView attendee_name;
//    TextView attendee_designation;
//    TextView attendee_comp_name;
//    TextView attendee_city, txt_start_time_dialog, txt_end_time_dialog,
//            save_text, share_text, send_txt, setup_txt, attendee_detail_desc,
//            attendee_arnumber, send_exh2;
//
//    String startTime = "", endTime = "", attendee_id;
//
//    EditText setUpMessage;
//    Dialog myDialog;
//    ImageView sendMessage;
//    ImageView setMeeting;
//    FrameLayout saveAttendee;
//    FrameLayout shareAttendee;
//    String sendMsgUrl = "";
//    String saveAttendeeUrl = "";
//    Date dateFlag;
//    LinearLayout send_attendee_layout, setup_meeting_layout,
//            send_attendee_layout2;
//
//    private ImageView back_img_attendee;
//
//
//    Constants constant = new Constants();
//    Attendees specificAttendee;
//    WallNotifications specificAttendeeFromWall;
//    private ProgressDialog pDialog;
//    private DBHelper procializeDB1;
//    private SQLiteDatabase db1;
//    String fromActivity = "";
//
//    String api_access_token_ = "";
//    String subject_id_ = "";
//    String subject_type_ = "";
//    String event_id_ = "1";
//    String type_ = "";
//    String transaction_type_ = "";
//    String image_url = "";
//    Editable value;
//    String sendMsg = "";
//    String setUpMeetingMsg = "";
//    String whoCame = "";
//    String sendName;
//    ConnectionDetector cd;
//    View save_view;
//    LinearLayout back_attendee_detail;
//    String attendeeName = "";
//    ArrayList<UserProfile> userData;
//    String attendeeId = "";
//    String shareName = "";
//    String attendeeNameSend;
//    String setupMeetingUrl = "";
//    // Access Token Variable
//    private String accessToken, top_management_flag;
//
//    private Typeface typeFace;
//
//    // Session Manager Class
//    SessionManagement session;
//    private String analytic_msg_url;
//    private List<Attendees> attendeesDBList = new ArrayList<Attendees>();
//    ;
//    MixpanelAPI mixpanel;
//
//    private LinearLayout linDateDialog, linTimeDialog;
//    private int year;
//    private int month;
//    private int day;
//
//    private TextView date, time;
//    private TextView etime;
//    static final int DATE_PICKER_ID = 1111;
//    static final int TIME_DIALOG_ID = 1110;
//    static final int TIME_DIALOG_ID2 = 1112;
//    private int hour;
//    private int minute;
//    private String showDate, showTime, totalDateTime, endTotalDT;
//    Calendar date2;
//    UserProfile userData1;
//    private DBHelper dbHelper;
//    private DBHelper procializeDB;
//    private SQLiteDatabase db;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tagging_detail);
//
//        initializeGUI();
//        String projectToken = Constants.PROJECT_TOKEN;
//        Bundle intent = getIntent().getExtras();
//        attendee_id = intent.getString("attendeeId");
//
//        dbHelper = new DBHelper(TaggingDetailActivity.this);
//        procializeDB = new DBHelper(this);
//        db = procializeDB.getWritableDatabase();
//
//        db = dbHelper.getWritableDatabase();
//        procializeDB1 = new DBHelper(this);
//        db1 = procializeDB1.getReadableDatabase();
//        userData1 = procializeDB1.getUserProfile();
//        attendeesDBList = dbHelper.getAttendeeDetailById(attendee_id);
//        attendeeNameSend = procializeDB1.getUserProfile().getFirst_name() + " "
//                + procializeDB1.getUserProfile().getLast_name();
//        session = new SessionManagement(getApplicationContext());
//        accessToken = session.getAccessToken();
//        mixpanel = MixpanelAPI.getInstance(getApplicationContext(),
//                projectToken);
//        for (int i = 0; i < attendeesDBList.size(); i++) {
//            attendee_detail_desc.setText(attendeesDBList.get(i).getAttendee_description());
//            attendee_city.setText(attendeesDBList.get(i).getAttendee_city());
//            attendee_comp_name.setText(attendeesDBList.get(i).getAttendee_company());
//            attendee_designation.setText(attendeesDBList.get(i).getAttendee_designation());
//            attendee_name.setText(attendeesDBList.get(i).getFirst_name() + " " + attendeesDBList.get(i).getLast_name());
//
//            image_url = constant.WEBSERVICE_URL + constant.ATTENDEE_IMAGE_URL
//                    + attendeesDBList.get(i).getAttendee_image();
//
//            sendMsgUrl = constant.WEBSERVICE_URL + constant.WEBSERVICE_FOLDER
//                    + constant.SEND_MESSAGE;
//            PicassoTrustAll.getInstance(this).load(image_url).placeholder(R.drawable.user_pic_w)
//                    .transform(new CirclePicassoTransform())
//                    .into(attendee_thumbnail);
//
//            sendName = attendeesDBList.get(i).getFirst_name() + " "
//                    + attendeesDBList.get(i).getLast_name();
//
//            if (sendName.equalsIgnoreCase(attendeeNameSend)) {
//
//                send_attendee_layout.setVisibility(View.INVISIBLE);
//            } else {
//                send_attendee_layout.setVisibility(View.VISIBLE);
//            }
//
//            if (sendName.equalsIgnoreCase(attendeeNameSend)) {
//
//                send_attendee_layout2.setVisibility(View.INVISIBLE);
//            } else {
//                send_attendee_layout2.setVisibility(View.VISIBLE);
//            }
//
//            subject_id_ = attendeesDBList.get(i).getAttendee_id();
//            subject_type_ = attendeesDBList.get(i).getAttendee_type();
//        }
//
//
//    }
//
//    private void initializeGUI() {
//
//        cd = new ConnectionDetector(getApplicationContext());
//
//        save_view = findViewById(R.id.save_view);
//        back_attendee_detail = (LinearLayout) findViewById(R.id.back_attendee_detail);
//        back_attendee_detail.setOnClickListener(this);
//
//        back_img_attendee = (ImageView) findViewById(R.id.back_img_attendee);
//        back_img_attendee.setOnClickListener(this);
//
//        saveAttendee = (FrameLayout) findViewById(R.id.attendee_detail_save_attendee);
//        saveAttendee.setOnClickListener(this);
//        saveAttendee.setClickable(true);
//        saveAttendee.setEnabled(true);
//        save_view.setAlpha(1f);
//
//        send_attendee_layout = (LinearLayout) findViewById(R.id.send_attendee_layout);
//        send_attendee_layout.setOnClickListener(this);
//        setup_meeting_layout = (LinearLayout) findViewById(R.id.setup_meeting_layout);
//
//        // setup_meeting_layout.setOnClickListener(this);
//
//        fromActivity = getIntent().getExtras().getString("fromActivity");
//        whoCame = getIntent().getExtras().getString("whoCame");
//
//        typeFace = Typeface.createFromAsset(getAssets(),
//                "fonts/FuturaStd-Medium.ttf");
//
//        attendee_thumbnail = (ImageView) findViewById(R.id.attendee_detail_thumbnail);
//        attendee_detail_header = (TextView) findViewById(R.id.attendee_detail_header);
//        attendee_detail_header.setTypeface(typeFace);
//
//        TextView send_message = (TextView) findViewById(R.id.send_message);
//        send_message.setTypeface(typeFace);
//
//        save_text = (TextView) findViewById(R.id.save_text);
//        save_text.setTypeface(typeFace);
//
//        share_text = (TextView) findViewById(R.id.share_text);
//        share_text.setTypeface(typeFace);
//
//        send_txt = (TextView) findViewById(R.id.send_txt);
//        send_txt.setTypeface(typeFace);
//
//        setup_txt = (TextView) findViewById(R.id.setup_txt);
//        setup_txt.setTypeface(typeFace);
//
//        attendee_name = (TextView) findViewById(R.id.attendee_detail_name);
//        attendee_name.setTypeface(typeFace);
//
//        attendee_designation = (TextView) findViewById(R.id.attendee_detail_designation);
//        attendee_designation.setTypeface(typeFace);
//
//        attendee_comp_name = (TextView) findViewById(R.id.attendee_detail_comp_name);
//        attendee_comp_name.setTypeface(typeFace);
//
//        attendee_city = (TextView) findViewById(R.id.attendee_detail_city);
//        attendee_city.setTypeface(typeFace);
//
//        attendee_detail_desc = (TextView) findViewById(R.id.attendee_detail_desc);
//        attendee_detail_desc.setTypeface(typeFace);
//
//        send_attendee_layout2 = (LinearLayout) findViewById(R.id.send_attendee_layout2);
//        send_attendee_layout2.setOnClickListener(this);
//
//        send_exh2 = (TextView) findViewById(R.id.send_exh2);
//        send_exh2.setTypeface(typeFace);
//
//        /*
//         * attendee_arnumber = (TextView)
//         * findViewById(R.id.attendee_arn_number);
//         * attendee_arnumber.setTypeface(typeFace);
//         * attendee_arnumber.setVisibility(View.GONE);
//         */
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (v == back_img_attendee) {
//            finish();
//        } else if (v == back_img_attendee) {
//            finish();
//        } else if (v == send_attendee_layout) {
//            messageDialog("message", sendName, this);
//        }
//    }
//
//    public void messageDialog(String title, String message,
//                              final Context activity) {
//
//        myDialog = new Dialog(activity);
//        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        myDialog.setContentView(R.layout.dialog_layout);
////        myDialog.setTitle("Send Message");
//        myDialog.setCancelable(false);
//        myDialog.show();
//
//
//        TextView sender_dialog_txt = (TextView) myDialog
//                .findViewById(R.id.sender_dialog_txt);
//        sender_dialog_txt.setMovementMethod(ScrollingMovementMethod
//                .getInstance());
//        sender_dialog_txt.setText("To, " + message);
//        sender_dialog_txt.setTypeface(typeFace);
//
//        final EditText sendMessage = (EditText) myDialog
//                .findViewById(R.id.message_edt_send__dialog);
//        sendMessage.setTypeface(typeFace);
//
//        final TextView txtcount1 = (TextView) myDialog
//                .findViewById(R.id.txt_count);
//        final TextWatcher txwatcher = new TextWatcher() {
//
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int start,
//                                          int count, int after) {
//                int tick = start + after;
//                if (tick < 128) {
//                    int remaining = 128 - tick;
//                    // txtcount1.setText(String.valueOf(remaining));
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                // TODO Auto-generated method stub
//                txtcount1.setText(String.valueOf(250 - s.length()));
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//                // TODO Auto-generated method stub
//                System.out.print("Hello");
//            }
//        };
//
//        sendMessage.addTextChangedListener(txwatcher);
//
//        Button send = (Button) myDialog.findViewById(R.id.btn_send_dialog);
//        send.setTypeface(typeFace);
//        send.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                if (cd.isConnectingToInternet()) {
//                    if (sendMessage.length() == 0) {
//                        Toast.makeText(TaggingDetailActivity.this,
//                                "Please enter message", Toast.LENGTH_SHORT)
//                                .show();
//                    } else {
//                        sendMsg = sendMessage.getText().toString();
//                        new SendMessageAttendee().execute();
//                    }
//                } else {
//                    Toast.makeText(getBaseContext(), "No Internet Connection",
//                            Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//        Button close = (Button) myDialog.findViewById(R.id.btn_close_dialog);
//        close.setTypeface(typeFace);
//        close.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                myDialog.hide();
//
//            }
//        });
//
//        myDialog.show();
//
//    }
//
//    private class SendMessageAttendee extends AsyncTask<Void, Void, Void> {
//
//        String error = "";
//        String message = "";
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            // Showing progress dialog
//            pDialog = new ProgressDialog(TaggingDetailActivity.this,
//                    R.style.Base_Theme_AppCompat_Dialog_Alert);
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();
//
//        }
//
//        @Override
//        protected Void doInBackground(Void... arg0) {
//            // Creating service handler class instance
//            ServiceHandler sh = new ServiceHandler();
//            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
//
//            nameValuePair.add(new BasicNameValuePair("api_access_token",
//                    accessToken));
//            nameValuePair.add(new BasicNameValuePair("event_id", event_id_));
//            nameValuePair.add(new BasicNameValuePair("message_text", sendMsg));
//            nameValuePair.add(new BasicNameValuePair("target_attendee_id",
//                    subject_id_));
//            nameValuePair.add(new BasicNameValuePair("target_user_type",
//                    subject_type_));
//
//            // Making a request to url and getting response
//            String jsonStr = sh.makeServiceCall(sendMsgUrl,
//                    ServiceHandler.POST, nameValuePair);
//            Log.d("Response: ", "> " + jsonStr);
//
//            if (jsonStr != null) {
//                try {
//
//                    JSONObject jsonResult = new JSONObject(jsonStr);
//                    error = jsonResult.getString("status");
//                    message = jsonResult.getString("msg");
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//            // Dismiss the progress dialog
//
//            if (pDialog.isShowing())
//                pDialog.dismiss();
//            if (error.equalsIgnoreCase("success")) {
//                try {
//                    JSONObject props = new JSONObject();
//                    props.put("Name", MainActivity.sname);
//                    props.put("Designation", MainActivity.sdesig);
//                    props.put("Email", MainActivity.semail);
//                    props.put("City", MainActivity.scity);
//                    props.put("MessageContent", sendMsg);
//
//                    mixpanel.track("Send Message Successfull", props);
//
//                } catch (JSONException e) {
//                    Log.e("MYAPP", "Unable to add properties to JSONObject", e);
//                }
//
//                myDialog.dismiss();
//            }
//            AlertDialog.Builder builder = new AlertDialog.Builder(
//                    TaggingDetailActivity.this);
//            builder.setMessage(message).setTitle(error);
//            builder.setPositiveButton("OK",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            // User clicked OK button
//                        }
//                    });
//            AlertDialog alert = builder.create();
//            alert.show();
//
//            Log.d("Created URL : ", ">>>>> " + saveAttendeeUrl);
//        }
//    }
//}
