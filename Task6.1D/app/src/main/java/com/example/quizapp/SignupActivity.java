package com.example.quizapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.media.effect.Effect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.Manifest;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;

import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;

public class SignupActivity extends AppCompatActivity {
    private ImageView selectedAvatar;
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private ActivityResultLauncher<Uri> takePhotoLauncher;
    private Uri imageUri;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private EditText signFullNameEditText;
    private EditText signUserNameEditText;
    private EditText signPasswordEditText;
    private EditText signConfirmPasswordEditText;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        MyApplication myApp = (MyApplication) getApplication();
        db = myApp.getDbHelper();
        signFullNameEditText = findViewById(R.id.sign_full_name_edittext);
        signUserNameEditText = findViewById(R.id.sign_user_name_edittext);
        signPasswordEditText = findViewById(R.id.sign_password_edittext);
        signConfirmPasswordEditText = findViewById(R.id.sign_confirm_password_edittext);
        selectedAvatar = findViewById(R.id.selected_avatar);
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null && result.getData().getData() != null) {
                            Uri sourceUri = result.getData().getData();
                            startCrop(sourceUri);
                        }
                    }
                });

        takePhotoLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                result -> {
                    if (result) {
                        startCrop(imageUri);
                    }
                });

    }


    private void startCrop(Uri sourceUri) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "CROPPED_" + timeStamp + ".jpg";
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), fileName));

        UCrop uCrop = UCrop.of(sourceUri, destinationUri);
        uCrop.withAspectRatio(1, 1);
        uCrop.withMaxResultSize(1000, 1000);
        uCrop.withOptions(getUCropOptions());
        uCrop.start(this);
    }

    private UCrop.Options getUCropOptions() {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(80);
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(false);
        return options;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            imageUri = resultUri;
            Glide.with(this)
                    .load(resultUri)
                    .circleCrop()
                    .into(selectedAvatar);
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Log.e("SignupActivity", "Crop error: " + cropError);
        }
    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        } else {
            takePhoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                Toast.makeText(this, "Camera permission is necessary", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void selectAvatar(View view) {
        showImagePickerDialog();
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
                if (photoFile != null) {
                    imageUri = FileProvider.getUriForFile(this,
                            "com.example.quizapp.fileprovider",
                            photoFile);
                    takePhotoLauncher.launch(imageUri);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        return image;
    }


    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }


    private void showImagePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose image source");
        builder.setItems(new CharSequence[]{"Take photo", "Pick from gallery", "Cancel"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        requestCameraPermission();
                        break;
                    case 1:
                        pickFromGallery();
                        break;
                    case 2:
                        dialog.dismiss();
                        break;
                }
            }
        });
        builder.show();
    }


    public void createAccount(View view) {
        String fullName = signFullNameEditText.getText().toString();
        String username = signUserNameEditText.getText().toString();
        String password = signPasswordEditText.getText().toString();
        String confirmPassword = signConfirmPasswordEditText.getText().toString();
        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill all fields and select an image.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "The two password inputs are inconsistent.", Toast.LENGTH_LONG).show();
            return;
        }
        db.createNewUser(this, fullName, username, password, imageUri);
    }

    public void backToHome(View view) {
        Intent intent=new Intent(SignupActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}