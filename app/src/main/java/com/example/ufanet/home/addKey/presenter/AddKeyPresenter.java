package com.example.ufanet.home.addKey.presenter;

import com.example.ufanet.edit.IEditView;
import com.example.ufanet.edit.model.EditModel;
import com.example.ufanet.edit.model.IEditModel;
import com.example.ufanet.edit.presenter.IEditPresenter;
import com.example.ufanet.home.addKey.IAddKeyView;
import com.example.ufanet.home.addKey.model.AddKeyModel;
import com.example.ufanet.home.addKey.model.IAddKeyModel;
import com.example.ufanet.settings.IKey;

import java.util.ArrayList;

public class AddKeyPresenter implements IAddKeyPresenter, AddKeyModel.OnFinishedListener {

    private IAddKeyModel model;
    private IAddKeyView view;

    public AddKeyPresenter(IAddKeyView view){
        this.view = view;
        model = new AddKeyModel();
    }

    @Override
    public void onFinished(String keys) {
        if (view != null) {
            view.onResponse("Конфиг перезаписан, перезагружаюсь");
            view.closeView();
        }
    }

    int boolToInt(Boolean b) {
        return b.compareTo(false);
    }

    private boolean intToBoolean(int input) {
        if((input==0)||(input==1)) {
            return input!=0;
        }
        return true;
    }

    @Override
    public void onFailure(Throwable t) {
        if (view != null) {
            view.onResponseFailure(t);
        }
    }

    Boolean isFioValid(String text){
        if(text.isEmpty()){
            return false;
        }
        return true;
    }

    Boolean isKeyValid(String text){
        if(text.isEmpty()){
            return false;
        }
        return true;
    }

    @Override
    public void onDestroy() {
        this.view = null;
    }

    @Override
    public void requestEditConfig() {
        if (view != null) {
            if(isFioValid(view.getFio())){
                if(isKeyValid(view.getKey())){
                    String keys_for_request = "";
                    ArrayList<IKey> keys = view.getMemoryOperation().getKeyList();
                    for (int i = 0; i < keys.size(); i++) {
                        keys_for_request += view.getMemoryOperation().getKeyDataKey(i) + "," + view.getMemoryOperation().getKeyDataFIO(i) + "," + view.getMemoryOperation().getKeyDataType(i) + "\n";
                    }
                    model.sendKeys(this, keys_for_request);
                }
                else{
                    view.onResponse("Ключ не должен быть пустым");
                }
            }
            else{
                view.onResponse("ФИО не должно быть пустым");
            }
        }
    }
}
