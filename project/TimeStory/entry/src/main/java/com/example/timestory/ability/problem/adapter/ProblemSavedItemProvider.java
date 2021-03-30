package com.example.timestory.ability.problem.adapter;

import com.example.timestory.ResourceTable;
import com.example.timestory.Utils.HmOSImageLoader;
import com.example.timestory.constant.Constant;
import com.example.timestory.constant.ServiceConfig;
import com.example.timestory.entity.Problem;
import com.example.timestory.entity.problem.ProblemSelect;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;
import ohos.app.Context;

import java.util.List;

public class ProblemSavedItemProvider extends BaseItemProvider {
    private AbilitySlice abilitySlice;
    private Context context;
    private List<Problem> problems;


    public ProblemSavedItemProvider(AbilitySlice abilitySlice, Context context, List<Problem> problems ){
        this.abilitySlice = abilitySlice;
        this.context = context;
        this.problems = problems;
    }

    @Override
    public int getCount() {
        if (problems != null)
            return problems.size();
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (problems != null)
            return problems.get(i);
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Component getComponent(int i, Component component, ComponentContainer componentContainer) {
        ViewHolder viewHolder = null;
        if(component == null){
            component = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_item_problem_info,null,false);
            Text  mTvTitle = (Text) component.findComponentById(ResourceTable.Id_tv_title);
            Image mIvOptionA = (Image) component.findComponentById(ResourceTable.Id_iv_optionA);
            Text  mTvOptionA = (Text) component.findComponentById(ResourceTable.Id_tv_optionA);
            Image mIvOptionB = (Image) component.findComponentById(ResourceTable.Id_iv_optionB);
            Text  mTvOptionB = (Text) component.findComponentById(ResourceTable.Id_tv_optionB);
            Image mIvOptionC = (Image) component.findComponentById(ResourceTable.Id_iv_optionC);
            Text  mTvOptionC = (Text) component.findComponentById(ResourceTable.Id_tv_optionC);
            Image mIvOptionD = (Image) component.findComponentById(ResourceTable.Id_iv_optionD);
            Text mTvOptionD = (Text) component.findComponentById(ResourceTable.Id_tv_optionD);
            DirectionalLayout container = (DirectionalLayout) component.findComponentById(ResourceTable.Id_container);
            viewHolder = new ViewHolder();
            viewHolder.mTvTitle = mTvTitle;
            viewHolder.mIvOptionA = mIvOptionA;
            viewHolder.mIvOptionB = mIvOptionB;
            viewHolder.mIvOptionC = mIvOptionC;
            viewHolder.mIvOptionD = mIvOptionD;
            viewHolder.mTvOptionA = mTvOptionA;
            viewHolder.mTvOptionB = mTvOptionB;
            viewHolder.mTvOptionC = mTvOptionC;
            viewHolder.mTvOptionD = mTvOptionD;
            viewHolder.container = container;
            component.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) component.getTag();
        }

        Problem problem = problems.get(i);
        String[] contents1 = problem.getProblemContent().split(Constant.DELIMITER);
        switch (problem.getProblemType()) {
            case 1://选
//                holder.linearLayouts.get(1).setVisibility(View.INVISIBLE);
//                holder.linearLayouts.get(2).setVisibility(View.INVISIBLE);
                ProblemSelect problemSelect = new ProblemSelect();
                problemSelect.setProblemId(problem.getProblemId());
                problemSelect.setDynastyId(problem.getDynastyId());
                problemSelect.setProblemType(1);
                problemSelect.setTitle(contents1[0]);
                problemSelect.setOptionA(contents1[1]);
                problemSelect.setOptionApic(contents1[2]);
                problemSelect.setOptionB(contents1[3]);
                problemSelect.setOptionBpic(contents1[4]);
                problemSelect.setOptionC(contents1[5]);
                problemSelect.setOptionCpic(contents1[6]);
                problemSelect.setOptionD(contents1[7]);
                problemSelect.setOptionDpic(contents1[8]);
                problemSelect.setProblemKey(problem.getProblemKey());
                problemSelect.setProblemDetails(problem.getProblemDetails());
//                Constant.userProblems.add(problemSelect);
//               展示数据
                //        显示
                viewHolder.mTvTitle.setText(problemSelect.getTitle());
                String url = ServiceConfig.SERVICE_ROOT + "/img/";

                HmOSImageLoader.with(abilitySlice).load(url + problemSelect.getOptionApic()).into(viewHolder.mIvOptionA);
                HmOSImageLoader.with(abilitySlice).load(url + problemSelect.getOptionBpic()).into(viewHolder.mIvOptionB);
                HmOSImageLoader.with(abilitySlice).load(url + problemSelect.getOptionCpic()).into(viewHolder.mIvOptionC);
                HmOSImageLoader.with(abilitySlice).load(url + problemSelect.getOptionDpic()).into(viewHolder.mIvOptionD);


                viewHolder.mTvOptionA.setText(problemSelect.getOptionA());
                viewHolder.mTvOptionB.setText(problemSelect.getOptionB());
                viewHolder.mTvOptionC.setText(problemSelect.getOptionC());
                viewHolder.mTvOptionD.setText(problemSelect.getOptionD());
//                点击liner 跳转activity 携带数据
                viewHolder.container.setClickedListener(new Component.ClickedListener() {
                    @Override
                    public void onClick(Component component) {
                        Intent intent = new Intent();
                        intent.setParam("type", "xuan");
                        intent.setParam("dynastyId", problemSelect.getDynastyId());
                        intent.setParam("problem", problemSelect);
                        intent.setParam("position", i);
                        intent.setParam("before", "info");
                        intent.setFlags(Intent.FLAG_ABILITY_NEW_MISSION);
                        Operation operation = new Intent.OperationBuilder()
                                .withDeviceId("")//目标设备 ""表示此设备
                                .withBundleName("com.example.timestory")
                                .withAbilityName("com.example.timestory.ability.problem.ProblemInfoAbility")
                                .build();
                        intent.setOperation(operation);
                        abilitySlice.getAbility().startAbility(intent);
                    }
                });
                break;
        }

        return component;
    }
    private class ViewHolder{
        private Text mTvTitle;//标题
        private Image mIvOptionA;//选项1图片
        private Text mTvOptionA;//选项一 文字
        private Image mIvOptionB;
        private Text mTvOptionB;
        private Image mIvOptionC;
        private Text mTvOptionC;
        private Image mIvOptionD;
        private Text mTvOptionD;
        private DirectionalLayout container;
    }

}
