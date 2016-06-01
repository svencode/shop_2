package com.cqgk.clerk.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.cqgk.clerk.R;
import com.cqgk.clerk.base.BusinessBaseActivity;
import com.cqgk.clerk.utils.CheckUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by duke on 16/5/31.
 */
@ContentView(R.layout.activity_crashlog)
public class CrashLogActivity extends BusinessBaseActivity {


    @ViewInject(R.id.content)
    TextView content;

    private String filePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("崩溃日志");

        try {
            filePath = getStringExtra("path");
        } catch (Exception e) {

        }

        String contentStr = readSDFile(filePath);
        if(CheckUtils.isAvailable(contentStr)) {
            content.setText(contentStr);
        }else{
            content.setText(String.format("打开日志错误,请到%s手动打开",filePath));
        }
    }

    public String readSDFile(String fileName) {

        StringBuffer sb = new StringBuffer();

        File file = new File(filePath);

        try {

            FileInputStream fis = new FileInputStream(file);

            int c;

            sb.append("可以复制粘贴,请把错误发给技术\n\n");
            while ((c = fis.read()) != -1) {

                sb.append((char) c);

            }

            fis.close();

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

        return sb.toString();

    }
}
