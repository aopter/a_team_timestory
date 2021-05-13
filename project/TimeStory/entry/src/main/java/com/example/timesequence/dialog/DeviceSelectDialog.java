package com.example.timesequence.dialog;

import com.example.timesequence.ResourceTable;
import com.example.timesequence.Utils.CommentViewHolder;
import com.example.timesequence.ability.card.adapter.ListComponentAdapter;
import com.example.timesequence.entity.DeviceData;
import ohos.agp.components.*;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.app.Context;
import ohos.distributedschedule.interwork.DeviceInfo;
import ohos.distributedschedule.interwork.DeviceManager;

import java.util.ArrayList;
import java.util.List;

import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_CONTENT;
import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_PARENT;

public class DeviceSelectDialog extends CommonDialog {
    /**
     * DeviceSelectDialog在slice中的使用方法
     *
     DeviceSelectDialog dialog = new DeviceSelectDialog(this);
     dialog.setListener(deviceInfo -> {
     LogUtil.debug(TAG, deviceInfo.getDeviceName().toString());
     dialog.hide();
     });
     dialog.show();
     */

    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface OnclickListener {
        public void onYesClick(DeviceInfo deviceInfo);
    }

    private Context mContext;
    private OnclickListener mOnclickListener;

    public DeviceSelectDialog(Context context) {
        super(context);
        this.mContext = context;

    }

    public void setListener(OnclickListener listener) {
        mOnclickListener = listener;
    }


    private DeviceInfo mCheckedDevice;
    ListContainer mListContainer;
    List<DeviceData> mDeviceList = new ArrayList<>();
    ListComponentAdapter listComponentAdapter;

    @Override
    protected void onCreate() {
        super.onCreate();
        Component rootView = LayoutScatter.getInstance(mContext).parse(ResourceTable.Layout_dialog_layout_device, null, false);
        mListContainer = (ListContainer) rootView.findComponentById(ResourceTable.Id_list_container_device);
        Text operateYes = (Text) rootView.findComponentById(ResourceTable.Id_operate_yes);
        Text operateNo = (Text) rootView.findComponentById(ResourceTable.Id_operate_no);

        List<DeviceInfo> deviceInfoList = DeviceManager.getDeviceList(DeviceInfo.FLAG_GET_ONLINE_DEVICE);
        mDeviceList.clear();
        for (int i = 0; i < deviceInfoList.size(); i++) {
            mDeviceList.add(new DeviceData(false, deviceInfoList.get(i)));
        }
        if (deviceInfoList.size() > 0)
            mCheckedDevice = deviceInfoList.get(0);

        mListContainer.setItemProvider(listComponentAdapter = new ListComponentAdapter<DeviceData>(mContext, mDeviceList, ResourceTable.Layout_dialog_device_item) {
            @Override
            public void onBindViewHolder(CommentViewHolder commonViewHolder, DeviceData item, int position) {
                commonViewHolder.getTextView(ResourceTable.Id_item_desc).setText(item.getDeviceInfo().getDeviceName());
                switch (item.getDeviceInfo().getDeviceType()) {
                    case SMART_PHONE:
                        commonViewHolder.getImageView(ResourceTable.Id_item_type).setPixelMap(ResourceTable.Media_dv_phone);
                        break;
                    case SMART_PAD:
                        commonViewHolder.getImageView(ResourceTable.Id_item_type).setPixelMap(ResourceTable.Media_dv_pad);
                        break;
                    case SMART_WATCH:
                        commonViewHolder.getImageView(ResourceTable.Id_item_type).setPixelMap(ResourceTable.Media_dv_watch);
                        break;
                }
                commonViewHolder.getImageView(ResourceTable.Id_item_check).setPixelMap(item.isChecked() ? ResourceTable.Media_checked_point : ResourceTable.Media_uncheck_point);
            }

            @Override
            public void onItemClick(Component component, DeviceData item, int position) {
                super.onItemClick(component, item, position);
                for (int i = 0; i < mDeviceList.size(); i++) {
                    mDeviceList.get(i).setChecked(false);
                }
                mDeviceList.get(position).setChecked(true);
                listComponentAdapter.notifyDataChanged();
                mCheckedDevice = item.getDeviceInfo();
            }
        });


        operateYes.setClickedListener(component -> {
            if (mOnclickListener != null && mCheckedDevice != null)
                mOnclickListener.onYesClick(mCheckedDevice);
        });
        operateNo.setClickedListener(component -> {
            hide();
        });
        setSize(MATCH_PARENT, MATCH_CONTENT);
        setAlignment(LayoutAlignment.BOTTOM);
        setCornerRadius(10);
        setAutoClosable(true);
        setContentCustomComponent(rootView);
        setTransparent(true);
    }

}

