package com.ljkj.lib_common.manager;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import java.util.Locale;

/**
 * 作者: fzy
 * 日期: 2024/9/24
 * 描述:
 */
public class TTSManager extends UtteranceProgressListener {

    private static final String TAG = "TTSManager";
    private static TTSManager instance;
    private TextToSpeech textToSpeech;
    private static boolean debugFlag = true;
    private static boolean switchFlag = true;

    private TTSManager(Context context) {
        if (switchFlag) {
            textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        int result = textToSpeech.setLanguage(Locale.CHINA);
                        textToSpeech.setPitch(1.0F);
                        textToSpeech.setSpeechRate(1.0F);
                        textToSpeech.setOnUtteranceProgressListener(TTSManager.this);
                    } else {
                        Log.e(TAG, "TTS init failed: error code " + status);
                    }
                }
            });
        }
    }

    public static synchronized TTSManager getInstance(Context context) {
        if (instance == null) {
            instance = new TTSManager(context);
        }
        return instance;
    }

    public void release() {
        if (switchFlag) {
            stopSpeak();
            if (textToSpeech != null) {
                textToSpeech.shutdown();
                textToSpeech = null;
            }
        }
    }

    public void speakText(String playText) {
        if (textToSpeech != null) {
            textToSpeech.speak(playText, TextToSpeech.QUEUE_FLUSH, null, playText);
        }
    }

    public void stopSpeak() {
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }

    public boolean isPlaying() {
        return textToSpeech != null && textToSpeech.isSpeaking();
    }

    public void setSpeechRate(float speechRate) {
        if (textToSpeech != null) {
            textToSpeech.setSpeechRate(speechRate);
        }
    }

    public void setPitch(float pitch) {
        if (textToSpeech != null) {
            textToSpeech.setPitch(pitch);
        }
    }

    @Override
    public void onStart(String utteranceId) {
        if (!debugFlag) {
            Log.d(TAG, "TTS started speaking...");
        }
    }

    @Override
    public void onDone(String utteranceId) {
        if (!debugFlag) {
            Log.d(TAG, "TTS finished speaking...");
        }
    }

    @Override
    public void onError(String utteranceId) {
        if (!debugFlag) {
            Log.d(TAG, "TTS error occurred...");
        }
    }

    @Override
    public void onError(String utteranceId, int errorCode) {
        if (!debugFlag) {
            Log.d(TAG, "TTS error occurred: " + errorCode);
        }
    }

    @Override
    public void onStop(String utteranceId, boolean interrupted) {
        if (!debugFlag) {
            Log.d(TAG, "TTS stopped speaking...");
        }
    }

    public static void setTtsSwitch(boolean switchFlag) {
        TTSManager.switchFlag = switchFlag;
    }

    public static void setLogSwitch(boolean debugFlag) {
        TTSManager.debugFlag = debugFlag;
    }
}
