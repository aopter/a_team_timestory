package com.example.timesequence.ability.problem.adapter;

import com.example.timesequence.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.*;
import ohos.app.Context;

import java.util.List;

public class ProblemleftDynastyItemProvider extends BaseItemProvider {
    private AbilitySlice abilitySlice;
    private Context context;
    private List<String> dynastys;


    public ProblemleftDynastyItemProvider(AbilitySlice abilitySlice, Context context, List<String> dynastys ){
        this.abilitySlice = abilitySlice;
        this.context = context;
        this.dynastys = dynastys;
    }

    @Override
    public int getCount() {
        if (dynastys != null)
            return dynastys.size();
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (dynastys != null)
            return dynastys.get(i);
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
            component = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_item_problem_dynasty,null,false);
            Text  mTvTitle = (Text) component.findComponentById(ResourceTable.Id_dynasty);
            viewHolder = new ViewHolder();
            viewHolder.mTvTitle = mTvTitle;
            component.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) component.getTag();
        }
        String  dynasty = dynastys.get(i);
        viewHolder.mTvTitle.setText(dynasty);
        return component;
    }
    private class ViewHolder{
        private Text mTvTitle;//朝代

    }

}
