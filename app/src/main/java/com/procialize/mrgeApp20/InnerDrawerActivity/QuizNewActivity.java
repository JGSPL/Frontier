package com.procialize.mrgeApp20.InnerDrawerActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.procialize.mrgeApp20.Adapter.QuizAdapter;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.GetterSetter.Quiz;
import com.procialize.mrgeApp20.GetterSetter.QuizOptionList;
import com.procialize.mrgeApp20.Parser.QuizOptionParser;
import com.procialize.mrgeApp20.Parser.QuizParser;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.MyApplication;
import com.procialize.mrgeApp20.Utility.ServiceHandler;
import com.procialize.mrgeApp20.Utility.Util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class QuizNewActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog pDialog;
    // Session Manager Class
    private SessionManager session;

    // Access Token Variable
    private String accessToken, event_id, colorActive;

    private String quiz_question_id;
    private String quiz_options_id;

    private String quizQuestionUrl = "";
    private String getQuizUrl = "";

    private ConnectionDetector cd;


    private ApiConstant constant = new ApiConstant();

    private RecyclerView quizNameList;
    private QuizAdapter adapter;

    private QuizParser quizParser;
    private ArrayList<Quiz> quizList = new ArrayList<Quiz>();

    private QuizOptionParser quizOptionParser;
    private ArrayList<QuizOptionList> quizOptionList = new ArrayList<QuizOptionList>();

    public static MyApplication appDelegate;
    String foldername = "null";
    Button submit;
    ImageView headerlogoIv;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    int count = 1;
    private DBHelper dbHelper;
    LinearLayoutManager llm;
    LinearLayoutManager recyclerLayoutManager;
    String MY_PREFS_NAME = "ProcializeInfo";
    ViewPager pager;
    TextView questionTv, txt_count;
    Button btnNext;
    RelativeLayout relative;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_new);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizNewActivity.this, FolderQuizActivity.class);
                startActivity(intent);
                finish();

            }
        });

        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        quizQuestionUrl = constant.baseUrl + constant.quizsubmit;

        dbHelper = new DBHelper(QuizNewActivity.this);

        procializeDB = new DBHelper(QuizNewActivity.this);
        db = procializeDB.getWritableDatabase();

        db = procializeDB.getReadableDatabase();

        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);
        appDelegate = (MyApplication) getApplicationContext();

        foldername = getIntent().getExtras().getString("folder");

        // Session Manager
        session = new SessionManager(getApplicationContext());
        accessToken = session.getUserDetails().get(SessionManager.KEY_TOKEN);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        event_id = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");
        cd = new ConnectionDetector(getApplicationContext());

        // Initialize Get Quiz URL
        getQuizUrl = constant.baseUrl + constant.quizlist;


        submit = (Button) findViewById(R.id.submit);
        pager = ( ViewPager ) findViewById(R.id.pager);
        questionTv = (TextView) findViewById(R.id.questionTv);
        btnNext = (Button) findViewById(R.id.btnNext);
        submit.setOnClickListener(this);
        txt_count = (TextView) findViewById(R.id.txt_count);
//        txt_count.setVisibility(View.GONE);
        quizNameList = (RecyclerView) findViewById(R.id.quiz_list);
        btnNext = (Button) findViewById(R.id.btnNext);
        relative = (RelativeLayout) findViewById(R.id.relative);
        recyclerLayoutManager = new LinearLayoutManager(this);
        quizNameList.setLayoutManager(recyclerLayoutManager);
        questionTv.setBackgroundColor(Color.parseColor(colorActive));
        submit.setBackgroundColor(Color.parseColor(colorActive));
        btnNext.setBackgroundColor(Color.parseColor(colorActive));
        txt_count.setTextColor(Color.parseColor(colorActive));
//		quizNameList.setItemViewCacheSize(0);
        quizNameList.setAnimationCacheEnabled(true);
        quizNameList.setDrawingCacheEnabled(true);
        quizNameList.hasFixedSize();
        questionTv.setText(foldername);
        quizNameList.setLayoutFrozen(true);
//        quizNameList.setNestedScrollingEnabled(false);
        quizNameList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        llm = (LinearLayoutManager) quizNameList.getLayoutManager();

        try {
//            ContextWrapper cw = new ContextWrapper(HomeActivity.this);
            //path to /data/data/yourapp/app_data/dirName
//            File directory = cw.getDir("/storage/emulated/0/Procialize/", Context.MODE_PRIVATE);
            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Procialize/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            relative.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            relative.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int option = adapter.getSelectedOption();
                String correctOption = quizList.get(llm.findLastVisibleItemPosition()).getCorrect_answer();
                int i = adapter.getItemCount();
//                adapter.getItemViewType(llm.findLastVisibleItemPosition());
                if (i != count) {
//                    if (option != llm.findLastVisibleItemPosition()) {
                    quizNameList.getLayoutManager().scrollToPosition(llm.findLastVisibleItemPosition() + 1);
                    txt_count.setText(count + 1 + "/" + i);
                    count = count + 1;
                    if (quizList.size() == llm.findLastVisibleItemPosition() + 2) {

                        btnNext.setVisibility(View.GONE);
                        submit.setVisibility(View.VISIBLE);
                    } else {
                        btnNext.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.GONE);
                    }
//                    } else {
//                        Toast.makeText(QuizNewActivity.this, "Please Select Option", Toast.LENGTH_SHORT).show();
//                    }
                }


            }
        });
//
//
//		quizNameList.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//			@Override
//			public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//
//			}
//		});

        if (cd.isConnectingToInternet()) {
            new getQuizList().execute();
        } else {

            Toast.makeText(QuizNewActivity.this, "No internet connection",
                    Toast.LENGTH_SHORT).show();

            // videoDBList = dbHelper.getVideoList();
            //
            // adapter = new VideosListAdapter(this, videoDBList);
            // videoList.setAdapter(adapter);
        }
//
        // LinearLayout mLinearLayout = (LinearLayout)
        // findViewById(R.id.linear1);
        // for (int k = 1; k <= 20; k++) {
        // // create text button
        // TextView title = new TextView(this);
        // title.setText("Question Number:" + k);
        // title.setTextColor(Color.BLUE);
        // mLinearLayout.addView(title);
        // // create radio button
        // final RadioButton[] rb = new RadioButton[5];
        // RadioGroup rg = new RadioGroup(this);
        // rg.setOrientation(RadioGroup.VERTICAL);
        // for (int i = 0; i < 5; i++) {
        // rb[i] = new RadioButton(this);
        // rg.addView(rb[i]);
        // rb[i].setText(countryName[i]);
        //
        // }
        // mLinearLayout.addView(rg);
        // }

//		quizNameList.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//
//				Quiz quiz = adapter.getQuestionIdFromList(position);
//
//				// QuizOptionList quizTempOptionList;
//				//
//				// for (int i = 0; i < quizOptionList.size(); i++) {
//				//
//				// if (quizOptionList.get(i).getQuiz_id()
//				// .equalsIgnoreCase(quiz.getId())) {
//				//
//				// quizTempOptionList.set
//				//
//				//
//				// }
//				//
//				// }
//
//				// QuizOptionList quizOptionList = adapter
//				// .getQuestionIdFromList(position);
//				//
//				// Bundle bundleObject = new Bundle();
//				// bundleObject.putSerializable("quizOptionList",
//				// quizOptionList);
//
//				if (quiz.getReplied().equalsIgnoreCase("0")) {
//
//					Intent quizOptionIntent = new Intent(QuizActivity.this,
//							QuizDetailActivity.class);
//					quizOptionIntent.putExtra("questionId", quiz.getId());
//					quizOptionIntent.putExtra("question", quiz.getQuestion());
//
//					startActivity(quizOptionIntent);
//
//					/*
//					 * Apply our splash exit (fade out) and main entry (fade in)
//					 * animation transitions.
//					 */
//					overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
//				} else {
//
//					Toast.makeText(QuizActivity.this,
//							"You already submitted the quiz.",
//							Toast.LENGTH_SHORT).show();
//				}
//
//				// .putExtra("quizOptionList", quizOptionList);
//
//				// quizOptionIntent.putExtras(bundleObject);
//
//			}
//		});

    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class getQuizList extends AsyncTask<Void, Void, Void> {

        String status = "";
        String message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Dismiss the progress dialog
            if (pDialog != null) {
                pDialog.dismiss();
                pDialog = null;
            }

            // Showing progress dialog
            pDialog = new ProgressDialog(QuizNewActivity.this,
                    R.style.Base_Theme_AppCompat_Dialog_Alert);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

            nameValuePair.add(new BasicNameValuePair("api_access_token",
                    accessToken));

            nameValuePair.add(new BasicNameValuePair("event_id",
                    event_id));

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(getQuizUrl,
                    ServiceHandler.POST, nameValuePair);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject jsonResult = new JSONObject(jsonStr);
                    status = jsonResult.getString("status");
                    message = jsonResult.getString("msg");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            if (status.equalsIgnoreCase("success")) {

                // Get Quiz Parser
                quizParser = new QuizParser();
                quizList = quizParser.Quiz_Parser(jsonStr, foldername);

                // Get Quiz Option List
                quizOptionParser = new QuizOptionParser();
                quizOptionList = quizOptionParser.Quiz_Option_Parser(jsonStr);

                procializeDB.clearQuizTable();
                procializeDB.insertQuizTable(quizList, db);


                appDelegate.setQuizOptionList(quizOptionList);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // Dismiss the progress dialog
            if (pDialog != null) {
                pDialog.dismiss();
                pDialog = null;
            }
//
            adapter = new QuizAdapter(QuizNewActivity.this, quizList);
            quizNameList.setAdapter(adapter);
            int itemcount = adapter.getItemCount();
            txt_count.setText(1 + "/" + itemcount);
            if (quizList.size() > 1) {
                btnNext.setVisibility(View.VISIBLE);
                submit.setVisibility(View.GONE);
            } else {
                btnNext.setVisibility(View.GONE);
                submit.setVisibility(View.VISIBLE);
            }
//			Parcelable state = quizNameList.onSaveInstanceState();
//			quizNameList.onRestoreInstanceState(state);
//			quizNameList.setEmptyView(findViewById(android.R.id.empty));

//            Intent intent = new Intent(QuizNewActivity.this, AnswersActivity.class);
//            intent.putExtra("folderName", foldername);
//            startActivity(intent);


        }
    }

    @Override
    public void onClick(View v) {
        if (v == submit) {
            Boolean valid = true;
            final int[] check = {0};
            int sum = 0;
            final String[] question_id = {""};
            final String[] question_ans = {""};
            final String[] value = {""};
            final RadioGroup[] radioGroup = new RadioGroup[1];
            final EditText[] ans_edit = new EditText[1];
            final RadioButton[] radioButton = new RadioButton[1];
            Log.e("size", adapter.getItemCount() + "");


            String[] data = adapter.getselectedData();
            String[] question = adapter.getselectedquestion();

            if (data != null) {
                for (int i = 0; i < data.length; i++) {
                    if (i != 0) {
                        question_id[0] = question_id[0] + "$#";
                        question_ans[0] = question_ans[0] + "$#";
                    }

                    String id = quizList.get(i).getId();
                    question_id[0] = question_id[0] + id;

                    if (data[i] != null) {
                        if (quizList.get(i).getQuiz_type() != null) {
                            if (quizList.get(i).getQuiz_type().equalsIgnoreCase("2")) {
                                if (!data[i].equalsIgnoreCase("")) {
                                    question_ans[0] = question_ans[0] + data[i];
                                } else {
                                    valid = false;
                                }
                            } else {

                                if (!data[i].equalsIgnoreCase("")) {

                                    String idno = quizList.get(i).getId();

                                    for (int j = 0; j < quizOptionList.size(); j++) {
                                        if (quizOptionList.get(j).getOption().equals(data[i]) && quizOptionList.get(j).getQuizId().equals(idno)) {
                                            question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                        }
                                    }
                                } else {
                                    valid = false;
                                }
                            }
                        } else {

                            if (!data[i].equalsIgnoreCase("")) {

                                String idno = quizList.get(i).getId();

                                for (int j = 0; j < quizOptionList.size(); j++) {
                                    if (quizOptionList.get(j).getOption().equals(data[i]) && quizOptionList.get(j).getQuizId().equals(idno)) {
                                        question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                    }
                                }
                            } else {
                                valid = false;
                            }
                        }
                    } else {
                        valid = false;
                    }

                }


                Log.e("valid", question_ans.toString());
                Log.e("valid", question_id.toString());
                Log.e("valid", valid.toString());

                Intent intent = new Intent(QuizNewActivity.this, YourScoreActivity.class);
                intent.putExtra("folderName", foldername);
                intent.putExtra("Answers", String.valueOf(adapter.getCorrectOption()));
                intent.putExtra("TotalQue", String.valueOf(adapter.getselectedData().length));
                startActivity(intent);
                finish();

//                if (valid == true) {
//                    quiz_question_id = question_id[0];
//                    quiz_options_id = question_ans[0];
//
//                    new postQuizQuestion().execute();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Please answer all questions", Toast.LENGTH_SHORT).show();
//                }


            }

//			//adapter.notifyDataSetChanged();
//			quizNameList.post(new Runnable() {
//				@Override
//				public void run() {
//
//						for (int i = 0; i < adapter.getItemCount(); i++) {
//
//							//View view = quizNameList.getChildAt(i);
//							View view = quizNameList.getLayoutManager().findViewByPosition(i);
////							View view= quizNameList.findViewHolderForAdapterPosition(i).itemView;
//
//							//QuizNewAdapter.ViewHolder holder= (QuizNewAdapter.ViewHolder) quizNameList.findViewHolderForAdapterPosition(i);
//
//
////						QuizNewAdapter.ViewHolder holder =
////								quizNameList.findViewHolderForItemId(adapter.getItemId(i));
//							//View view = holder.itemView;
//							//View view=adapter.getItemId();
////				if (view == null) {
////					try {
////						final int firstListItemPosition = 0;
////						final int lastListItemPosition = firstListItemPosition
////								+ quizNameList.getChildCount();
////
////						if (i < firstListItemPosition || i > lastListItemPosition) {
////							//This may occure using Android Monkey, else will work otherwise
//////							/view = quizNameList.getAdapter().getItemViewType(i, null, quizNameList);
//////							view = quizNameList.getAdapter().getItemViewType(i);
////							view=quizNameList.findViewHolderForAdapterPosition(i).itemView;
////						} else {
////							final int childIndex = i - firstListItemPosition;
////							view = quizNameList.getChildAt(childIndex);
////						}
////					} catch (Exception e) {
////						e.printStackTrace();
////						view = null;
////					}
////				}
//
//							if (i != 0) {
//								question_id[0] = question_id[0] + "$#";
//								question_ans[0] = question_ans[0] + "$#";
//							}
//
//							String id = quizList.get(i).getId();
//
//							question_id[0] = question_id[0] + id;
//
//
//							if (quizList.get(i).getQuiz_type().equalsIgnoreCase("1")) {
//
//								radioGroup[0] = view.findViewById(R.id.radiogroup);
//
//								int genid = radioGroup[0].getCheckedRadioButtonId();
//								radioButton[0] = radioGroup[0].findViewById(genid);
//								value[0] = radioButton[0].getText().toString();
//
//
//								for (int j = 0; j < quizOptionList.size(); j++) {
//									if (quizOptionList.get(j).getOption().equals(value[0]) && quizOptionList.get(j).getQuiz_id().equals(id)) {
//										question_ans[0] = question_ans[0] + quizOptionList.get(j).getOption_id();
//									}
//								}
//
//
//							} else if (quizList.get(i).getQuiz_type().equalsIgnoreCase("2")) {
//
//								ans_edit[0] = view.findViewById(R.id.ans_edit);
//
//								question_ans[0] = question_ans[0] + ans_edit[0].getText().toString().trim();
//
//								if (ans_edit[0].getText().toString().trim().equalsIgnoreCase("")) {
//									check[0] = check[0] + 1;
//								}
//							}
//						}
//
//					quiz_question_id = question_id[0];
//					quiz_options_id = question_ans[0];
//
//					if (quiz_question_id!=null && quiz_options_id!=null) {
//						if (!quiz_options_id.equalsIgnoreCase("") && !quiz_question_id.equalsIgnoreCase("")) {
//							if (check[0] == 0) {
//								new postQuizQuestion().execute();
//							} else {
//								Toast.makeText(QuizActivity.this, "Please answer all the questions", Toast.LENGTH_SHORT).show();
//							}
//						}
//					}
//				}
//			});
//
//
//			Log.e("id", question_id[0]);
//			Log.e("ans", question_ans[0]);
//
//			quiz_question_id = question_id[0];
//			quiz_options_id = question_ans[0];
//
//			if (quiz_question_id!=null && quiz_options_id!=null) {
//			    if (!quiz_options_id.equalsIgnoreCase("") && !quiz_question_id.equalsIgnoreCase("")) {
//                    if (check[0] == 0) {
//                        new postQuizQuestion().execute();
//                    } else {
//                        Toast.makeText(QuizActivity.this, "Please answer all the questions", Toast.LENGTH_SHORT).show();
//                    }
//                }
//			}
//


        }

    }


    private class postQuizQuestion extends AsyncTask<Void, Void, Void> {

        String error = "";
        String message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(QuizNewActivity.this,
                    R.style.Base_Theme_AppCompat_Dialog_Alert);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

            nameValuePair.add(new BasicNameValuePair("api_access_token",
                    accessToken));
            nameValuePair.add(new BasicNameValuePair("quiz_id", quiz_question_id));
            nameValuePair.add(new BasicNameValuePair("quiz_options_id",
                    quiz_options_id));

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(quizQuestionUrl,
                    ServiceHandler.POST, nameValuePair);
            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject jsonResult = new JSONObject(jsonStr);
                    error = jsonResult.getString("status");
                    message = jsonResult.getString("msg");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // Dismiss the progress dialog
            if (pDialog != null) {
                pDialog.dismiss();
                pDialog = null;
            }

            if (error.equalsIgnoreCase("success")) {

                Toast.makeText(QuizNewActivity.this, message, Toast.LENGTH_SHORT)
                        .show();

                finish();

            } else {

                Toast.makeText(QuizNewActivity.this, message,
                        Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(QuizNewActivity.this, FolderQuizActivity.class);
        startActivity(intent);
        finish();
    }
}

