package com.cqgk.clerk.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;


import com.cqgk.clerk.base.BusinessBaseActivity;
import com.cqgk.clerk.utils.AppUtil;
import com.cqgk.clerk.utils.CheckUtils;
import com.cqgk.clerk.view.ProgressWebView;
import com.cqgk.shennong.shop.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by duke on 14/11/15.
 */
public class WebViewActivity extends BusinessBaseActivity implements
        ProgressWebView.MyWebViewListener {

    private ProgressWebView webview;
    private String openUrl = "", title = "";
    private LinearLayout noweb;
    private ImageButton moremenu;
    private Thread mThread;
    private static final int WEB_FAILD = 0;//
    private EditText search_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webviewui);

        noweb = (LinearLayout) findViewById(R.id.noweb);

        webview = (ProgressWebView) findViewById(R.id.webview);
        webview.setMyListener(this);

        enableTitleDelegate();
        getTitleDelegate().setTitle("");

        try {
            openUrl = this.getIntent().getStringExtra("url");
            title = this.getIntent().getStringExtra("title");

        } catch (NullPointerException e) {
        }

        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webview.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                if (url != null && url.startsWith("http://"))
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }

        });


        if (!CheckUtils.isAvailable(openUrl)) {
            NoWebState(true);
            return;
        }

        if (!AppUtil.isNetworkAvailable()) {
            NoWebState(true);
        } else {
            if (mThread == null) {
                mThread = new Thread(runnable);
                mThread.start();
            }
        }

        //LogUtil.d("wesugou", String.format("------------url:%s", openUrl));
        webview.loadUrl(openUrl);

    }

    Runnable runnable = new Runnable() {
        // 重写run()方法，此方法在新的线程中运行
        @Override
        public void run() {
            if (!validWebSiteCode(openUrl)) {
                mHandler.obtainMessage(WEB_FAILD).sendToTarget();
            }
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WEB_FAILD:
                    NoWebState(true);
                    break;
            }
        }
    };


    /**
     * @param state
     */
    private void NoWebState(Boolean state) {
        if (state) {
            webview.setVisibility(View.GONE);
            noweb.setVisibility(View.VISIBLE);
        } else {
            noweb.setVisibility(View.GONE);
            webview.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void WebTitleSet(String msg) {
        getTitleDelegate().setTitle(msg);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
            if (webview.canGoBack()) {
                webview.goBack();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void openSysContact() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        // intent.setData(ContactsContract.Contacts.CONTENT_URI);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (data == null) {
                    return;
                }
                Uri uri = data.getData();
                Cursor cursor = getContentResolver().query(uri, null, null, null,
                        null);
                cursor.moveToFirst();
                // 获取联系人的ID
                String contactId = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.Contacts._ID));

                Cursor phones = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                                + contactId, null, null);
                String phoneNumber = "";
                while (phones.moveToNext()) {
                    phoneNumber = phones
                            .getString(phones
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
                phones.close();

                Map<String, String> sendresult = new HashMap<String, String>();
                sendresult.put("phone", phoneNumber);
                break;
        }
    }

    private boolean validWebSiteCode(String urlString) {
        if (urlString.startsWith("file:")) return true;
        try {
            URL url = new URL(urlString);
            HttpURLConnection uRLConnection = (HttpURLConnection) url
                    .openConnection();
            int statusCode = uRLConnection.getResponseCode();
            String str = String.valueOf(statusCode);
            if (str.startsWith("4") || str.startsWith("5")) {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }


}
