package com.ab.rakshasutra;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.rakshasutra.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerVH>{

    List<Answer> answerList;

    public AnswerAdapter(List<Answer> answerList) {
        this.answerList = answerList;
    }

    @NonNull
    @Override
    public AnswerVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new AnswerVH(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AnswerVH holder, int position) {

        Answer answer = answerList.get(position);
        holder.questionTxt.setText(answer.getQuestion());
        holder.answerTxt.setText(answer.getAnswer());

        boolean isExpandable = answerList.get(position).isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);


    }

    @Override
    public int getItemCount() {
        return answerList.size();
    }

    public class AnswerVH extends RecyclerView.ViewHolder {

        TextView questionTxt, answerTxt;
        LinearLayout linearLayout;
        RelativeLayout expandableLayout;

        public AnswerVH(@NonNull View itemView) {
            super(itemView);

            questionTxt = itemView.findViewById(R.id.question);
            answerTxt = itemView.findViewById(R.id.answer);

            linearLayout = itemView.findViewById(R.id.linear_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Answer answer = answerList.get(getAdapterPosition());
                    answer.setExpandable(answer.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });


        }
    }
}
