package com.example.timestory.ability.user.adapter;

import com.example.timestory.ResourceTable;
import com.example.timestory.entity.HistoryDay;
import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.*;
import ohos.app.Context;

import java.util.List;

/**
 * @author PengHuAnZhi
 * @createTime 2021/3/30 10:55
 * @projectName TimeStory
 * @className HistoryTodayAdapter.java
 * @description TODO
 */
public class HistoryTodayAdapter extends RecycleItemProvider {
    private List<HistoryDay> historyDays;
    private Context context;
    private AbilitySlice slice;
    private Text mHistoryTodayTitle;
    private Text mHistoryTodayTime;
    private Text mHistoryTodayBody;

    public HistoryTodayAdapter(List<HistoryDay> historyDays, AbilitySlice slice, Context context) {
        this.historyDays = historyDays;
        this.slice = slice;
        this.context = context;
    }

    @Override
    public int getCount() {
        return historyDays.size();
    }

    @Override
    public Object getItem(int i) {
        return historyDays.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Component getComponent(int i, Component component, ComponentContainer componentContainer) {
        if (component == null) {
            component = LayoutScatter.getInstance(slice).parse(ResourceTable.Layout_item_history_today, null, false);
            initView(component);
            setData(i);
        }
        return component;
    }

    private void setData(int position) {
        HistoryDay historyDay = historyDays.get(position);
        mHistoryTodayTitle.setText(historyDay.getTitle());
        mHistoryTodayTime.setText(historyDay.getLunar());
        mHistoryTodayBody.setText(historyDay.getDes());
    }

    private void initView(Component component) {
        mHistoryTodayTitle = (Text) component.findComponentById(ResourceTable.Id_history_today_title);
        mHistoryTodayTime = (Text) component.findComponentById(ResourceTable.Id_history_today_time);
        mHistoryTodayBody = (Text) component.findComponentById(ResourceTable.Id_history_today_body);
    }
}