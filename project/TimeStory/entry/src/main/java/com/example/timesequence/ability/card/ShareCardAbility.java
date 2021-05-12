package com.example.timesequence.ability.card;
import com.example.timesequence.ability.card.slice.ShareCardAbilitySlice;
import com.example.timesequence.entity.card.Icon;
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
