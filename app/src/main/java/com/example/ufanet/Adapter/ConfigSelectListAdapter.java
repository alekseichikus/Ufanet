package com.example.ufanet.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ufanet.R;
import com.example.ufanet.configList.IConfigListView;
import com.example.ufanet.configSelect.IConfigSelectListView;
import com.example.ufanet.templates.TrimConfig;

import java.util.ArrayList;


public class ConfigSelectListAdapter extends RecyclerView.Adapter<ConfigSelectListAdapter.CustomViewHolder>{

    public ArrayList<TrimConfig> items;
    private IConfigSelectListView listView;

    public ConfigSelectListAdapter(IConfigSelectListView listView, ArrayList<TrimConfig> items) {
        super();
        this.items = items;
        this.listView = listView;
    }

    @NonNull
    @Override
    public ConfigSelectListAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConfigSelectListAdapter.CustomViewHolder(LayoutInflater.from(listView.getContext()).inflate(R.layout.row_config, parent, false));
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView fio;
        TextView key;
        View view;
        public CustomViewHolder(View view) {
            super(view);
            fio = (TextView) view.findViewById(R.id.fio);
            key = (TextView) view.findViewById(R.id.key);
            this.view = view;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ConfigSelectListAdapter.CustomViewHolder holder, int position) {
        holder.fio.setText(items.get(position).getNameConfig());
        //holder.key.setText(items.get(position).getKeyBytes());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.selectConfig(position);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}