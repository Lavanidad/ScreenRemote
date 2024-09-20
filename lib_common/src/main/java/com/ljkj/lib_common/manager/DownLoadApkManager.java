package com.ljkj.lib_common.manager;


import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 作者: fzy
 * 日期: 2024/9/20
 * 描述:
 */
public class DownLoadApkManager {

    private static DownLoadApkManager instance;
    private OkHttpClient client;
    private String fileLocalPath;
    private String apkName;

    private DownLoadApkManager(String fileLocalPath, String apkName) {
        this.fileLocalPath = fileLocalPath;
        this.apkName = apkName;
        client = new OkHttpClient();
    }

    public static synchronized DownLoadApkManager getInstance(String fileLocalPath, String apkName) {
        if (instance == null) {
            instance = new DownLoadApkManager(fileLocalPath, apkName);
        }
        return instance;
    }

    public Observable<Integer> downLoadApk(String url) {
        return Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
                    File directory = new File(fileLocalPath);
                    if (!directory.exists()) {
                        directory.mkdirs();// 创建目录
                    }

                    File destinationFile = new File(directory, apkName);

                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            if (!emitter.isDisposed()) {
                                emitter.onError(new Exception("下载失败: " + e.getMessage()));
                            }
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            if (!response.isSuccessful()) {
                                if (!emitter.isDisposed()) {
                                    emitter.onError(new Exception("下载失败，响应码: " + response.code()));
                                }
                                return;
                            }

                            try (InputStream inputStream = response.body().byteStream();
                                 FileOutputStream outputStream = new FileOutputStream(destinationFile)) {

                                long totalBytes = response.body().contentLength();
                                byte[] buffer = new byte[1024];
                                long downloadedBytes = 0;
                                int readBytes;

                                while ((readBytes = inputStream.read(buffer))!= -1) {
                                    outputStream.write(buffer, 0, readBytes);
                                    downloadedBytes += readBytes;

                                    int progress = (int) (100 * downloadedBytes / totalBytes);
                                    if (!emitter.isDisposed()) {
                                        emitter.onNext(progress); // 发送进度
                                    }
                                }

                                if (!emitter.isDisposed()) {
                                    emitter.onComplete(); // 下载成功
                                }
                            } catch (IOException e) {
                                if (!emitter.isDisposed()) {
                                    emitter.onError(new Exception("写入文件失败: " + e.getMessage()));
                                }
                            }
                        }
                    });
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
