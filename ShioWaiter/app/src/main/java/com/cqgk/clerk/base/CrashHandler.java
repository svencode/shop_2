package com.cqgk.clerk.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import com.cqgk.clerk.helper.NavigationHelper;
import com.cqgk.clerk.utils.AppUtil;
import com.cqgk.clerk.utils.LogUtil;
import com.cqgk.clerk.view.CommonDialogView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.finalteam.toolsfinal.io.FileUtils;

/**
 * Created by duke on 16/5/31.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    public static final String CrashFilePath = "/sdcard/crashlog/";
    public static final int LogFileLimit = 10;

    public static final String TAG = "CrashHandler";

    private String fileName;

    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //CrashHandler实例
    private static CrashHandler INSTANCE = new CrashHandler();
    //程序的Context对象
    private Context mContext;
    //用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();

    //用于格式化日期,作为日志文件名的一部分
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                LogUtil.e(e.getMessage());
            }
            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }


    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.e("an error occured when collect package info" + e.getMessage());
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                LogUtil.e("an error occured when collect crash info" + e.getMessage());
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private int saveCrashInfo2File(Throwable ex) {

        //将设备信息变成string
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        //递归获取全部的exception信息
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result); //将写入的结果

        //构造文件名
        long timestamp = System.currentTimeMillis();
        String time = formatter.format(new Date());
        fileName = "waiter_crash-" + time + "-" + timestamp + ".txt";

        //写文件和限制数量
        //FileUtils.writeFile2SDCard(CrashFilePath, fileName, sb.toString());
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            try {
                File dir = new File(CrashFilePath);
                LogUtil.i(dir.toString());
                if (!dir.exists())
                    dir.mkdir();
                FileOutputStream fos = new FileOutputStream(new File(dir,
                        fileName));
                fos.write(sb.toString().getBytes());
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        cleanLogFileToN(CrashFilePath);
        return 1;
    }

    Comparator<File> newfileFinder = new Comparator<File>() {

        @Override
        public int compare(File x, File y) {
            // TODO Auto-generated method stub
            if (x.lastModified() > y.lastModified()) return 1;
            if (x.lastModified() < y.lastModified()) return -1;
            else return 0;
        }

    };

    private int cleanLogFileToN(String dirname) {
        File dir = new File(dirname);
        if (dir.isDirectory()) {
            File[] logFiles = dir.listFiles();
            if (logFiles.length > LogFileLimit) {
                Arrays.sort(logFiles, newfileFinder);  //从小到大排
                //删掉N个以前的
                for (int i = 0; i < logFiles.length - LogFileLimit; i++) {
                    logFiles[i].delete();
                }
            }
        }

        return 1;
    }


    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }

        //打印错误
        ex.printStackTrace();

        //收集设备参数信息
        collectDeviceInfo(mContext);
        //保存日志文件
        saveCrashInfo2File(ex);


        //使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                LogUtil.e(String.format("file://%s%s", CrashFilePath, fileName));
                AppUtil.showLongToast("很抱歉,程序出现异常,即将退出并打开崩溃日志");

                if(Basic.getActivity()!=null)
                     Basic.getActivity().finish();

                NavigationHelper.getInstance().startCrashLog(CrashFilePath + fileName);
                Looper.loop();
            }
        }.start();


        return true;
    }

}

