package com.example.ufanet.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ufanet.R;
import com.example.ufanet.keyss.IKeyListView;
import com.example.ufanet.settings.IKey;

import java.util.ArrayList;


public class KeyListAdapter extends RecyclerView.Adapter<KeyListAdapter.CustomViewHolder>{

    public ArrayList<IKey> items;
    private IKeyListView keyListView;

    public KeyListAdapter(IKeyListView keyListView, ArrayList<IKey> items) {
        super();
        this.items = items;
        this.keyListView = keyListView;
    }

    @NonNull
    @Override
    public KeyListAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new KeyListAdapter.CustomViewHolder(LayoutInflater.from(keyListView.getContext()).inflate(R.layout.row_key, parent, false));
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
    public void onBindViewHolder(@NonNull KeyListAdapter.CustomViewHolder holder, int position) {
        holder.fio.setText(items.get(position).getFio());
        holder.key.setText(items.get(position).getKeyBytes());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyListView.startEditKey(position);
            }
        });
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                keyListView.startControlKeyDialogFragment(position);
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