package com.example.intellicareer.ui.dashboard;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.Manifest;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.content.pm.PackageManager;

import com.bumptech.glide.Glide;
import com.example.intellicareer.DBHelper;
import com.example.intellicareer.MyApplication;
import com.example.intellicareer.R;
import com.example.intellicareer.databinding.FragmentExploreBinding;
import com.example.intellicareer.databinding.FragmentPublishJobBinding;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.Date;
import java.util.Locale;

public class PublishJobFragment extends Fragment {
    private Uri imageUri;
    private FragmentPublishJobBinding binding;
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private ActivityResultLauncher<Intent> cropImageLauncher;
    private MyApplication myApplication;
    private DBHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPublishJobBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myApplication = (MyApplication) getActivity().getApplication();
        db = myApplication.getDbHelper();
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        if (result.getData() != null && result.getData().getData() != null) {
                            Uri sourceUri = result.getData().getData();
                            startCrop(sourceUri);
                        }
                    }
                });
        cropImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        final Uri resultUri = UCrop.getOutput(result.getData());
                        imageUri = resultUri;
                        Glide.with(this)
                                .load(resultUri)
                                .into(binding.publishIconImageView);
                    } else if (result.getResultCode() == UCrop.RESULT_ERROR) {
                        final Throwable cropError = UCrop.getError(result.getData());
                    }
                });
        binding.publishIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickImageLauncher.launch(intent);
            }
        });
        binding.publishButton.setOnClickListener(new View.OnClickListener(

        ) {
            @Override
            public void onClick(View v) {
                String title = binding.publishTitleTextView.getText().toString();
                String skills = binding.publishSkillsTextView.getText().toString();
                String minSalary = binding.publishMinSalaryTextView.getText().toString();
                String maxSalary = binding.publishMaxSalaryTextView.getText().toString();
                String description = binding.publishDescriptionTextView.getText().toString();
                if (title.isEmpty() || skills.isEmpty() || minSalary.isEmpty() || maxSalary.isEmpty() || description.isEmpty() || imageUri == null) {
                    Toast.makeText(getContext(), "Please fill in all fields and select an image.", Toast.LENGTH_SHORT).show();
                    return;
                }
                db.publishJob(getActivity(), imageUri, title, skills, minSalary, maxSalary, description);

            }
        });
    }


    private void startCrop(Uri sourceUri) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "CROPPED_" + timeStamp + ".jpg";
        File cropFile = new File(getActivity().getCacheDir(), fileName);

        try {
            Uri destinationUri = FileProvider.getUriForFile(getContext(),
                    getContext().getApplicationContext().getPackageName() + ".provider",
                    cropFile);
            UCrop uCrop = UCrop.of(sourceUri, destinationUri);
            uCrop.withAspectRatio(1, 1);
            uCrop.withMaxResultSize(1000, 1000);
            uCrop.withOptions(getUCropOptions());
            cropImageLauncher.launch(uCrop.getIntent(getContext()));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


    private UCrop.Options getUCropOptions() {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(80);
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(false);
        return options;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}