package com.example.task91p;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdvertAdapter extends RecyclerView.Adapter<AdvertAdapter.AdvertViewHolder> {

    private List<Advert> advertList;

    private onItemClickListener listener;

    public interface onItemClickListener {
        void onItemClick(Advert advert);
    }

    public AdvertAdapter(List<Advert> advertList, onItemClickListener listener) {
        this.advertList = advertList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdvertAdapter.AdvertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.advert_row, parent, false);
        return new AdvertViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdvertAdapter.AdvertViewHolder holder, int position) {
        Advert advert = advertList.get(position);
        holder.bind(advert, listener);
    }

    @Override
    public int getItemCount() {
        return advertList.size();
    }

    public class AdvertViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView date;
        private TextView type;

        public AdvertViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.advert_row_name_text_view);
            date = itemView.findViewById(R.id.advert_row_date_text_view);
            type = itemView.findViewById(R.id.advert_row_type_text_view);
        }

        public void bind(Advert advert, onItemClickListener listener) {
            name.setText(advert.getName());
            date.setText("Date: " + advert.getDate());
            type.setText("Type: " + advert.getType());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(advert);
                }
            });
        }
    }
}
