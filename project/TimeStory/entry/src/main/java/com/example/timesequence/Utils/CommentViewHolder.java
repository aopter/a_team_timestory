package com.example.timesequence.Utils;

import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.app.Context;

import java.util.HashMap;
import java.util.Map;

public class CommentViewHolder {
    //使用单例模式避免多个静态类
    public static CommentViewHolder getCommentViewHolder(Context context, Component convertView, int resource) {

        if (convertView == null) {

            convertView = LayoutScatter.getInstance(context).parse(resource, null, false);
            return new CommentViewHolder(convertView);
        } else {

            return (CommentViewHolder) convertView.getTag();
        }
    }

    public Component convertView;

    // 对成员变量赋值，并把当前CommentViewHolder用tag存起来以便复用
    public CommentViewHolder(Component convertView) {

        this.convertView = convertView;
        convertView.setTag(this);
    }

    private Map<Integer, Component> mViews = new HashMap<>();

    //根据xml的控件的资源id从集合里面取出对应控件
    public <T extends Component> T getView(int resId) {

        Component view = mViews.get(resId);
        if (view == null) {

            view = convertView.findComponentById(resId);
            mViews.put(resId, view);
        }
        return (T) view;
    }

    //根据泛型，指定view的类型
    public <T extends Component> T getView(int resId, Class<T> type) {

        Component view = mViews.get(resId);
        if (view == null) {

            view = convertView.findComponentById(resId);
            mViews.put(resId, view);
        }
        return (T) view;
    }

    //2个常用的view，简单处理一下对外提供出来
    public Text getTextView(int resId) {

        return getView(resId, Text.class);
    }

    public Image getImageView(int resId) {

        return getView(resId, Image.class);
    }


}
