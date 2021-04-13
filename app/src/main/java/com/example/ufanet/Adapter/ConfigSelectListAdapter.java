package com.example.ufanet.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ufanet.R;
import com.example.ufanet.configSelect.IConfigSelectListView;
import com.example.ufanet.templates.TrimConfig;
import java.util.ArrayList;

public class ConfigSelectListAdapter extends RecyclerView.Adapter<ConfigSelectListAdapter.CustomViewHolder>{

    public ArrayList<TrimConfig> items;
    private IConfigSelectListView iConfigSelectListView;

    public ConfigSelectListAdapter(IConfigSelectListView iConfigSelectListView, ArrayList<TrimConfig> items) {
        super();
        this.items = items;
        this.iConfigSelectListView = iConfigSelectListView;
    }

    @NonNull
    @Override
    public ConfigSelectListAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConfigSelectListAdapter.CustomViewHolder(LayoutInflater.from(iConfigSelectListView.getContext()).inflate(R.layout.row_config, parent, false));
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
    public void onBindViewHolder(@NonNull ConfigSelectListAdapter.CustomViewHolder holder, int position) {
        holder.fioTV.setText(items.get(position).getNameConfig());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iConfigSelectListView.selectConfig(position);
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