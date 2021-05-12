package com.example.timesequence.Utils;

import com.example.timesequence.ResourceTable;
import ohos.agp.components.*;
import ohos.app.Context;
import ohos.utils.net.Uri;

import java.util.List;

public class MainProvider extends BaseItemProvider {
    private List<Uri> mUris;
    private List<String> mPaths;
    private final Context mContext;

    public MainProvider(Context context,List<Uri> mUris, List<String> mPaths){
        mContext = context;
        this.mPaths = mPaths;
        this.mUris = mUris;
    }

    public void setData(List<Uri> uris, List<String> paths) {
        mUris = uris;
        mPaths = paths;
        notifyDataChanged();
    }

    @Override
    public int getCount() {
        return mUris == null ? 0 : mUris.size();
    }

    @Override
    public Object getItem(int i) {
        return mUris == null ? 0 : mUris.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mUris == null ? 0 : mUris.get(i).hashCode();
    }

    @Override
    public Component getComponent(int position, Component component, ComponentContainer componentContainer) {
        Component v = LayoutScatter.getInstance(mContext).parse(ResourceTable.Layout_uri_item,componentContainer,false);

        Text mUri = (Text) v.findComponentById(ResourceTable.Id_uri);
        Text mPath = (Text) v.findComponentById(ResourceTable.Id_path);
        mUri.setText(mUris.get(position).toString());
        mPath.setText(mPaths.get(position));

        mUri.setAlpha(position % 2 == 0 ? 1.0f : 0.54f);
        mPath.setAlpha(position % 2 == 0 ? 1.0f : 0.54f);
        return v;
    }
}
