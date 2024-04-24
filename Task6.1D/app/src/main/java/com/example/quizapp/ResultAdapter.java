package com.example.quizapp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResulViewHolder> {

    private List<Question> questionList;
    private List<Boolean> answerList;

    public ResultAdapter(List<Question> questionList, List<Boolean> answerList) {
        this.questionList = questionList;
        this.answerList = answerList;
    }

    @NonNull
    @Override
    public ResultAdapter.ResulViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_item, parent, false);
        return new ResulViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultAdapter.ResulViewHolder holder, int position) {
        Question question = questionList.get(position);
        Boolean answer = answerList.get(position);
        holder.bind(question, answer, position);
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class ResulViewHolder extends RecyclerView.ViewHolder {
        private TextView questionTextView;
        private TextView answerTextView;
        private LinearLayout resultBackground;

        public ResulViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.result_question_textview);
            answerTextView = itemView.findViewById(R.id.result_answer_textview);
            resultBackground = itemView.findViewById(R.id.result_background);
        }


        public void bind(Question question, Boolean answer, int position) {
            int number = position + 1;
            questionTextView.setText("Q" + number + ": " + question.getQuestion() + 1);
            answerTextView.setText(question.getOptions().get(question.getFormattedAnswer()));
            if (answer) {
                resultBackground.setBackgroundColor(Color.parseColor("#67D642"));
            } else {
                resultBackground.setBackgroundColor(Color.parseColor("#E63A3A"));
            }
        }
    }
}
