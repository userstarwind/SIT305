package com.example.intellicareer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {
    private List<Job> jobList;
    private OnItemClickListener listener;

    private Activity activity;
    public interface OnItemClickListener {
        void onItemClick(Job job);
    }

    public JobAdapter(List<Job> jobList, Activity activity, OnItemClickListener listener) {
        this.jobList = jobList;
        this.listener = listener;
        this.activity=activity;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_item, parent, false);
        return new JobViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job = jobList.get(position);
        holder.bind(job, listener);
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public class JobViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView skillsTextView;
        private TextView salaryTextView;
        private TextView suitAbilityTextView;
        private ImageView iconImageView;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.job_title_text_view);
            skillsTextView = itemView.findViewById(R.id.job_skills_text_view);
            salaryTextView = itemView.findViewById(R.id.job_salary_text_view);
            suitAbilityTextView = itemView.findViewById(R.id.job_suit_ability_text_view);
            iconImageView = itemView.findViewById(R.id.job_image_view);
        }

        public void bind(Job job, OnItemClickListener listener) {
            titleTextView.setText(job.getTitle());
            skillsTextView.setText(job.getSkills());
            salaryTextView.setText(job.getMinSalary() + " - " + job.getMaxSalary() + " per month");
            Glide.with(activity)
                    .load(job.getIconUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(iconImageView);
            if (job.getWhetherShowSuitability()) {
                suitAbilityTextView.setText(job.getSuitAbility()+"%");
                suitAbilityTextView.setVisibility(View.VISIBLE);
            } else {
                suitAbilityTextView.setVisibility(View.GONE);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(job);
                }
            });
        }


    }
}