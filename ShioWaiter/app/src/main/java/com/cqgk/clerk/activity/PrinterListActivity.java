package com.cqgk.clerk.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.cqgk.clerk.R;
import com.cqgk.clerk.adapter.DeviceRowIteAdapter;
import com.cqgk.clerk.base.BusinessBaseActivity;
import com.cqgk.clerk.bean.normal.DeviceBindBean;
import com.cqgk.clerk.bean.normal.DeviceItemBean;
import com.cqgk.clerk.bean.normal.DeviceListBean;
import com.cqgk.clerk.config.Key;
import com.cqgk.clerk.helper.PreferencesHelper;
import com.cqgk.clerk.http.HttpCallBack;
import com.cqgk.clerk.http.RequestUtils;
import com.cqgk.clerk.utils.CheckUtils;
import com.cqgk.clerk.view.NormalListView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duke on 16/7/8.
 */
@ContentView(R.layout.activity_printerlist)
public class PrinterListActivity extends BusinessBaseActivity {


    @ViewInject(R.id.printer_deviceid)
    EditText printer_deviceid;

    @ViewInject(R.id.printer_name)
    EditText printer_name;

    @ViewInject(R.id.listview)
    NormalListView listview;

    private List<DeviceItemBean> deviceItemBeanList = new ArrayList<>();


    private DeviceRowIteAdapter deviceRowIteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("打印机设置");

        initView();
        requestData();
    }

    @Override
    public void initView() {
        super.initView();

        deviceRowIteAdapter = new DeviceRowIteAdapter(this);
        deviceRowIteAdapter.setSelectdDeivceId(PreferencesHelper.find(Key.DeviceSerialNUmber,""));
        listview.setAdapter(deviceRowIteAdapter);
    }

    @Override
    public void requestData() {
        super.requestData();
        RequestUtils.device_list(new HttpCallBack<DeviceListBean>() {
            @Override
            public void success(DeviceListBean result, String msg) {
                deviceItemBeanList=result.getDevice();
                deviceRowIteAdapter.setValueList(deviceItemBeanList);
                deviceRowIteAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean failure(int state, String msg) {
                showToast(msg);
                return super.failure(state, msg);
            }
        });
    }

    @Event(R.id.btn_add)
    private void btn_add_click(View view) {
        if (!CheckUtils.isAvailable(printer_deviceid.getText().toString())) {
            showToast("请输入打印机编号");
            return;
        }

        if (!CheckUtils.isAvailable(printer_name.getText().toString())) {
            showToast("请输入打印机名称");
            return;
        }

        RequestUtils.device_bind(printer_deviceid.getText().toString(),
                printer_name.getText().toString(),
                new HttpCallBack<DeviceItemBean>() {
                    @Override
                    public void success(DeviceItemBean result, String msg) {
                        if (result == null)
                            return;

                        deviceItemBeanList.add(result);
                        deviceRowIteAdapter.setValueList(deviceItemBeanList);
                        deviceRowIteAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public boolean failure(int state, String msg) {
                        showToast(msg);
                        return super.failure(state, msg);
                    }
                });
    }
}
