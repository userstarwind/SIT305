package com.example.task81c;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView chatRecyclerView;
    private List<Message> messageList;
    private EditText messageEditText;
    private ChatAdapter chatAdapter;
    private AIServiceAgent aiServiceAgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
        chatRecyclerView = findViewById(R.id.chat_recycler_view);
        messageEditText = findViewById(R.id.message_edit_text);
        Intent intent = getIntent();
        String username = intent.getStringExtra("USERNAME");
        messageList = new ArrayList<>();
        messageList.add(new Message("Left", "Welcome " + username + "!"));
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(messageList);
        chatRecyclerView.setAdapter(chatAdapter);
        aiServiceAgent = new AIServiceAgent();
        aiServiceAgent.clearHistory(this, new Callback<ServiceResponse>() {
            @Override
            public void onResponse(Call<ServiceResponse> call, Response<ServiceResponse> response) {
                Toast.makeText(ChatActivity.this, "Managed to clear history", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ServiceResponse> call, Throwable throwable) {
                Toast.makeText(ChatActivity.this, "Error clearing history", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void sendMessage(View view) {
        String messageContent = messageEditText.getText().toString();
        if (!messageContent.isEmpty()) {
            aiServiceAgent.sendNewMessage(this, messageContent, new Callback<ServiceResponse>() {
                @Override
                public void onResponse(Call<ServiceResponse> call, Response<ServiceResponse> response) {
                    if (response.isSuccessful()) {
                        ServiceResponse serviceResponse = response.body();
                        messageList.add(new Message("Right",messageContent));
                        messageList.add(new Message("Left",serviceResponse.getMessage()));
                        chatAdapter.notifyDataSetChanged();
                        chatRecyclerView.smoothScrollToPosition(messageList.size() - 1);
                        messageEditText.setText("");
                    } else {
                        Toast.makeText(ChatActivity.this, "Error sending message", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ServiceResponse> call, Throwable throwable) {
                    Toast.makeText(ChatActivity.this, "Error sending message", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(ChatActivity.this, "Please enter message", Toast.LENGTH_SHORT).show();
        }
    }
}