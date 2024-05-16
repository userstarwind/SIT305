package com.example.intellicareer.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.intellicareer.R;

import java.util.List;

public class DashMenuAdapter extends BaseAdapter {
    private Context context;
    private List<MenuItem> menuItemList;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(MenuItem menuItem);
    }

    public DashMenuAdapter(Context context, List<MenuItem> menuItemList, OnItemClickListener listener) {
        this.context = context;
        this.menuItemList = menuItemList;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return menuItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return menuItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_menu_item, parent, false);
        }
        ImageView imageView = convertView.findViewById(R.id.grid_image_view);
        TextView textView = convertView.findViewById(R.id.grid_text_view);
        imageView.setImageResource(menuItemList.get(position).getImageRes());
        textView.setText(menuItemList.get(position).getText());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(menuItemList.get(position));
            }
        });
        return convertView;
    }
}
