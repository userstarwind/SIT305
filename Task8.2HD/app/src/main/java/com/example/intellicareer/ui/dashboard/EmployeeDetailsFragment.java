package com.example.intellicareer.ui.dashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.intellicareer.DBHelper;
import com.example.intellicareer.MyApplication;
import com.example.intellicareer.R;
import com.example.intellicareer.databinding.FragmentEmployeeDetailsBinding;
import com.example.intellicareer.databinding.FragmentExploreBinding;
import com.example.intellicareer.databinding.FragmentJobDetailsBinding;
import com.example.intellicareer.databinding.FragmentRecruitBinding;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDetailsFragment extends Fragment {
    private FragmentEmployeeDetailsBinding binding;
    private MyApplication myApplication;
    private DBHelper db;
    private List<Employee> employeeList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication = (MyApplication) getActivity().getApplication();
        db = myApplication.getDbHelper();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEmployeeDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    employeeList=new ArrayList<>();
    binding.employeeDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    binding.employeeDetailsCancelJobButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            db.deleteJobById(getActivity(),myApplication.getRecruitJob().getJobId());
        }
    });
    binding.employeeDetailsRecyclerView.setAdapter(new EmployeeAdapter(employeeList, new EmployeeAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Employee employee) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(employee.getCVUrl()));
            startActivity(intent);
        }
    }));
    db.fetchEmployeeByIds(getActivity(),myApplication.getRecruitJob().getEmployeeIdList(),employeeList,binding.employeeDetailsRecyclerView);
    }

}