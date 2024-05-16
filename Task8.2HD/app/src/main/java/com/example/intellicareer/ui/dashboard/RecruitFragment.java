package com.example.intellicareer.ui.dashboard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.intellicareer.DBHelper;
import com.example.intellicareer.Job;
import com.example.intellicareer.JobAdapter;
import com.example.intellicareer.MyApplication;
import com.example.intellicareer.R;
import com.example.intellicareer.databinding.FragmentPublishJobBinding;
import com.example.intellicareer.databinding.FragmentRecruitBinding;

import java.util.ArrayList;
import java.util.List;

public class RecruitFragment extends Fragment {
    private FragmentRecruitBinding binding;
    private MyApplication myApplication;
    private DBHelper db;
    private List<Job> recruitJobList;
private List<Job> filteredRecruitJobList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication = (MyApplication) getActivity().getApplication();
        db = myApplication.getDbHelper();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecruitBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recruitJobList = new ArrayList<>();
        filteredRecruitJobList=new ArrayList<>();
        binding.recruitRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recruitRecyclerView.setAdapter(new JobAdapter(filteredRecruitJobList, getActivity(), new JobAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Job job) {
                myApplication.setRecruitJob(job);
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_home);
                navController.navigate(R.id.action_recruitFragment_to_employerDetailsFragment);
            }
        }));
        db.fetchAllRecruitJobs(getActivity(), recruitJobList,filteredRecruitJobList, binding.recruitRecyclerView, myApplication.getCurrentUser().getUid());
        binding.recruitSearchEditText.setText("");
        binding.recruitSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    String searchText = s.toString().toLowerCase();
                    filteredRecruitJobList.clear();
                    for (Job job : recruitJobList) {
                        if (job.getDescription().toLowerCase().contains(searchText) ||
                                job.getSkills().toLowerCase().contains(searchText) ||
                                job.getTitle().toLowerCase().contains(searchText)) {
                            filteredRecruitJobList.add(job);
                        }
                    }
                } else {
                    filteredRecruitJobList.clear();
                    filteredRecruitJobList.addAll(recruitJobList);
                }
                binding.recruitRecyclerView.getAdapter().notifyDataSetChanged();
            }

        });
    }
}