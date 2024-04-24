package com.example.quizapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import android.app.AlertDialog;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DBHelper {

    private FirebaseStorage storage;
    private StorageReference storageRef;
    private FirebaseFirestore db;
    private MyApplication myApplication;
    private AlertDialog progressDialog;
    private static DBHelper dbHelperInstance;

    private DBHelper(MyApplication myApplication) {
        this.myApplication = myApplication;
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        db = FirebaseFirestore.getInstance();
    }

    public static DBHelper getInstance(MyApplication myApplication) {
        if (dbHelperInstance == null) {
            dbHelperInstance = new DBHelper(myApplication);
        }
        return dbHelperInstance;
    }

    private void navigateToMainActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    private void navigateToTaskActivity(Activity activity) {
        Intent intent = new Intent(activity, TaskActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    private void navigateToInterestActivity(Activity activity) {
        Intent intent = new Intent(activity, InterestActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    private void showLoading(Activity activity) {
        activity.runOnUiThread(() -> progressDialog = MuskUtil.showProgressDialog(activity));
    }

    private void hideLoading(Activity activity) {
        if (progressDialog != null) {
            activity.runOnUiThread(() -> progressDialog.dismiss());
        }
    }

    public void createNewUser(Activity activity, String fullName, String username, String password, Uri imageUri) {
        showLoading(activity);
        db.collection("users").whereEqualTo("username", username).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    StorageReference imageRef = storageRef.child("user_images/" + imageUri.getLastPathSegment());
                    imageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                        imageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                            String imageUrl = downloadUri.toString();
                            createUserInFirestore(activity, fullName, username, password, imageUrl);
                            loginWithUsername(activity,username,password);
                            activity.runOnUiThread(() -> Toast.makeText(activity, "User created successfully", Toast.LENGTH_SHORT).show());
                        }).addOnFailureListener(e -> {
                            hideLoading(activity);
                            activity.runOnUiThread(() -> Toast.makeText(activity, "Failed to get download URL", Toast.LENGTH_SHORT).show());
                        });
                    }).addOnFailureListener(e -> {
                        hideLoading(activity);
                        activity.runOnUiThread(() -> Toast.makeText(activity, "Image upload failed", Toast.LENGTH_SHORT).show());
                    });
                } else {
                    hideLoading(activity);
                    activity.runOnUiThread(() -> Toast.makeText(activity, "Username already exists", Toast.LENGTH_SHORT).show());
                }
            } else {
                hideLoading(activity);
                activity.runOnUiThread(() -> Toast.makeText(activity, "Error checking username", Toast.LENGTH_SHORT).show());
            }

        });
    }


    private void createUserInFirestore(Activity activity, String fullName, String username, String password, String imageUrl) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("fullName", fullName);
        userData.put("username", username);
        userData.put("password", password);
        userData.put("taskList", new ArrayList<>());
        userData.put("avatarUrl", imageUrl);
        userData.put("interestList", new ArrayList<>());
        db.collection("users").add(userData).addOnSuccessListener(documentReference -> {
            Log.d("Firestore", "DocumentSnapshot added with ID: " + documentReference.getId());
        }).addOnFailureListener(e -> {
            Log.e("Firestore", "Error adding document", e);
        });
    }

    public void loginWithUsername(Activity activity, String username, String password) {
        showLoading(activity);
        db.collection("users")
                .whereEqualTo("username", username)
                .whereEqualTo("password", password)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot result = task.getResult();
                        if (result != null && !result.isEmpty()) {
                            DocumentSnapshot document = result.getDocuments().get(0);
                            MyApplication myApp = (MyApplication) activity.getApplication();
                            User currentUser = document.toObject(User.class);
                            currentUser.setId(document.getId());
                            myApp.setCurrentUser(currentUser);
                            if (!currentUser.getInterestList().isEmpty()) {
                                navigateToTaskActivity(activity);
                            } else {
                                navigateToInterestActivity(activity);
                            }
                            activity.runOnUiThread(() -> Toast.makeText(activity, "Login successful", Toast.LENGTH_SHORT).show());
                        } else {
                            activity.runOnUiThread(() -> Toast.makeText(activity, "Invalid username or password", Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        activity.runOnUiThread(() -> Toast.makeText(activity, "Login failed", Toast.LENGTH_SHORT).show());
                    }
                    hideLoading(activity);
                });
    }

    public void updateUserTaskListById(Activity activity, String userId, List<Task> taskList) {
        showLoading(activity);
        db.collection("users").document(userId)
                .update("taskList", taskList)
                .addOnSuccessListener(aVoid -> {
                    activity.runOnUiThread(() -> Toast.makeText(activity, "Task list updated successfully", Toast.LENGTH_SHORT).show());
                    hideLoading(activity);
                })
                .addOnFailureListener(e -> {
                    activity.runOnUiThread(() -> Toast.makeText(activity, "Failed to update task list", Toast.LENGTH_SHORT).show());
                    hideLoading(activity);
                });
    }

    public void updateUserInterestListById(Activity activity, String userId, List<String> interestList) {
        showLoading(activity);
        db.collection("users").document(userId)
                .update("interestList", interestList)
                .addOnSuccessListener(aVoid -> {
                    navigateToTaskActivity(activity);
                    activity.runOnUiThread(() -> Toast.makeText(activity, "interest list updated successfully", Toast.LENGTH_SHORT).show());
                    hideLoading(activity);

                })
                .addOnFailureListener(e -> {
                    activity.runOnUiThread(() -> Toast.makeText(activity, "Failed to update interest list", Toast.LENGTH_SHORT).show());
                    hideLoading(activity);
                });
    }

}

