package com.example.ufanet.configList;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.DialogFragment;
import com.example.ufanet.R;
import com.example.ufanet.editConfig.EditConfigActivity;
import com.example.ufanet.keyss.KeyListActivity;
import com.example.ufanet.utils.MemoryOperation;

public class ControlConfigListDialogFragment extends DialogFragment {

    Button deleteConfigButton;
    LinearLayout editConfigButton;

    MemoryOperation memoryOperation;

    Integer id_config;

    public ControlConfigListDialogFragment(Integer id_config){
        this.id_config = id_config;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_fragment_control_config_list, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        initUI(rootView);
        setListeners();

        return rootView;
    }

    public void initUI(View view){
        deleteConfigButton = view.findViewById(R.id.bt_delete_config);
        editConfigButton = view.findViewById(R.id.ll_edit_button);
        memoryOperation = new MemoryOperation(getContext());
    }

    public void setListeners(){
        deleteConfigButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                memoryOperation.deleteConfig(id_config);
                dismiss();
                ConfigListActivity keyListActivity = (ConfigListActivity) getActivity();
                keyListActivity.onResume();
            }
        });

        editConfigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditConfigActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("id_config", id_config);
                startActivityForResult(intent, 0);
                dismiss();
            }
        });
    }
}
