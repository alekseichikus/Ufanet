package com.example.ufanet.keyss;

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
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.ufanet.R;
import com.example.ufanet.editKey.EditKeyActivity;
import com.example.ufanet.home.addKey.IAddKeyView;
import com.example.ufanet.home.addKey.presenter.AddKeyPresenter;
import com.example.ufanet.home.addKey.presenter.IAddKeyPresenter;
import com.example.ufanet.templates.TrimConfig;
import com.example.ufanet.utils.MemoryOperation;

public class ControKeyListDialogFragment extends DialogFragment implements IAddKeyView {

    Button deleteKeyButton;
    LinearLayout editKeyButton;
    MemoryOperation memoryOperation;
    Integer id_key;

    IAddKeyPresenter addKeyPresenter;

    public ControKeyListDialogFragment(Integer id_key){
        this.id_key = id_key;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_fragment_control_key_list, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        initUI(rootView);
        setListeners();

        addKeyPresenter = new AddKeyPresenter(this);

        return rootView;
    }

    public void initUI(View view){
        deleteKeyButton = view.findViewById(R.id.bt_delete_config);
        editKeyButton = view.findViewById(R.id.ll_edit_button);
        memoryOperation = new MemoryOperation(getContext());
    }

    public void setListeners(){
        deleteKeyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                memoryOperation.deleteKey(id_key);
                Toast.makeText(getContext(), "Ключ удалён", Toast.LENGTH_SHORT).show();
                dismiss();
                addKeyPresenter.requestEditConfig();
                KeyListActivity keyListActivity = (KeyListActivity) getActivity();
                keyListActivity.onResume();
            }
        });

        editKeyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditKeyActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("id_key", id_key);
                startActivity(intent);
                dismiss();
            }
        });
    }

    @Override
    public MemoryOperation getMemoryOperation() {
        return memoryOperation;
    }

    @Override
    public void onResponse(String string) {
        Toast.makeText(getContext(), string, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void closeView() {

    }

    @Override
    public String getFio() {
        return "a";
    }

    @Override
    public String getKey() {
        return "a";
    }

    @Override
    public void onResponseFailure(Throwable throwable) {

    }
}
