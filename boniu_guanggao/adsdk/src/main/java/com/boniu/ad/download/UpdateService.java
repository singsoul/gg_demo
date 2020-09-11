package com.boniu.ad.download;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;


import com.boniu.ad.R;
import com.boniu.ad.SplashSingleton;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class UpdateService extends Service {
    public static String CALENDAR_ID = "channel_01";
    public static int NotificationID = 1;
    private NotificationManager notificationManager;
    private Notification notification;
    private NotificationCompat.Builder notificationBuilder;
    public static String APP_NAME = "APP_NAME";
    public static String APP_LOGO = "APP_LOGO";
    public static String APP_URL = "APP_URL";
    private String appName,appUrl,appLogo;
    private Bitmap logoBitmap = null;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //通知栏跳转Intent
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotification(notificationManager);
        appName = intent.getStringExtra(APP_NAME);
        appUrl = intent.getStringExtra(APP_URL);
        appLogo = intent.getStringExtra(APP_LOGO);
        new Thread(new Runnable() {
            @Override
            public void run() {
                logoBitmap = getBitmap(appLogo);
                myHandler.sendEmptyMessage(0);

            }
        }).start();

        return super.onStartCommand(intent, 0, 0);
    }

    public NotificationCompat.Builder getNotificationBuilder(String name) {
        if (logoBitmap == null ){
            logoBitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.smart_logo);
        }
        return new NotificationCompat.Builder(getApplicationContext(), CALENDAR_ID)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(name)
                .setSmallIcon(R.drawable.smart_logo)
                .setContentText("context...")
                .setLargeIcon(logoBitmap)
                .setAutoCancel(true);
    }

    private static void createNotification(NotificationManager notificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CALENDAR_ID, "ander drowload apk default channel.",
                    NotificationManager.IMPORTANCE_MIN);
            // 设置渠道描述
            notificationChannel.setDescription("通知更新");
            // 是否绕过请勿打扰模式
            notificationChannel.canBypassDnd();
            // 设置绕过请勿打扰模式
            notificationChannel.setBypassDnd(true);
            // 桌面Launcher的消息角标
            notificationChannel.canShowBadge();
            // 设置显示桌面Launcher的消息角标
            notificationChannel.setShowBadge(true);
            // 设置通知出现时声音，默认通知是有声音的
            notificationChannel.setSound(null, null);
            // 设置通知出现时的闪灯（如果 android 设备支持的话）
            notificationChannel.enableLights(false);
            notificationChannel.setLightColor(Color.RED);
            // 设置通知出现时的震动（如果 android 设备支持的话）
            notificationChannel.enableVibration(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            notificationBuilder = getNotificationBuilder(appName);
            notification = notificationBuilder.build();
            notificationManager.notify(NotificationID, notification);
            File externalDownloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            File files = new File(externalDownloadsDir, "test.apk");
            DownloadUtil.get().download( appUrl,files,
                    new DownloadUtil.OnDownloadListener() {
                        @Override
                        public void onDownloadSuccess(File file) {

                            Intent intents = new Intent("android.intent.action.VIEW");
                            String packageName = SplashSingleton.getInstance().getPackageName();
                            Log.e("asd", "onDownloadSuccess: " + packageName );
                            //适配N
                            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.N){
                                Uri contentUrl = FileProvider.getUriForFile(UpdateService.this, SplashSingleton.getInstance().getPackageName()+".TTFileProvider",file);
                                intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intents.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intents.setDataAndType(contentUrl,"application/vnd.android.package-archive");
                            }else{
                                intents.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
                                intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            }
                            startActivity(intents);
//                        notificationBuilder .setContentText("下载成功,点击安装应用");
//
//                        notificationBuilder.setAutoCancel(true);
//                        notification = notificationBuilder.build();

                            notificationManager.cancel(NotificationID);
                            stopSelf();
                        }
                        @Override
                        public void onDownloading(int progress) {
                            notificationBuilder .setContentText("当前下载进度 " + progress + "%");
                            notification = notificationBuilder.build();
                            notificationManager.notify(NotificationID, notification);
                        }
                        @Override
                        public void onDownloadFailed(Exception e) {
                            Log.e("asd", "onDownloadFailed: " + e.getLocalizedMessage() );
                            Log.e("Asd", "onDownloadFailed: " + e.getMessage() );
                            notificationBuilder .setContentText("下载失败");
                            notificationBuilder.setAutoCancel(true);
                            notification = notificationBuilder.build();
                            notificationManager.cancel(NotificationID);
                            notificationManager.notify(0, notification);
                            stopSelf();
                        }
                    });
        }
    };
    public Bitmap getBitmap(String url) {
        Bitmap bm = null;
        try {
            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;

            int length = http.getContentLength();

            conn.connect();
            // 获得图像的字符流
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();// 关闭流
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }

}