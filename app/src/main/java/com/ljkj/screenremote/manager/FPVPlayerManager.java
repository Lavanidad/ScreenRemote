package com.ljkj.screenremote.manager;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.skydroid.fpvplayer.FPVWidget;
import com.skydroid.fpvplayer.LiveStreamHelper;
import com.skydroid.fpvplayer.LiveStreamListener;
import com.skydroid.fpvplayer.LiveStreamStatus;
import com.skydroid.fpvplayer.PlayerType;
import com.skydroid.fpvplayer.RtspTransport;

/**
 * 作者: fzy
 * 日期: 2024/9/11
 * 描述:
 */
public class FPVPlayerManager {

    private static final String TAG = FPVPlayerManager.class.getSimpleName();

    private static FPVPlayerManager instance;

    public static synchronized FPVPlayerManager getInstance(FPVWidget fpvWidget, String url) {
        if (instance == null) {
            instance = new FPVPlayerManager(fpvWidget, url);
        }
        return instance;
    }

    private final FPVWidget fpvWidget;
    private final LiveStreamHelper liveStreamHelper;
    private final String url;
    private boolean isLiveStart = false;
    private boolean isFullVideo = false;
    private final LiveStreamListener liveStreamListener;

    private FPVPlayerManager(FPVWidget fpvWidget, String url) {
        this.fpvWidget = fpvWidget;
        this.url = url;
        this.liveStreamHelper = new LiveStreamHelper();
        this.liveStreamListener = new LiveStreamListener() {
            @Override
            public void onLiveStreamStatus(LiveStreamStatus status, long timestamp_us) {
                Log.d(TAG, "直播流状态:" + status + " " + timestamp_us);
            }
        };

        init();
    }

    private void init() {
        fpvWidget.setUsingMediaCodec(true); // 硬解码
        // fpvWidget.setUsingMediaCodec(false); // 软解码
        fpvWidget.setPlayerType(PlayerType.ONLY_SKY); // 云卓播放器
        // fpvWidget.setPlayerType(PlayerType.ONLY_IJK); // IJK播放器
        fpvWidget.setRtspTranstype(RtspTransport.AUTO);
        fpvWidget.setUrl(url);
        fpvWidget.start();

        /*
         * 配置编码器相关属性
         */
        liveStreamHelper.addListener(liveStreamListener);
        liveStreamHelper.setBitrate(1000000); // 比特率
        liveStreamHelper.setFrameRate(15); // 输出帧率
        liveStreamHelper.setVideoSize(640, 360); // 输出分辨率
        liveStreamHelper.setUseSoftEncoder(true); // true: 软编码（X264），false: 硬编码
    }

    /**
     * 交换地图 Or 地图画面
     */
    public void switchVideoMap(View mapWidget) {
        if (fpvWidget.getLayoutParams().width == ViewGroup.LayoutParams.MATCH_PARENT) {
            //map
            ConstraintLayout.LayoutParams mapLayoutParams = (ConstraintLayout.LayoutParams) mapWidget.getLayoutParams();
            mapLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            mapLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mapWidget.setLayoutParams(mapLayoutParams);
            mapWidget.setTranslationZ(-1f);

            //fpv
            ConstraintLayout.LayoutParams fpvLayoutParams = new ConstraintLayout.LayoutParams(
                    SizeUtils.dp2px(178), SizeUtils.dp2px(118));
            fpvLayoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
            fpvLayoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
            fpvLayoutParams.leftMargin = SizeUtils.dp2px(12);
            fpvLayoutParams.bottomMargin = SizeUtils.dp2px(15);
            fpvWidget.setLayoutParams(fpvLayoutParams);
            fpvWidget.bringToFront();
        } else {
            //fpv
            ConstraintLayout.LayoutParams fpvLayoutParams = new ConstraintLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            fpvLayoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
            fpvLayoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
            fpvWidget.setLayoutParams(fpvLayoutParams);

            // map
            ConstraintLayout.LayoutParams mapLayoutParams = new ConstraintLayout.LayoutParams(
                    SizeUtils.dp2px(178), SizeUtils.dp2px(118));
            mapLayoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
            mapLayoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
            mapLayoutParams.leftMargin = SizeUtils.dp2px(12);
            mapLayoutParams.bottomMargin = SizeUtils.dp2px(15);
            mapWidget.setLayoutParams(mapLayoutParams);
            mapWidget.setTranslationZ(0f);
            mapWidget.bringToFront();
        }
    }


    /**
     * 回收
     */
    public void release() {
        fpvWidget.stop();
        liveStreamHelper.removeListener(liveStreamListener);
        if (isLiveStart) {
            liveStreamHelper.stop();
            isLiveStart = false;
        }
    }

    public static void clearInstance() {
        instance = null;
    }
}
