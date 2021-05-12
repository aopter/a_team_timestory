package com.example.timesequence.slice;

import com.example.timesequence.Utils.Util;
import com.example.timesequence.ability.card.MyCardAbility;
import com.example.timesequence.ability.dynasty.HomePageAbility;
import com.example.timesequence.ability.user.LoginAbility;
import com.example.timesequence.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.Text;

public class MainAbilitySlice extends AbilitySlice {

    private Text tvUser;
    private Text tvDynasty;
    private Text tvCard;
    private Text tvProblem;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        findViews();
        setClickListener();
    }

    //    点击事件绑定监听
    private void setClickListener() {

        MyListener myListener = new MyListener();
        tvDynasty.setClickedListener(myListener);
        tvUser.setClickedListener(myListener);
        tvCard.setClickedListener(myListener);
        tvProblem.setClickedListener(myListener);
    }

    private void findViews() {
        tvUser = (Text) findComponentById(ResourceTable.Id_module_user);
        tvDynasty = (Text) findComponentById(ResourceTable.Id_module_dynasty);
        tvCard = (Text) findComponentById(ResourceTable.Id_module_card);
        tvProblem = (Text) findComponentById(ResourceTable.Id_module_problem);
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    class MyListener implements Component.ClickedListener {

        @Override
        public void onClick(Component component) {
            switch (component.getId()) {
                case ResourceTable.Id_module_user:
//                    跳转
                    startAbility(Util.generatePageNavigationIntent(LoginAbility.class.getName()));
                    break;
                case ResourceTable.Id_module_dynasty:
//                    跳转
                    startAbility(Util.generatePageNavigationIntent(HomePageAbility.class.getName()));
                    break;
                case ResourceTable.Id_module_card:
//                    跳转
                    startAbility(Util.generatePageNavigationIntent(MyCardAbility.class.getName()));
                    break;
                case ResourceTable.Id_module_problem:
                    System.out.println("选中了：题目模块");
//                    跳转
                    break;
            }
        }
    }
}