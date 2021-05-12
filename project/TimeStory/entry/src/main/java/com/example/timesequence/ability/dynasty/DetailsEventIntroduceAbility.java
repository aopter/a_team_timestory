package com.example.timesequence.ability.dynasty;

import com.example.timesequence.ability.dynasty.slice.DetailsEventIntroduceSlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.IAbilityContinuation;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;

import java.util.ArrayList;
import java.util.List;

public class DetailsEventIntroduceAbility extends Ability implements IAbilityContinuation {

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(DetailsEventIntroduceSlice.class.getName());
        requestPermission();
    }

    private void requestPermission() {
        String[] permission = {
                "ohos.permission.DISTRIBUTED_DATASYNC",
                "ohos.permission.GET_DISTRIBUTED_DEVICE_INFO"};
        List<String> applyPermissions = new ArrayList<>();
        for (String element : permission) {
            if (verifySelfPermission(element) != 0) {
                if (canRequestPermission(element)) {
                    applyPermissions.add(element);
                } else {
                }
            } else {
            }
        }
        requestPermissionsFromUser(applyPermissions.toArray(new String[0]), 0);
    }

    @Override
    public boolean onStartContinuation() {
        return true;
    }

    @Override
    public boolean onSaveData(IntentParams intentParams) {
        return true;
    }

    @Override
    public boolean onRestoreData(IntentParams intentParams) {
        return true;
    }

    @Override
    public void onCompleteContinuation(int i) {

    }
}
