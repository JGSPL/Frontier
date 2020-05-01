package com.procialize.mrgeApp20.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.procialize.mrgeApp20.GetterSetter.Quiz;
import com.procialize.mrgeApp20.GetterSetter.QuizOptionList;
import com.procialize.mrgeApp20.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder> {
    private List<QuizOptionList> quizSpecificOptionListnew;
    private List<Quiz> quizList;
    private Context context;
    String correctAns;
    String SelectedAns;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;

    public AnswerAdapter(Context context, List<QuizOptionList> travelList, String correctAns, String SelectedAns) {
        this.quizSpecificOptionListnew = travelList;
        this.correctAns = correctAns;
        this.SelectedAns = SelectedAns;

        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_answers, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final QuizOptionList optionList = quizSpecificOptionListnew.get(position);
        String option = optionList.getOptionId();


        try {
            if (correctAns.equalsIgnoreCase(option)) {
                if (correctAns.equalsIgnoreCase(SelectedAns)) {
                    holder.raiolayout.setBackgroundResource(R.drawable.quiz_correct);

                    holder.radio.setBackgroundResource(R.drawable.checked_radio);
                } else if (option.equalsIgnoreCase(correctAns)) {
                    holder.raiolayout.setBackgroundResource(R.drawable.quiz_correct);
                    holder.radio.setBackgroundResource(R.drawable.unchecked_radio);
                }
            } else if (option.equalsIgnoreCase(SelectedAns)) {

                holder.raiolayout.setBackgroundResource(R.drawable.quiz_wrong);
                holder.radio.setBackgroundResource(R.drawable.checked_radio);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        holder.textno1.setText(optionList.getOption());
    }

    @Override
    public int getItemCount() {
        return quizSpecificOptionListnew.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textno1;
        ImageView radio;
        LinearLayout raiolayout;

        public ViewHolder(View itemView) {
            super(itemView);


            textno1 = (TextView) itemView.findViewById(R.id.textno1);
            radio = (ImageView) itemView.findViewById(R.id.radio);
            raiolayout = (LinearLayout) itemView.findViewById(R.id.raiolayout);


        }
    }
}
