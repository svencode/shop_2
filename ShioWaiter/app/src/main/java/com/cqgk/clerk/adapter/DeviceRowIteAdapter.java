package com.cqgk.clerk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cqgk.clerk.R;
import com.cqgk.clerk.bean.normal.DeviceItemBean;
import com.cqgk.clerk.bean.normal.DeviceItemBean;
import com.cqgk.clerk.config.Key;
import com.cqgk.clerk.helper.ImageHelper;
import com.cqgk.clerk.helper.PreferencesHelper;
import com.cqgk.clerk.http.HttpCallBack;
import com.cqgk.clerk.http.RequestUtils;
import com.cqgk.clerk.utils.AppUtil;
import com.cqgk.clerk.utils.ViewHolderUtil;
import com.cqgk.clerk.view.CommonDialogView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class DeviceRowIteAdapter extends BaseAdapter {

    private List<DeviceItemBean> valueList;
    private Context context;
    private String selectdDeivceId = "";

    public String getSelectdDeivceId() {
        return selectdDeivceId;
    }

    public void setSelectdDeivceId(String selectdDeivceId) {
        this.selectdDeivceId = selectdDeivceId;
    }

    public DeviceRowIteAdapter(Context context) {
        valueList = new ArrayList<>();
        this.context = context;
    }

    public DeviceRowIteAdapter(Context context, List<DeviceItemBean> valueList) {
        this.valueList = valueList;
        this.context = context;
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public List<DeviceItemBean> getValueList() {
        return valueList;
    }

    public void setValueList(List<DeviceItemBean> valueList) {
        this.valueList = valueList;
    }


    public void addValueLIst(List<DeviceItemBean> valueList) {
        if (this.valueList == null)
            this.valueList = new ArrayList<>();


        this.valueList.addAll(valueList);

    }

    @Override
    public int getCount() {
        return valueList == null ? 0 : valueList.size();
    }

    @Override
    public DeviceItemBean getItem(int i) {
        return valueList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_deviceitem, null);
        }

        DeviceItemBean item = valueList.get(i);

        TextView device_name = ViewHolderUtil.get(view, R.id.device_name);
        TextView device_id = ViewHolderUtil.get(view, R.id.device_id);
        Button btn_del = ViewHolderUtil.get(view, R.id.btn_del);
        RadioButton selthis = ViewHolderUtil.get(view, R.id.selthis);

        device_name.setText(item.getDeviceName());
        device_id.setText(item.getDeviceSerialNumber());

        if (selectdDeivceId.equals(item.getDeviceSerialNumber())) {
            selthis.setChecked(true);
        } else {
            selthis.setChecked(false);
        }

        selthis(selthis, i);
        removeItem(btn_del, i);
        return view;
    }

    private void selthis(View view, final int i) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectdDeivceId.equals(valueList.get(i).getDeviceSerialNumber())){
                    selectdDeivceId="";
                    PreferencesHelper.save(Key.DeviceSerialNUmber,selectdDeivceId);
                    notifyDataSetChanged();
                    return;
                }
                selectdDeivceId = valueList.get(i).getDeviceSerialNumber();
                PreferencesHelper.save(Key.DeviceSerialNUmber,valueList.get(i).getDeviceSerialNumber());
                notifyDataSetChanged();
            }
        });
    }

    /**
     * 删除物品
     *
     * @param i
     */
    public void removeItem(View view, final int i) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (valueList == null) return;
                CommonDialogView.show("你确定要删除吗?", new CommonDialogView.DialogClickListener() {
                    @Override
                    public void doConfirm() {
                        DeviceItemBean item = valueList.get(i);
                        RequestUtils.device_del(item.getDeviceSerialNumber(), new HttpCallBack<String>() {
                            @Override
                            public void success(String result, String msg) {
                                valueList.remove(i);
                                notifyDataSetChanged();
                            }

                            @Override
                            public boolean failure(int state, String msg) {
                                AppUtil.showToast(msg);
                                return super.failure(state, msg);
                            }
                        });
                    }
                });
            }
        });

    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

}
