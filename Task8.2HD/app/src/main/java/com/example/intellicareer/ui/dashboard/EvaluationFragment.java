package com.example.intellicareer.ui.dashboard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.intellicareer.AIServiceAgent;
import com.example.intellicareer.DBHelper;
import com.example.intellicareer.MyApplication;
import com.example.intellicareer.R;
import com.example.intellicareer.ServiceResponse;
import com.example.intellicareer.databinding.FragmentDashboardBinding;
import com.example.intellicareer.databinding.FragmentEvaluationBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EvaluationFragment extends Fragment {
    private FragmentEvaluationBinding binding;
    private List<Message> messageList;
    private MyApplication myApplication;
    private DBHelper db;
    private AIServiceAgent aiServiceAgent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication = (MyApplication) getActivity().getApplication();
        db = myApplication.getDbHelper();
        messageList = new ArrayList<>();
        messageList.add(new Message("Left", "Welcome " + myApplication.getCurrentUser().getDisplayName() + "! Here is an evaluation of your CV."));
        messageList.add(new Message("Left", myApplication.getCVEvaluation()));
        aiServiceAgent=new AIServiceAgent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEvaluationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.evaluationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.evaluationRecyclerView.setAdapter(new ChatAdapter(messageList));
        binding.evaluationSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageContent = binding.evaluationMessageEditText.getText().toString();
                if (!messageContent.isEmpty()) {
                    aiServiceAgent.fetchOtherResponse(getActivity(), myApplication.getCVContent(), messageContent, new Callback<ServiceResponse>() {
                        @Override
                        public void onResponse(Call<ServiceResponse> call, Response<ServiceResponse> response) {
                            if (response.isSuccessful()) {
                                ServiceResponse serviceResponse = response.body();
                                messageList.add(new Message("Right",messageContent));
                                messageList.add(new Message("Left",serviceResponse.getMessage()));
                                binding.evaluationRecyclerView.getAdapter().notifyDataSetChanged();
                                binding.evaluationRecyclerView.smoothScrollToPosition(messageList.size() - 1);
                                binding.evaluationMessageEditText.setText("");
                            } else {
                                Toast.makeText(getActivity(), "Error sending message", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ServiceResponse> call, Throwable throwable) {
                            Toast.makeText(getActivity(), "Error sending message", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Please enter message", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}