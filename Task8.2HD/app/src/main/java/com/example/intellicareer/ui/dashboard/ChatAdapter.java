package com.example.intellicareer.ui.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.intellicareer.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<Message> messageList;

    public ChatAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.message_row, parent, false);
        return new ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        private ImageView leftAvatar;
        private TextView leftMessageContent;
        private ImageView rightAvatar;
        private TextView rightMessageContent;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            leftAvatar = itemView.findViewById(R.id.left_avatar_image_view);
            rightAvatar = itemView.findViewById(R.id.right_avatar_image_view);
            leftMessageContent = itemView.findViewById(R.id.left_message_text_view);
            rightMessageContent = itemView.findViewById(R.id.right_message_text_view);
        }

        public void bind(Message message) {
            if (message.getDirection() == "Left") {
                rightAvatar.setVisibility(View.GONE);
                rightMessageContent.setVisibility(View.GONE);
                leftAvatar.setVisibility(View.VISIBLE);
                leftMessageContent.setVisibility(View.VISIBLE);
                leftMessageContent.setText(message.getContent());
            } else {
                rightAvatar.setVisibility(View.VISIBLE);
                rightMessageContent.setVisibility(View.VISIBLE);
                leftAvatar.setVisibility(View.GONE);
                leftMessageContent.setVisibility(View.GONE);
                rightMessageContent.setText(message.getContent());
            }
        }
    }
}
