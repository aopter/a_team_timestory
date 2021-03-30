package com.example.timestory.ability.user.adapter;

import com.example.timestory.ResourceTable;
import com.example.timestory.Utils.HmOSImageLoader;
import com.example.timestory.constant.ServiceConfig;
import com.example.timestory.entity.User;
import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.*;
import ohos.agp.render.render3d.ViewHolder;
import ohos.app.Context;

import java.util.List;

/**
 * @author PengHuAnZhi
 * @createTime 2021/3/30 10:55
 * @projectName TimeStory
 * @className UserRankingAdapter.java
 * @description TODO
 */
public class UserRankingAdapter extends BaseItemProvider {
    private List<User> users;
    private AbilitySlice slice;
    private Image mRankImg;
    private Image mRankUserHeader;
    private Text mRankNum;
    private Text mRankUserName;
    private Text mRankUserSomething;
    private Text mRankingPoint;

    public UserRankingAdapter(List<User> users, AbilitySlice slice) {
        this.users = users;
        this.slice = slice;
    }

    @Override
    public int getCount() {
        return users == null ? 0 : users.size();
    }

    @Override
    public Object getItem(int i) {
        if (users != null && i < users.size() && i >= 0) {
            return users.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Component getComponent(int i, Component component, ComponentContainer componentContainer) {
        ViewHolder viewHolder;
        if (component == null) {
            viewHolder = new ViewHolder();
            component = LayoutScatter.getInstance(slice).parse(ResourceTable.Layout_item_user_ranking, null, false);
            initView(component, viewHolder);
            component.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) component.getTag();
        }
        setData(i, viewHolder);
        return component;
    }

    private void setData(int position, ViewHolder viewHolder) {
        User user = users.get(position);
        //设置头像
        if (user.getUserHeader() != null) {
            HmOSImageLoader.with(slice).load(ServiceConfig.SERVICE_ROOT + "/img/" + user.getUserHeader()).into(viewHolder.mRankUserHeader);
        }
        //用户排名前三设置对应的奖杯图标
        switch (position) {
            case 0:
                viewHolder.mRankImg.setPixelMap(ResourceTable.Media_rank_f);
                break;
            case 1:
                viewHolder.mRankImg.setPixelMap(ResourceTable.Media_rank_s);
                break;
            case 2:
                viewHolder.mRankImg.setPixelMap(ResourceTable.Media_rank_t);
                break;
            default:
                viewHolder.mRankImg.setPixelMap(ResourceTable.Media_rank_e);
                break;
        }
        //排名数字
        viewHolder.mRankNum.setText(position + 1 + "");
        //用户昵称
        viewHolder.mRankUserName.setText(user.getUserNickname());
        //用户个性签名
        viewHolder.mRankUserSomething.setText(user.getUserSignature());
        //设置经验
        viewHolder.mRankingPoint.setText(user.getUserExperience() + "");
    }

    private void initView(Component component, ViewHolder viewHolder) {
        mRankImg = (Image) component.findComponentById(ResourceTable.Id_rank_img);
        viewHolder.mRankImg = mRankImg;
        mRankUserHeader = (Image) component.findComponentById(ResourceTable.Id_rank_user_header);
        viewHolder.mRankUserHeader = mRankUserHeader;
        mRankNum = (Text) component.findComponentById(ResourceTable.Id_rank_num);
        viewHolder.mRankNum = mRankNum;
        mRankUserName = (Text) component.findComponentById(ResourceTable.Id_rank_user_name);
        viewHolder.mRankUserName = mRankUserName;
        mRankUserSomething = (Text) component.findComponentById(ResourceTable.Id_rank_user_something);
        viewHolder.mRankUserSomething = mRankUserSomething;
        mRankingPoint = (Text) component.findComponentById(ResourceTable.Id_ranking_point);
        viewHolder.mRankingPoint = mRankingPoint;

    }

    private static class ViewHolder {
        private Image mRankImg;
        private Image mRankUserHeader;
        private Text mRankNum;
        private Text mRankUserName;
        private Text mRankUserSomething;
        private Text mRankingPoint;
    }
}