package com.example.intellicareer.ui.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.intellicareer.Job;
import com.example.intellicareer.JobAdapter;
import com.example.intellicareer.R;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {
    private List<Employee> employeeList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Employee employee);
    }

    public EmployeeAdapter(List<Employee> employeeList, OnItemClickListener listener) {
        this.employeeList = employeeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EmployeeAdapter.EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_item, parent, false);
        return new EmployeeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeAdapter.EmployeeViewHolder holder, int position) {
       Employee employee= employeeList.get(position);
        holder.bind(employee, listener);
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public class EmployeeViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView emailTextView;
        private Button downloadButton;
        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView=itemView.findViewById(R.id.employee_item_name_text_view);
            emailTextView=itemView.findViewById(R.id.employee_item_email_text_view);
            downloadButton=itemView.findViewById(R.id.employee_item_download_cv_button);
        }

        public void bind(Employee employee, OnItemClickListener listener) {
            nameTextView.setText(employee.getName());
            emailTextView.setText("Email: "+employee.getEmail());
            downloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(employee);
                }
            });
        }
    }
}
