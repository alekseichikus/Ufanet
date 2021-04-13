package com.example.ufanet.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ufanet.R;
import com.example.ufanet.edit.ILoadConfigView;
import com.example.ufanet.templates.ConfigSelect;
import java.util.ArrayList;

public class ConfigSelectAdapter extends RecyclerView.Adapter<ConfigSelectAdapter.CustomViewHolder>{

    public ArrayList<ConfigSelect> items;
    private ILoadConfigView iEditView;

    public ConfigSelectAdapter(ILoadConfigView iEditView, ArrayList<ConfigSelect> items) {
        super();
        this.items = items;
        this.iEditView = iEditView;
    }

    @NonNull
    @Override
    public ConfigSelectAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConfigSelectAdapter.CustomViewHolder(LayoutInflater.from(iEditView.getContext()).inflate(R.layout.row_config_select, parent, false));
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView nameTV;
        TextView descriptionTV;
        View view;
        public CustomViewHolder(View view) {
            super(view);
            nameTV = (TextView) view.findViewById(R.id.tv_name);
            descriptionTV = (TextView) view.findViewById(R.id.tv_description);
            this.view = view;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ConfigSelectAdapter.CustomViewHolder holder, int position) {
        holder.nameTV.setText(items.get(position).getNameUser());
        holder.descriptionTV.setText(items.get(position).getNameConfig());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iEditView.startSelectConfigActivity(position);
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