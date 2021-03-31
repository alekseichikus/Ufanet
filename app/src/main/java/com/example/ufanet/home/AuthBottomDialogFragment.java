package com.example.ufanet.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.ufanet.R;
import com.example.ufanet.edit.EditActivity;
import com.example.ufanet.utils.MemoryOperation;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AuthBottomDialogFragment extends BottomSheetDialogFragment {

    MemoryOperation memoryOperation;
    CardView saveButtonCV;
    EditText loginUserET;
    EditText passwordUserET;
    View view;
    public static final String APP_PREFERENCES_LOGIN_USER = "login_user";
    public static final String APP_PREFERENCES_PASSWORD_USER = "password_user";

    public AuthBottomDialogFragment(){
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.dialog_fragment_auth, container,
                false);

        initUI();
        setListeners();

        memoryOperation = new MemoryOperation(getContext());

        loginUserET.setText(memoryOperation.getLoginUser());
        passwordUserET.setText(memoryOperation.getPasswordUser());

        return view;
    }

    void initUI(){
        saveButtonCV = view.findViewById(R.id.cv_save_button);
        loginUserET = view.findViewById(R.id.et_login);
        passwordUserET = view.findViewById(R.id.et_password);
    }
    void setListeners(){
        saveButtonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEmptyString(loginUserET.getText().toString())){
                    if(!isEmptyString(passwordUserET.getText().toString())){
                        memoryOperation.setLoginUser(loginUserET.getText().toString());
                        memoryOperation.setPasswordUser(passwordUserET.getText().toString());

                        Intent intent = new Intent(getContext(), EditActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getContext(), "Введите пароль", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getContext(), "Введите логин", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    Boolean isEmptyString(String text){
        if(text.isEmpty())
            return true;
        return false;
    }
}