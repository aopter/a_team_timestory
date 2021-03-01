package com.example.timestory.slice;

import com.example.timestory.ResourceTable;
import com.example.timestory.ability.user.slice.LoginAbilitySlice;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Component;
import ohos.agp.components.Text;

public class MainAbilitySlice extends AbilitySlice {

    Text tvUser;
    Text tvDynasty;
    Text tvCard;
    Text tvProblem;

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
                    present(new LoginAbilitySlice(), new Intent());
                    break;
                case ResourceTable.Id_module_dynasty:
//                    跳转
                    Intent dynastyIntent = new Intent();
                    Operation dynastyOperation = new Intent.OperationBuilder()
                            .withDeviceId("")
                            .withBundleName("com.example.timestory")
                            .withAbilityName("com.example.timestory.ability.dynasty.HomePageAbility")
                            .build();
                    dynastyIntent.setOperation(dynastyOperation);
                    startAbility(dynastyIntent);
                    break;
                case ResourceTable.Id_module_card:
//                    跳转
                    break;
                case ResourceTable.Id_module_problem:
                    System.out.println("选中了：题目模块");
//                    跳转
                    break;

            }
        }
    }
}
