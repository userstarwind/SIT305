package com.example.intellicareer.ui.explore;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.example.intellicareer.DBHelper;
import com.example.intellicareer.Job;
import com.example.intellicareer.JobAdapter;
import com.example.intellicareer.MyApplication;
import com.example.intellicareer.R;
import com.example.intellicareer.databinding.FragmentExploreBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ExploreFragment extends Fragment {
    private MyApplication myApplication;
    private DBHelper db;
    private FragmentExploreBinding binding;
    private List<Job> jobList;
    private List<Job> filteredJobList;
private List<String> keywords;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentExploreBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        jobList = new ArrayList<>();
        keywords = new ArrayList<>();
        filteredJobList = new ArrayList<>();
        myApplication = (MyApplication) getActivity().getApplication();
        db = myApplication.getDbHelper();
        binding.exploreRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.exploreRecyclerView.setAdapter(new JobAdapter(filteredJobList, getActivity(), new JobAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Job job) {
                myApplication.setSelectedJob(job);
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_home);
                navController.navigate(R.id.action_exploreFragment_to_jobDetailsFragment);
            }
        }));
        db.fetchAllJobs(getActivity(), jobList, filteredJobList, binding.exploreRecyclerView);
        binding.exploreSearchBar.setText("");
        binding.exploreAiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.fetchUserKeyWordsAndUpdateSuitAbility(getActivity(), myApplication.getCurrentUser().getUid(), keywords,filteredJobList,binding.exploreRecyclerView);
            }
        });
        binding.exploreSearchBar.addTextChangedListener(new TextWatcher() {
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
                    filteredJobList.clear();
                    for (Job job : jobList) {
                        if (job.getDescription().toLowerCase().contains(searchText) ||
                                job.getSkills().toLowerCase().contains(searchText) ||
                                job.getTitle().toLowerCase().contains(searchText)) {
                            filteredJobList.add(job);
                        }
                    }
                } else {
                    filteredJobList.clear();
                    filteredJobList.addAll(jobList);
                }
                binding.exploreRecyclerView.getAdapter().notifyDataSetChanged();
            }

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }




}