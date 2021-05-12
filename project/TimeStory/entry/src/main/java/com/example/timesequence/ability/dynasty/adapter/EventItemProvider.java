package com.example.timesequence.ability.dynasty.adapter;

import com.example.timesequence.Utils.HmOSImageLoader;
import com.example.timesequence.constant.Constant;
import com.example.timesequence.constant.ServiceConfig;
import com.example.timesequence.entity.Incident;
import com.example.timesequence.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;

import java.util.List;

public class EventItemProvider extends BaseItemProvider {
    private AbilitySlice abilitySlice;
    private Context context;
    private List<Incident> incidentList;
    private int dynastyId;

    public EventItemProvider(AbilitySlice abilitySlice, Context context, List<Incident> incidentList, int dynastyId) {
        this.abilitySlice = abilitySlice;
        this.context = context;
        this.incidentList = incidentList;
        this.dynastyId = dynastyId;
    }

    @Override
    public int getCount() {
        if (incidentList != null)
            return incidentList.size();
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (incidentList != null)
            return incidentList.get(i);
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
            component = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_event_item_layout,null,false);
            Text eventName = (Text) component.findComponentById(ResourceTable.Id_event_name);
            Image evnetImg = (Image) component.findComponentById(ResourceTable.Id_event_img);
            viewHolder = new ViewHolder();
            viewHolder.eventName = eventName;
            viewHolder.eventImg = evnetImg;
            component.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) component.getTag();
        }

        if(isUnlock(incidentList.get(i).getIncidentId())){
            //放入深颜色
//            viewHolder.eventName.setTextColor(new Color(595959));
        }else{
            //放入浅颜色
//            viewHolder.eventName.setTextColor(new Color(500000));

            ToastDialog toastDialog = new ToastDialog(context);
            toastDialog.setText("事件未完成");
            toastDialog.show();
        }

        //放入名称
        viewHolder.eventName.setText(incidentList.get(i).getIncidentName());
        viewHolder.eventName.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                //跳转到指定的事件详情页面
                Intent intent = new Intent();
                Operation operation = new Intent.OperationBuilder()
                        .withDeviceId("")
                        .withBundleName("com.example.timesequence")
                        .withAbilityName("com.example.timesequence.ability.dynasty.DetailsEventIntroduceAbility")
                        .build();
                intent.setParam("dynastyId",dynastyId);
                intent.setParam("eventId",incidentList.get(i).getIncidentId());
                intent.setOperation(operation);
                context.startAbility(intent,0);
            }
        });


        //获取网络图片放入eventImg
        String[] urls = incidentList.get(i).getIncidentPicture().split(Constant.DELIMITER);
        String url = urls[urls.length - 1];
        HmOSImageLoader.with(abilitySlice).load(ServiceConfig.SERVICE_ROOT+"/img/"+url).into(viewHolder.eventImg);

        return component;
    }

    private class ViewHolder{
        public Text eventName;
        public Image eventImg;
    }

    //判断事件是否解锁
    private boolean isUnlock(int incidentId){
        for(int i = 0;i < Constant.UnlockDynastyIncident.size();i++){
            if(Constant.UnlockDynastyIncident.get(i).getIncidentId().intValue() == incidentId){
                return true;
            }
        }
        return false;
    }
}
