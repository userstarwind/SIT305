package com.example.task91p;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BoardActivity extends AppCompatActivity {

    private List<Advert> advertList;
    private RecyclerView advertRecyclerView;
    private TextView searchAdvertTextView;
    private List<Advert> filteredList;
    private AdvertAdapter advertAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        MyApp myApp = (MyApp) getApplication();
        advertList = myApp.getDb().getAllAdverts();
        filteredList = new ArrayList<>();
        filteredList.addAll(advertList);
        advertRecyclerView = findViewById(R.id.advert_recycler_view);
        searchAdvertTextView = findViewById(R.id.search_advert_text_view);
        Toolbar searchAdvertToolbar = findViewById(R.id.search_advert_toolbar);
        setSupportActionBar(searchAdvertToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        searchAdvertToolbar.setNavigationOnClickListener(v -> finish());
        advertRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        advertAdapter=new AdvertAdapter(filteredList, new AdvertAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Advert advert) {
                Intent intent = new Intent(BoardActivity.this, AdvertDetailsActivity.class);
                intent.putExtra("ADVERT_ID", advert.getId());
                startActivity(intent);
                finish();
            }
        });
        advertRecyclerView.setAdapter(advertAdapter);

        setupSearchFilter();

    }

    private void setupSearchFilter() {
        searchAdvertTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() > 0) {
                    filteredList.clear();
                    for (Advert advert : advertList) {
                        if (advert.getName().toLowerCase().contains(s.toString().toLowerCase())) {
                            filteredList.add(advert);
                        }
                    }
                } else {
                    filteredList.clear();
                    filteredList.addAll(advertList);
                }
                advertAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    filteredList.clear();
                    for (Advert advert : advertList) {
                        if (advert.getName().toLowerCase().contains(s.toString().toLowerCase())) {
                            filteredList.add(advert);
                        }
                    }
                } else {
                    filteredList.clear();
                    filteredList.addAll(advertList);
                }
                advertAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    filteredList.clear();
                    for (Advert advert : advertList) {
                        if (advert.getName().toLowerCase().contains(s.toString().toLowerCase())) {
                            filteredList.add(advert);
                        }
                    }
                } else {
                    filteredList.clear();
                    filteredList.addAll(advertList);
                }
                advertAdapter.notifyDataSetChanged();
            }
        });
    }



}