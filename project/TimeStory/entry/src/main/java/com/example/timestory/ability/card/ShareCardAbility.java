package com.example.timestory.ability.card;
import com.example.timestory.ability.card.slice.ShareCardAbilitySlice;
import com.example.timestory.entity.card.Icon;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

import java.util.List;

public class ShareCardAbility extends Ability {
    private List<Icon> icons;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(ShareCardAbilitySlice.class.getName());

    }
}
