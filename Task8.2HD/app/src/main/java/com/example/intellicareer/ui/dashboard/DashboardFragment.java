package com.example.intellicareer.ui.dashboard;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.intellicareer.AIServiceAgent;
import com.example.intellicareer.DBHelper;
import com.example.intellicareer.MyApplication;
import com.example.intellicareer.R;
import com.example.intellicareer.ServiceResponse;
import com.example.intellicareer.databinding.FragmentDashboardBinding;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {
    private FragmentDashboardBinding binding;
    private List<MenuItem> menuItemList;
    private MyApplication myApplication;
    private DBHelper db;
    private ActivityResultLauncher<String> filePickerLauncher;
    private AIServiceAgent aiServiceAgent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication = (MyApplication) getActivity().getApplication();
        db = myApplication.getDbHelper();
        menuItemList = new ArrayList<>();
        menuItemList.add(new MenuItem(R.drawable.ic_upload_24px, "Upload CV"));
        menuItemList.add(new MenuItem(R.drawable.ic_publish_24px, "Publish Job"));
        menuItemList.add(new MenuItem(R.drawable.ic_recruit_24px, "Recruit"));
        menuItemList.add(new MenuItem(R.drawable.ic_logout_24px, "Log Out"));
        filePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                try {
                    myApplication.setCVContent(extractTextFromPdf(uri));
                    db.updateUserCV(getActivity(), uri);
                    aiServiceAgent = new AIServiceAgent();

                    aiServiceAgent.fetchCVEvaluation(getActivity(), myApplication.getCVContent(), new Callback<ServiceResponse>() {
                        @Override
                        public void onResponse(Call<ServiceResponse> call, Response<ServiceResponse> response) {
                            if (response.isSuccessful()) {
                                ServiceResponse serviceResponse = response.body();
                                myApplication.setCVEvaluation(serviceResponse.getMessage());
                                aiServiceAgent.fetchCVKeywords(getActivity(), myApplication.getCVContent(), new Callback<ServiceResponse>() {
                                    @Override
                                    public void onResponse(Call<ServiceResponse> call, Response<ServiceResponse> response) {
                                        if (response.isSuccessful()) {
                                            ServiceResponse serviceResponse = response.body();
                                            List<String> keyWordsList = Arrays.stream(serviceResponse.getMessage().split(","))
                                                    .map(String::trim)
                                                    .collect(Collectors.toList());
                                            db.updateUserKeyWords(getActivity(), keyWordsList);
                                            navigateToEvaluationFragment();
                                        } else {
                                            Toast.makeText(getActivity(), "Manage to update CV Keywords", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ServiceResponse> call, Throwable throwable) {
                                        Toast.makeText(getActivity(), "Fail to update CV Keywords", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } else {
                                Toast.makeText(getActivity(), "Manage to update CV Evaluation", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ServiceResponse> call, Throwable throwable) {
                            Toast.makeText(getActivity(), "Fail to update CV Evaluation", Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void navigateToEvaluationFragment() {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_home);
        navController.navigate(R.id.action_dashFragment_to_evaluationFragment);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.dashWelcomeBanner.setText("Welcome, " + myApplication.getCurrentUser().getDisplayName());
        GridView gridView = binding.dashMenuGridView;
        gridView.setAdapter(new DashMenuAdapter(getActivity(), menuItemList, new DashMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MenuItem menuItem) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_home);
                switch (menuItem.getText()) {
                    case "Upload CV":
                        pickPdfFile();
                        break;
                    case "Publish Job":
                        navController.navigate(R.id.action_dashFragment_to_publishJobFragment);
                        break;
                    case "Recruit":
                        navController.navigate(R.id.action_dashFragment_to_recruitFragment);
                        break;
                    case "Log Out":
                        db.signOut(getActivity());
                        break;
                    default:
                        break;
                }
            }
        }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void pickPdfFile() {
        filePickerLauncher.launch("application/pdf");
    }

    private String extractTextFromPdf(Uri uri) throws Exception {
        ContentResolver contentResolver = getActivity().getContentResolver();
        InputStream inputStream = contentResolver.openInputStream(uri);

        if (inputStream == null) {
            throw new Exception("Unable to open InputStream from URI");
        }

        PdfReader reader = new PdfReader(inputStream);
        PdfDocument pdfDocument = new PdfDocument(reader);

        StringBuilder extractedText = new StringBuilder();
        for (int i = 1; i <= pdfDocument.getNumberOfPages(); i++) {
            String pageText = PdfTextExtractor.getTextFromPage(pdfDocument.getPage(i));
            extractedText.append(pageText);
        }

        pdfDocument.close();
        reader.close();

        return extractedText.toString();
    }


}