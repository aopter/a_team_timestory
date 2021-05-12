package com.example.timesequence.ability.problem.slice;


import com.example.timesequence.Utils.Util;
import com.example.timesequence.ResourceTable;
import com.example.timesequence.ability.problem.ProblemInfoAbility;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;

public class SelectProblemTypeAbilitySlice extends AbilitySlice {

    //    选择题
    private DirectionalLayout mTypeProblemXuan;
    //    连线题
    private DirectionalLayout mTypeProblemLian;
    //    排序题
    private DirectionalLayout mTypeProblemPai;
    //    混合题
    private DirectionalLayout mTypeProblemKuai;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_select_problem_type);
        //        关联
        mTypeProblemXuan = (DirectionalLayout) findComponentById(ResourceTable.Id_type_problem_xuan);
        mTypeProblemLian = (DirectionalLayout) findComponentById(ResourceTable.Id_type_problem_lian);
        mTypeProblemPai = (DirectionalLayout) findComponentById(ResourceTable.Id_type_problem_pai);
        mTypeProblemKuai = (DirectionalLayout) findComponentById(ResourceTable.Id_type_problem_kuai);
//    设置监听器
        setClickListener();
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }


    //    设置点击事件监听器 选择题目类型
    private void setClickListener() {
        MyListener myListener = new MyListener();
        mTypeProblemKuai.setClickedListener(myListener);
        mTypeProblemXuan.setClickedListener(myListener);
        mTypeProblemLian.setClickedListener(myListener);
        mTypeProblemPai.setClickedListener(myListener);


    }

    //    监听器类
    class MyListener implements Component.ClickedListener {

        @Override
        public void onClick(Component component) {
            switch (component.getId()) {
                case ResourceTable.Id_type_problem_xuan:
//                    跳转选择题  携带参数朝代
                    startAbility(Util.generatePageNavigationIntent(ProblemInfoAbility.class.getName()));
                    break;
                case ResourceTable.Id_type_problem_lian:
//                    跳转
                    startAbility(Util.generatePageNavigationIntent(ProblemInfoAbility.class.getName()));
                    break;
                case ResourceTable.Id_type_problem_pai:
//                    跳转
                    startAbility(Util.generatePageNavigationIntent(ProblemInfoAbility.class.getName()));
                    break;
                case ResourceTable.Id_type_problem_kuai:
                    System.out.println("选中了：题目模块");
//                    跳转
                    startAbility(Util.generatePageNavigationIntent(ProblemInfoAbility.class.getName()));
                    break;
            }
        }
    }
}
