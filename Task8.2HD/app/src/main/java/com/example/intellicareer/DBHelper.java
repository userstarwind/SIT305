package com.example.intellicareer;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.intellicareer.ui.dashboard.Employee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.UploadTask;


import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DBHelper {
    private FirebaseAuth mAuth;
    private static DBHelper dbHelperInstance;
    private MyApplication myApplication;
    private AlertDialog progressDialog;
    FirebaseFirestore db;
    private FirebaseStorage storage;

    private DBHelper(MyApplication myApplication) {
        this.myApplication = myApplication;
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public static DBHelper getInstance(MyApplication myApplication) {
        if (dbHelperInstance == null) {
            dbHelperInstance = new DBHelper(myApplication);
        }
        return dbHelperInstance;
    }

    private void showLoading(Activity activity) {
        activity.runOnUiThread(() -> progressDialog = MuskUtil.showProgressDialog(activity));
    }

    private void hideLoading(Activity activity) {
        if (progressDialog != null) {
            activity.runOnUiThread(() -> progressDialog.dismiss());
        }
    }

    private void navigateToMainActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    private void navigateToHomeActivity(Activity activity) {
        Intent intent = new Intent(activity, HomeActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public void createNewUser(Activity activity, String username, String email, String password) {
        showLoading(activity);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Map<String, Object> userData = new HashMap<>();
                                                userData.put("uid", user.getUid());
                                                userData.put("email", user.getEmail());
                                                userData.put("name", user.getDisplayName());
                                                userData.put("CVUrl", "");
                                                userData.put("CVKeyWords", new ArrayList<>());
                                                userData.put("createdAt", FieldValue.serverTimestamp());
                                                db.collection("users").document(user.getUid())
                                                        .set(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    activity.runOnUiThread(() -> Toast.makeText(activity, "Managed to create account",
                                                                            Toast.LENGTH_SHORT).show());
                                                                    hideLoading(activity);
                                                                    navigateToMainActivity(activity);
                                                                } else {
                                                                    activity.runOnUiThread(() -> Toast.makeText(activity, "Failed to setup account",
                                                                            Toast.LENGTH_LONG).show());
                                                                    hideLoading(activity);
                                                                }
                                                            }
                                                        });
                                            } else {
                                                activity.runOnUiThread(() -> Toast.makeText(activity, "Failed to setup account",
                                                        Toast.LENGTH_LONG).show());
                                                hideLoading(activity);
                                            }
                                        }
                                    });
                        } else {
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                            activity.runOnUiThread(() -> Toast.makeText(activity, "Authentication failed: " + errorMessage,
                                    Toast.LENGTH_LONG).show());
                            hideLoading(activity);
                        }
                    }
                });
    }

    public void loginWithEmailAndPassword(Activity activity, String email, String password) {
        showLoading(activity);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            activity.runOnUiThread(() -> Toast.makeText(activity, "Managed to login",
                                    Toast.LENGTH_SHORT).show());
                            myApplication.setCurrentUser(mAuth.getCurrentUser());
                            navigateToHomeActivity(activity);
                            hideLoading(activity);
                        } else {
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                            activity.runOnUiThread(() -> Toast.makeText(activity, "Authentication failed: " + errorMessage,
                                    Toast.LENGTH_LONG).show());
                            hideLoading(activity);
                        }
                    }
                });
    }

    public void resetPasswordByEmail(Activity activity, String email) {
        showLoading(activity);
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        hideLoading(activity);
                        if (task.isSuccessful()) {
                            activity.runOnUiThread(() -> Toast.makeText(activity, "The password reset email has been sent, please check your email.", Toast.LENGTH_SHORT).show());
                        } else {
                            activity.runOnUiThread(() -> Toast.makeText(activity, "Failed to send email：" + task.getException().getMessage(), Toast.LENGTH_LONG).show());
                        }
                    }
                });
    }

    public void checkWhetherUserLogin(Activity activity) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            myApplication.setCurrentUser(currentUser);
            navigateToHomeActivity(activity);
        }
    }

    public void signOut(Activity activity) {
        mAuth.signOut();
        navigateToMainActivity(activity);
    }

    public void publishJob(Activity activity, Uri icon, String title, String skills, String minSalary, String maxSalary, String description) {
        showLoading(activity);
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("images/" + icon.getLastPathSegment());
        imageRef.putFile(icon)
                .addOnSuccessListener(taskSnapshot -> {
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        LocalDate localDate = LocalDate.now();
                        Map<String, Object> jobData = new HashMap<>();
                        jobData.put("iconUrl", downloadUrl);
                        jobData.put("date", localDate.toString());
                        jobData.put("title", title);
                        jobData.put("skills", skills);
                        jobData.put("minSalary", minSalary);
                        jobData.put("maxSalary", maxSalary);
                        jobData.put("description", description);
                        jobData.put("employerId", myApplication.getCurrentUser().getUid());
                        jobData.put("employeeIdList", new ArrayList<>());
                        db.collection("jobs").add(jobData)
                                .addOnSuccessListener(documentReference -> {
                                    activity.runOnUiThread(() -> Toast.makeText(activity, "Managed to publish job", Toast.LENGTH_SHORT).show());
                                    NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment_activity_home);
                                    navController.navigate(R.id.action_publishJobFragment_to_dashFragment);
                                    hideLoading(activity);
                                })
                                .addOnFailureListener(e -> {
                                    activity.runOnUiThread(() -> Toast.makeText(activity, "Failed to publish job", Toast.LENGTH_SHORT).show());
                                    hideLoading(activity);
                                });
                    });
                })
                .addOnFailureListener(exception -> {
                    activity.runOnUiThread(() -> Toast.makeText(activity, "Failed to upload icon image", Toast.LENGTH_SHORT).show());
                    hideLoading(activity);
                });
    }

    public void fetchAllJobs(Activity activity, List<Job> jobList, List<Job> filteredjobList, RecyclerView recyclerView) {
        showLoading(activity);
        db.collection("jobs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            jobList.clear();
                            filteredjobList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Data", document.getId() + " => " + document.getData());
                                Map<String, Object> jobData = document.getData();
                                String iconUrl = (String) jobData.get("iconUrl");
                                String date = (String) jobData.get("date");
                                String title = (String) jobData.get("title");
                                String skills = (String) jobData.get("skills");
                                String minSalary = (String) jobData.get("minSalary");
                                String maxSalary = (String) jobData.get("maxSalary");
                                String description = (String) jobData.get("description");
                                String employerId = (String) jobData.get("employerId");
                                List<String> employeeIdList = (List<String>) jobData.get("employeeIdList");
                                jobList.add(new Job(document.getId(), title, skills, minSalary, maxSalary, description, date, iconUrl, false, 0, employerId, employeeIdList));
                            }
                            filteredjobList.addAll(jobList);
                            recyclerView.getAdapter().notifyDataSetChanged();
                            hideLoading(activity);
                        } else {
                            Log.w("Error", "Error getting documents.", task.getException());
                            hideLoading(activity);
                        }
                    }
                });
    }

    public void updateUserCV(Activity activity, Uri uri) {
        showLoading(activity);
        StorageReference storageRef = storage.getReference();
        StorageReference pdfRef = storageRef.child("pdfs/" + System.currentTimeMillis() + ".pdf");

        ContentResolver contentResolver = activity.getContentResolver();
        InputStream inputStream;
        try {
            inputStream = contentResolver.openInputStream(uri);
        } catch (Exception e) {
            Log.e(TAG, "Error opening InputStream", e);
            return;
        }

        UploadTask uploadTask = pdfRef.putStream(inputStream);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            pdfRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                String downloadUrl = downloadUri.toString();
                DocumentReference docRef = db.collection("users").document(myApplication.getCurrentUser().getUid());
                Map<String, Object> updates = new HashMap<>();
                updates.put("CVUrl", downloadUrl);
                docRef.update(updates)
                        .addOnSuccessListener(aVoid -> hideLoading(activity))
                        .addOnFailureListener(exception -> {
                            activity.runOnUiThread(() -> Toast.makeText(activity, "Failed to upload CV", Toast.LENGTH_SHORT).show());
                            hideLoading(activity);
                        });

            }).addOnFailureListener(exception -> {
                activity.runOnUiThread(() -> Toast.makeText(activity, "Failed to upload CV", Toast.LENGTH_SHORT).show());
                hideLoading(activity);
            });
        }).addOnFailureListener(exception -> {
            activity.runOnUiThread(() -> Toast.makeText(activity, "Failed to upload CV", Toast.LENGTH_SHORT).show());
            hideLoading(activity);
        });
    }

    public void updateUserKeyWords(Activity activity, List<String> keywords) {
        showLoading(activity);
        DocumentReference docRef = db.collection("users").document(myApplication.getCurrentUser().getUid());
        Map<String, Object> updates = new HashMap<>();
        updates.put("CVKeyWords", keywords);
        docRef.update(updates)
                .addOnSuccessListener(aVoid -> hideLoading(activity))
                .addOnFailureListener(exception -> {
                    activity.runOnUiThread(() -> Toast.makeText(activity, "Failed to update CV Key Words", Toast.LENGTH_SHORT).show());
                    hideLoading(activity);
                });
    }

    public void updateJobEmployeeIdList(Activity activity, List<String> employeeIdList) {
        showLoading(activity);
        DocumentReference docRef = db.collection("jobs").document(myApplication.getSelectedJob().getJobId());
        Map<String, Object> updates = new HashMap<>();
        updates.put("employeeIdList", employeeIdList);
        docRef.update(updates)
                .addOnSuccessListener(aVoid -> {
                    hideLoading(activity);
                })
                .addOnFailureListener(exception -> {
                    activity.runOnUiThread(() -> Toast.makeText(activity, "Failed to apply or cancel application", Toast.LENGTH_SHORT).show());
                    hideLoading(activity);
                });
    }


    public void fetchAllRecruitJobs(Activity activity, List<Job> jobList,List<Job> filteredRecruitJobList, RecyclerView recyclerView, String employerId) {
        showLoading(activity);
        db.collection("jobs")
                .whereEqualTo("employerId", employerId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            jobList.clear();
                            filteredRecruitJobList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Data", document.getId() + " => " + document.getData());
                                Map<String, Object> jobData = document.getData();
                                String iconUrl = (String) jobData.get("iconUrl");
                                String date = (String) jobData.get("date");
                                String title = (String) jobData.get("title");
                                String skills = (String) jobData.get("skills");
                                String minSalary = (String) jobData.get("minSalary");
                                String maxSalary = (String) jobData.get("maxSalary");
                                String description = (String) jobData.get("description");
                                List<String> employeeIdList = (List<String>) jobData.get("employeeIdList");
                                jobList.add(new Job(document.getId(), title, skills, minSalary, maxSalary, description, date, iconUrl, false, 0, employerId, employeeIdList));
                            }
                            filteredRecruitJobList.addAll(jobList);
                            recyclerView.getAdapter().notifyDataSetChanged();
                            hideLoading(activity);
                        } else {
                            activity.runOnUiThread(() -> Toast.makeText(activity, "Failed to fetch", Toast.LENGTH_SHORT).show());
                            hideLoading(activity);
                        }
                    }
                });
    }

    public void fetchEmployeeByIds(Activity activity, List<String> employeeIdList, List<Employee> employeeList, RecyclerView recyclerView) {
        if (employeeIdList == null || employeeIdList.isEmpty()) {
            activity.runOnUiThread(() -> Toast.makeText(activity, "No employee applied this job", Toast.LENGTH_SHORT).show());
            return;
        }


        employeeList.clear();

        for (String id : employeeIdList) {
            showLoading(activity);
            db.collection("users").document(id).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Map<String, Object> userData = document.getData();
                                    String CVUrl = (String) userData.get("CVUrl");
                                    String name = (String) userData.get("name");
                                    String email = (String) userData.get("email");
                                    employeeList.add(new Employee(name, email, CVUrl));
                                    recyclerView.getAdapter().notifyDataSetChanged();
                                    hideLoading(activity);
                                } else {
                                    Log.d("Data", "No such document with id " + id);
                                }
                            } else {
                                Log.w("Error", "Error getting document with id " + id, task.getException());
                            }
                        }
                    });
        }

    }

    public void deleteJobById(Activity activity, String documentId) {
        showLoading(activity);
        db.collection("jobs").document(documentId).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        activity.runOnUiThread(() -> Toast.makeText(activity, "Managed to cancel the job", Toast.LENGTH_SHORT).show());
                        hideLoading(activity);
                        NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment_activity_home);
                        navController.navigate(R.id.action_employeeDetailsFragment_to_recruitFragment);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        activity.runOnUiThread(() -> Toast.makeText(activity, "Fail to cancel the job", Toast.LENGTH_SHORT).show());
                        hideLoading(activity);
                    }
                });
    }

    public void fetchUserKeyWordsAndUpdateSuitAbility(Activity activity, String userId, List<String> keywords,List<Job> filteredJobList,RecyclerView recyclerView) {
      showLoading(activity);
        db.collection("users").document(userId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                keywords.clear();
                                keywords.addAll((List<String>) document.get("CVKeyWords"));
                                for (Job job : filteredJobList) {
                                    String jobInfo = job.getTitle() + job.getSkills() + job.getDescription();
                                    job.setSuitAbility(calculateMatchPercentage(keywords, jobInfo));
                                    job.setWhetherShowSuitability(true);
                                }
                                filteredJobList.sort(new Comparator<Job>() {
                                    public int compare(Job j1, Job j2) {
                                        return Integer.compare(j2.getSuitAbility(), j1.getSuitAbility());
                                    }
                                });
                                recyclerView.getAdapter().notifyDataSetChanged();
                                hideLoading(activity);
                            } else {
                                activity.runOnUiThread(() -> Toast.makeText(activity, "Fail to searching", Toast.LENGTH_SHORT).show());
                                hideLoading(activity);
                            }
                        } else {
                            activity.runOnUiThread(() -> Toast.makeText(activity, "Fail to searching", Toast.LENGTH_SHORT).show());
                            hideLoading(activity);
                        }
                    }
                });
    }

    private int calculateMatchPercentage(List<String> stringList, String inputString) {
        if (stringList == null || stringList.isEmpty() || inputString == null || inputString.isEmpty()) {
            return 0;
        }

        int totalStrings = stringList.size();
        int matchCount = 0;

        String lowerCaseInputString = inputString.toLowerCase();

        for (String str : stringList) {
            if (lowerCaseInputString.contains(str.toLowerCase())) {
                matchCount++;
            }
        }

        return (int) ((matchCount / (double) totalStrings) * 100);
    }
}





