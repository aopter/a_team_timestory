package com.example.timesequence.ability.user.adapter;

import com.example.timesequence.ResourceTable;
import com.example.timesequence.entity.HistoryDay;
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
public class HistoryTodayAdapter extends BaseItemProvider {
    private List<HistoryDay> historyDays;
    private Context context;
    private Text mHistoryTodayTitle;
    private Text mHistoryTodayTime;
    private Text mHistoryTodayBody;

    public HistoryTodayAdapter(List<HistoryDay> historyDays,  Context context) {
        this.historyDays = historyDays;
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
        ViewHolder viewHolder = null;
        if (component == null) {
            viewHolder = new ViewHolder();
            component = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_item_history_today, null, false);
            initView(component, viewHolder);
            component.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) component.getTag();
        }
        setData(i, viewHolder);
        return component;
    }

    private void setData(int position, ViewHolder viewHolder) {
        HistoryDay historyDay = historyDays.get(position);
        viewHolder.mHistoryTodayTitle.setText(historyDay.getTitle());
        viewHolder.mHistoryTodayTime.setText(historyDay.getLunar());
        viewHolder.mHistoryTodayBody.setText(historyDay.getDes());
    }

    private void initView(Component component, ViewHolder viewHolder) {
        mHistoryTodayTitle = (Text) component.findComponentById(ResourceTable.Id_history_today_title);
        viewHolder.mHistoryTodayTitle = mHistoryTodayTitle;
        mHistoryTodayTime = (Text) component.findComponentById(ResourceTable.Id_history_today_time);
        viewHolder.mHistoryTodayTime = mHistoryTodayTime;
        mHistoryTodayBody = (Text) component.findComponentById(ResourceTable.Id_history_today_body);
        viewHolder.mHistoryTodayBody = mHistoryTodayBody;
    }

    private static class ViewHolder {
        private Text mHistoryTodayTitle;
        private Text mHistoryTodayTime;
        private Text mHistoryTodayBody;
    }
}