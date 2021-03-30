package com.example.timestory.ability.user.adapter;

import com.example.timestory.ResourceTable;
import com.example.timestory.Utils.HmOSImageLoader;
import com.example.timestory.constant.Constant;
import com.example.timestory.constant.ServiceConfig;
import com.example.timestory.entity.User;
import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.*;
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
    private Context context;
    private Image mRankImg;
    private Image mRankUserHeader;
    private Text mRankNum;
    private Text mRankUserName;
    private Text mRankUserSomething;
    private Text mRankingPoint;

    public UserRankingAdapter(List<User> users, AbilitySlice slice, Context context) {
        this.users = users;
        this.slice = slice;
        this.context = context;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Component getComponent(int i, Component component, ComponentContainer componentContainer) {
        if (component == null) {
            component = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_item_user_ranking, null, false);
            initView(component);
            setData(i);
        }
        return component;
    }

    private void setData(int position) {
        User user = users.get(position);
        //设置头像
        HmOSImageLoader.with(slice).load(ServiceConfig.SERVICE_ROOT + "/img/" + Constant.User.getUserHeader()).into(mRankUserHeader);
        //用户排名前三设置对应的奖杯图标
        switch (position) {
            case 0:
                mRankImg.setPixelMap(ResourceTable.Media_rank_f);
                break;
            case 1:
                mRankImg.setPixelMap(ResourceTable.Media_rank_s);
                break;
            case 2:
                mRankImg.setPixelMap(ResourceTable.Media_rank_t);
                break;
            default:
                mRankImg.setPixelMap(ResourceTable.Media_rank_e);
                break;
        }
        //排名数字
        mRankNum.setText(position);
        //用户昵称
        mRankUserName.setText(user.getUserNickname());
        //用户个性签名
        mRankUserSomething.setText(user.getUserSignature());
        //设置经验
        mRankingPoint.setText(user.getUserExperience() + "");
    }

    private void initView(Component component) {
        mRankImg = (Image) component.findComponentById(ResourceTable.Id_rank_img);
        mRankUserHeader = (Image) component.findComponentById(ResourceTable.Id_rank_user_header);
        mRankNum = (Text) component.findComponentById(ResourceTable.Id_rank_num);
        mRankUserName = (Text) component.findComponentById(ResourceTable.Id_rank_user_name);
        mRankUserSomething = (Text) component.findComponentById(ResourceTable.Id_rank_user_something);
        mRankingPoint = (Text) component.findComponentById(ResourceTable.Id_ranking_point);
    }
}