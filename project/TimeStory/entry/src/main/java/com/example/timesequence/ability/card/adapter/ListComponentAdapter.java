package com.example.timesequence.ability.card.adapter;

import com.example.timesequence.Utils.CommentViewHolder;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.RecycleItemProvider;
import ohos.app.Context;

import java.util.List;

public abstract class ListComponentAdapter<T> extends RecycleItemProvider {
    private Context mContext;
    private List<T> mListBean;
    private int mXmlId;

    public abstract void onBindViewHolder(CommentViewHolder commonViewHolder, T item, int position);

    public ListComponentAdapter(Context context, List<T> list, int xmlId) {
        this.mContext = context;
        this.mListBean = list;
        this.mXmlId = xmlId;
        layoutScatter = LayoutScatter.getInstance(mContext);
    }

    @Override
    public int getCount() {
        return mListBean.size();
    }

    @Override
    public T getItem(int i) {
        return mListBean.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    LayoutScatter layoutScatter;

    @Override
    public Component getComponent(int i, Component component, ComponentContainer componentContainer) {
        CommentViewHolder commentViewHolder = CommentViewHolder.getCommentViewHolder(mContext, component, mXmlId);
        T t = mListBean.get(i);

        onBindViewHolder(commentViewHolder, t, i);
        commentViewHolder.convertView.setClickedListener(component1 -> onItemClick(component,t,i));
        return commentViewHolder.convertView;
    }


    public void  onItemClick(Component component, T item, int position){

    }
}
