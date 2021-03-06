package com.cqgk.clerk.base;

import android.app.Application;
import android.app.NotificationManager;
import android.os.Debug;

import com.cqgk.clerk.helper.ImageHelper;
import com.cqgk.clerk.helper.PreferencesHelper;
import com.cqgk.clerk.view.galleryfinall.UILImageLoader;
import com.cqgk.clerk.view.galleryfinall.UILPauseOnScrollListener;
import com.cqgk.clerk.BuildConfig;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.analytics.MobclickAgent;

import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;

/**
 *
 */
public class BaseApp extends Application {


    public static BaseApp Context;


    @Override
    public void onCreate() {
        super.onCreate();
        Context = this;
        //PlatformConfig.setWeixin(Constant.payAppId, Constant.appSecret);//友盟微信服务
        Basic.setAppContext(this);
        PreferencesHelper.init(this);//全局参服务


        //图片服务
        ImageHelper.getInstance().init();
        initImageLoader(getApplicationContext());


        galleryfinalInit();//图库

        x.Ext.init(this);//xutils3
        x.Ext.setDebug(true);

        if (BuildConfig.FLAVOR.equals("outversion")) {
            MobclickAgent.setCatchUncaughtExceptions(false);
        } else {
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(this);
        }

    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    /**
     * 设置友盟push系统
     */
//    private void setPushService() {
//        if (PreferencesHelper.find(Key.PUSH, true)) {
//            mPushAgent.enable();
//        } else {
//            mPushAgent.disable();
//        }
//    }

    /**
     * 图片库初始化
     */
    private void galleryfinalInit() {
        //设置主题
        ThemeConfig theme = ThemeConfig.DARK;
//        ThemeConfig theme = new ThemeConfig.Builder()
//                .build();
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(false)
                .setEnableCrop(false)
                .setEnableRotate(false)
                .setCropSquare(false)
                .setEnablePreview(true)

                .build();
        CoreConfig coreConfig = new CoreConfig.Builder(this, new UILImageLoader(), theme)
                .setFunctionConfig(functionConfig)
                .setNoAnimcation(true)
                .setPauseOnScrollListener(new UILPauseOnScrollListener(false, true))
                .build();
        GalleryFinal.init(coreConfig);
    }

    /**
     * 图片组件初始化
     *
     * @param context
     */
    public static void initImageLoader(android.content.Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(800 * 1024 * 1024); // 800 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.memoryCache(new WeakMemoryCache());
        config.threadPoolSize(3);
        if (BuildConfig.DEBUG)
            config.writeDebugLogs();
        ImageLoader.getInstance().init(config.build());
    }


    /**
     *
     */
    public static void exitApp() {
        // 清除通知中信息
        NotificationManager nm = (NotificationManager) Context.getSystemService(NOTIFICATION_SERVICE);
        nm.cancelAll();

        // 清空Activity
        exitAllActivity();
    }

    /**
     * 管理Activity
     */
    private static HashMap<String, IActivity> activityBuf = new HashMap<>();

    public static String getKey(IActivity activity) {
        return String.valueOf(activity.hashCode());
    }

    public static void addActivity(IActivity activity) {
        if (activityBuf.get(getKey(activity)) == null) {
            activityBuf.put(getKey(activity), activity);
        }
    }

    public static void removeActivity(IActivity activity) {
        if (activityBuf.get(getKey(activity)) != null) {
            activityBuf.remove(getKey(activity));
        }
    }

    public static void exitAllActivityButOne(IActivity activity) {
        ArrayList<IActivity> removeList = new ArrayList<>(activityBuf.size());
        for (Iterator<Map.Entry<String, IActivity>> iterator = activityBuf.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, IActivity> entry = iterator.next();
            if (!entry.getKey().equals(getKey(activity))) {
                removeList.add(entry.getValue());
            }
        }
        exitActivityList(removeList);
    }

    public static void exitAllActivity() {
        ArrayList<IActivity> removeList = new ArrayList<>(activityBuf.size());
        for (Iterator<Map.Entry<String, IActivity>> iterator = activityBuf.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, IActivity> entry = iterator.next();
            removeList.add(entry.getValue());
        }
        exitActivityList(removeList);
        activityBuf.clear();
    }

    public static void exitActivityList(ArrayList<IActivity> iActivities) {
        for (IActivity activity : iActivities) {
            if (activity != null) {
                activity.finish();
            }
        }
        iActivities.clear();
    }

    public static BaseApp getInstance() {
        return Context;
    }


    public static HashMap<String, IActivity> getActivityBuf() {
        return activityBuf;
    }

    public static void setActivityBuf(HashMap<String, IActivity> activityBuf) {
        BaseApp.activityBuf = activityBuf;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
