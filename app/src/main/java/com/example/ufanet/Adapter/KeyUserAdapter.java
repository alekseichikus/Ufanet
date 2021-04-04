package com.example.ufanet.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;



public class KeyUserAdapter  {

    /*public Context context;
    public ArrayList<UserShort> employeeArrayList;
    SharedPreferences mSettings;
    UsersCheckListActivity act;
    MemoryOperation memoryOperation;
    CardView resumeButton;

    public KeyUserAdapter(Context context, ArrayList<UserShort> employeeArrayList) {
        super();
        this.context = context;
        this.employeeArrayList = employeeArrayList;
        this.act = act;
        this.resumeButton = resumeButton;

        mSettings = PreferenceManager.getDefaultSharedPreferences(context);
        memoryOperation = new MemoryOperation(context);
    }

    @NonNull
    @Override
    public UsersCheckListAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UsersCheckListAdapter.CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.row_check_user, parent, false));
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView fio;
        TextView date;
        CardView funcButton;
        RelativeLayout checkButton;
        private CardView selectButton2;
        private CardView selectState;
        public CustomViewHolder(View view) {
            super(view);
            photo = view.findViewById(R.id.photo);
            fio = (TextView) view.findViewById(R.id.fio);
            date = (TextView) view.findViewById(R.id.date);
            funcButton = view.findViewById(R.id.func_button);
            checkButton = view.findViewById(R.id.select_button);
            selectButton2 = view.findViewById(R.id.select_button_2);
            selectState = view.findViewById(R.id.select_state);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull UsersCheckListAdapter.CustomViewHolder holder, int position) {
        Picasso.get().load(employeeArrayList.get(position).getPhoto()).into(holder.photo);
        holder.fio.setText(employeeArrayList.get(position).getLastName()+ " " + employeeArrayList.get(position).getFirstName());
        holder.date.setText(employeeArrayList.get(position).getDate());
        holder.checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(employeeArrayList.get(position).getCheck()){
                    holder.selectState.setCardBackgroundColor(Color.WHITE);
                    holder.selectButton2.setCardBackgroundColor(0xffe1e2e3);
                    employeeArrayList.get(position).setCheck(false);
                }
                else{
                    holder.selectState.setCardBackgroundColor(0xff5c9ce6);
                    holder.selectButton2.setCardBackgroundColor(0xff5c9ce6);
                    employeeArrayList.get(position).setCheck(true);
                }

                Boolean isCheck = false;
                for(UserShort note : employeeArrayList){
                    if(note.getCheck()){
                        isCheck = true;
                        break;
                    }
                }

                if(isCheck){
                    resumeButton.setVisibility(View.VISIBLE);
                }
                else{
                    resumeButton.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return employeeArrayList.size();
    }*/
}