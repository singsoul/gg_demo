package com.boniu.ad.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;

import com.boniu.ad.R;
import com.boniu.ad.RewardAdManager;
import com.boniu.ad.bean.MeterialBean;
import com.boniu.ad.download.UpdateService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

public class VideoDialog extends Dialog {
    public static final String TAG = "VideoDialog";
    private VideoView videoView;
    private TextView tvCountDown;
    private ImageView imgVoice;
    private RelativeLayout rlApk;

    private ImageView imgLogo;
    private TextView tvName;
    private TextView tvIntroduce;
    private TextView tvDownload;
    private ImageView imgClose;
    private MediaPlayer mediaPlayer;
    private boolean slience = false;
    private MeterialBean meterialBean;
    private int maxTime = 0;
    private int videoTimes = 0;
    private boolean isFinish = false;
    private RewardAdManager.RewardAdManagerListener adManagerListener;
    public VideoDialog(@NonNull Context context, MeterialBean meterialBean, RewardAdManager.RewardAdManagerListener adManagerListener) {
        super(context, R.style.SplashErrorDialogTheme);
        this.meterialBean = meterialBean;
        this.adManagerListener = adManagerListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_video);
        //设置window背景，默认的背景会有Padding值，不能全屏。当然不一定要是透明，你可以设置其他背景，替换默认的背景即可。
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //一定要在setContentView之后调用，否则无效
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();
        videoView.setZOrderOnTop(true);
        videoView.setZOrderMediaOverlay(true);
//        Uri uri=Uri.parse(meterialBean.getLineMate().get(0));
        Uri uri = Uri.parse(meterialBean.getLineMate().get(0));
        videoView.setVideoURI(uri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (mediaPlayer == null){
                    mediaPlayer = mp;
                    maxTime = videoView.getDuration();
                    handler.sendEmptyMessageDelayed(100,500);
                }else{
                    videoView.seekTo(videoTimes);
                    videoView.start();
                    handler.sendEmptyMessageDelayed(100,500);
                }

            }
        });


        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                imgVoice.setVisibility(View.GONE);
                tvCountDown.setVisibility(View.GONE);
                imgClose.setVisibility(View.VISIBLE);
                adManagerListener.rewardVideAdComplete();
                isFinish = true;

            }
        });


        videoView.start();

        initClick();
        initData();

    }





    private void initData() {
        tvName.setText(meterialBean.getVname()+"");
        tvIntroduce.setText(meterialBean.getVdesc()+"");
        Glide.with(getContext()).load(meterialBean.getLogoUrl()+"")
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(imgLogo);
    }

    private void initClick() {
        imgVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    if (!slience) {
                        mediaPlayer.setVolume(0f, 0f);
                    } else {
                        mediaPlayer.setVolume(1, 1);
                    }
                    slience = !slience;
                    imgVoice.setImageResource(slience?R.drawable.bu_voice_silent:R.drawable.bu_voice);
                }

            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adManagerListener.rewardVideAdClose();
                dismiss();
            }
        });

        tvDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (meterialBean.getOpenMode() == 2) {
                    Intent intent = new Intent(getContext(), UpdateService.class);
                    intent.putExtra(UpdateService.APP_URL,meterialBean.getJumpUrl());
                    intent.putExtra(UpdateService.APP_NAME,meterialBean.getVname());
                    intent.putExtra(UpdateService.APP_LOGO,meterialBean.getLogoUrl()+"");
                    getContext().startService(intent);
                }else{
                    String url = "";
                    if (meterialBean.getJumpUrl().contains("http")){
                        url = meterialBean.getJumpUrl()+"";
                    }else{
                        url = "http://" + meterialBean.getJumpUrl();
                    }
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    getContext().startActivity(intent);
                }

            }
        });
    }

    private void initView() {
        videoView = findViewById(R.id.videoview);
        tvCountDown =  findViewById(R.id.tv_count_down);
        imgVoice = findViewById(R.id.img_voice);
        rlApk  = findViewById(R.id.rl_apk);

        imgLogo = findViewById(R.id.img_logo);
        tvName  = findViewById(R.id.tv_name);
        tvIntroduce = findViewById(R.id.tv_introduce);
        tvDownload = findViewById(R.id.tv_download);
        imgClose = findViewById(R.id.img_close);
    }


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==100){
                if (isFinish){
                    tvCountDown.setVisibility(View.GONE);
                    imgVoice.setVisibility(View.GONE);
                }else{
                    int nowTime = videoView.getCurrentPosition();
                    if (nowTime != 0){
                        videoTimes = nowTime;
                        tvCountDown.setText((maxTime - videoTimes)/1000 + "");
                        tvCountDown.setVisibility(View.VISIBLE);
                        imgVoice.setVisibility(View.VISIBLE);
                        rlApk.setVisibility(View.VISIBLE);
                        if (videoTimes >= maxTime){

                        }else{
                            handler.sendEmptyMessageDelayed(100,500);
                        }
                    }
                }


            }
        }
    };


}
