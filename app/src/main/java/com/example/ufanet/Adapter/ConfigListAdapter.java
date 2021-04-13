package com.example.ufanet.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ufanet.R;
import com.example.ufanet.configList.IConfigListView;
import com.example.ufanet.templates.TrimConfig;

import java.util.ArrayList;

public class ConfigListAdapter extends RecyclerView.Adapter<ConfigListAdapter.CustomViewHolder> {

    public ArrayList<TrimConfig> items;
    private IConfigListView iConfigListView;

    public ConfigListAdapter(IConfigListView iConfigListView, ArrayList<TrimConfig> items) {
        super();
        this.items = items;
        this.iConfigListView = iConfigListView;
    }

    @NonNull
    @Override
    public ConfigListAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConfigListAdapter.CustomViewHolder(LayoutInflater.from(iConfigListView.getContext()).inflate(R.layout.row_config, parent, false));
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView fioTV;
        TextView keyTV;
        View view;

        public CustomViewHolder(View view) {
            super(view);
            fioTV = (TextView) view.findViewById(R.id.fio);
            keyTV = (TextView) view.findViewById(R.id.key);
            this.view = view;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ConfigListAdapter.CustomViewHolder holder, int position) {
        holder.fioTV.setText(items.get(position).getNameConfig());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iConfigListView.startEditConfig(position);
            }
        });
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                iConfigListView.startControlConfigDialogFragment(position);
                return true;
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