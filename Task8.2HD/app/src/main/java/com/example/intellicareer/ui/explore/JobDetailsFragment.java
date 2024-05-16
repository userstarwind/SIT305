package com.example.intellicareer.ui.explore;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.intellicareer.DBHelper;
import com.example.intellicareer.Job;
import com.example.intellicareer.MyApplication;
import com.example.intellicareer.R;
import com.example.intellicareer.databinding.FragmentJobDetailsBinding;


public class JobDetailsFragment extends Fragment {

    private FragmentJobDetailsBinding binding;
    private MyApplication myApplication;
    private Job currentJob;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentJobDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myApplication = (MyApplication) getActivity().getApplication();
        currentJob = myApplication.getSelectedJob();
        Glide.with(getActivity())
                .load(currentJob.getIconUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.jobDetailsIconImageView);
        binding.jobDetailsTitleTextView.setText(currentJob.getTitle());
        binding.jobDetailsSkillsTextView.setText(currentJob.getSkills());
        binding.jobDetailsMinSalaryTextView.setText(currentJob.getMinSalary() + " AUD/per month");
        binding.jobDetailsMaxSalaryTextView.setText(currentJob.getMaxSalary() + " AUD/per month");
        binding.jobDetailsDescriptionTextView.setText(currentJob.getDescription());
        if(currentJob.getEmployeeIdList().contains(myApplication.getCurrentUser().getUid())){
            binding.jobDetailsApplyButton.setText("Applied");
            binding.jobDetailsApplyButton.setBackgroundResource(R.drawable.btn_warning);
        }else{
            binding.jobDetailsApplyButton.setText("Apply");
            binding.jobDetailsApplyButton.setBackgroundResource(R.drawable.btn_default);
        }
        binding.jobDetailsApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentJob.getEmployeeIdList().contains(myApplication.getCurrentUser().getUid())){
                    binding.jobDetailsApplyButton.setText("Apply");
                    binding.jobDetailsApplyButton.setBackgroundResource(R.drawable.btn_default);
                    currentJob.getEmployeeIdList().remove(myApplication.getCurrentUser().getUid());
                    myApplication.getDbHelper().updateJobEmployeeIdList(getActivity(),currentJob.getEmployeeIdList());
                }else{
                    binding.jobDetailsApplyButton.setText("Cancel");
                    binding.jobDetailsApplyButton.setBackgroundResource(R.drawable.btn_warning);
                    currentJob.getEmployeeIdList().add(myApplication.getCurrentUser().getUid());
                    myApplication.getDbHelper().updateJobEmployeeIdList(getActivity(),currentJob.getEmployeeIdList());
                }
            }
        });
    }
}