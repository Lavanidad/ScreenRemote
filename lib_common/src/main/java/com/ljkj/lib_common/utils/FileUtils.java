package com.ljkj.lib_common.utils;

/**
 * 作者: fzy
 * 日期: 2024/9/19
 * 描述: 日志文件相关
 */

import android.content.Context;
import android.os.Environment;

import com.ljkj.lib_common.bean.LogListBean;
import com.ljkj.lib_common.common.Constants;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import io.reactivex.rxjava3.core.Single;

/**
 * 本地文件操作工具类
 */
public class FileUtils {

    private static FileUtils instance;
    private static Context context;


    // 私有构造函数
    private FileUtils() {
    }

    // 单例获取实例
    public static FileUtils getInstance() {
        if (instance == null) {
            instance = new FileUtils();
        }
        return instance;
    }

    // 初始化路径
    public void init(Context mContext) {
        context = mContext;

        createFileDirs(Constants.LOCAL_FILE_PATH);
        createFileDirs(Constants.LOCAL_DOWNLOAD_FILE_PATH);
        createFileDirs(Constants.APK_DOWNLOAD_FILE_PATH);
        createFileDirs(Constants.CLOUD_DOWNLOAD_FILE_PATH);
        createFileDirs(Constants.CAN_LOG_FILE_PATH);
        createFileDirs(Constants.WORK_LOG_FILE_PATH);
        createFileDirs(Constants.NAV_LOG_FILE_PATH);
        createFileDirs(Constants.ZIP_FILE_PATH);
        createFileDirs(Constants.BLUETOOTH_FILE_PATH);
        createFileDirs(Constants.MQTT_INFO_FILE_PATH);
        createFileDirs(Constants.LOG_FILE_PATH);
        createFileDirs(Constants.CRASH_LOG_FILE_PATH);
        createFileDirs(Constants.OTHER_LOG_FILE_PATH);
    }

    // 创建文件夹
    public void createFileDirs(String dirPath) {
        File fileDirectory = new File(dirPath);
        if (!fileDirectory.exists()) {
            fileDirectory.mkdirs();
        }
    }

    // 创建文件
    public File create(String name) {
        File file = new File(Constants.LOCAL_DOWNLOAD_FILE_PATH + name);
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    // 获取文件大小
    public static int getFileSize(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            return fis.available();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 从蓝牙文件夹中获取指定的.csv文件
    public List<File> getFileListFromBluetooth() {
        File fileDirectory = new File(Constants.BLUETOOTH_FILE_PATH);
        List<File> listFiles = new ArrayList<>();
        if (fileDirectory.exists()) {
            for (File file : fileDirectory.listFiles()) {
                if (file.getName().startsWith("share_")) {
                    listFiles.add(file);
                }
            }
        }
        return listFiles;
    }

    /**
     * 获取指定文件夹下的文件列表
     */
    public List<LogListBean> getSonNode(File path, int type) {
        List<LogListBean> list = new ArrayList<>();
        File[] files = path.listFiles();
        if (files != null) {
            for (File file : files) {
                if (type == 6 && !file.getName().startsWith(context.getPackageName())) {
                    continue;
                }
                LogListBean fileInfo = new LogListBean();
                fileInfo.setType(type);
                fileInfo.setName(file.getName());
                fileInfo.setPath(file.getAbsolutePath());
                list.add(fileInfo);
            }
            return setSortForLastModified(list);
        }
        return null;
    }

    /**
     * 按文件修改时间排序
     */
    public static List<LogListBean> setSortForLastModified(List<LogListBean> list) {
        if (list.isEmpty()) {
            return list;
        }
        list.sort((f1, f2) -> Long.compare(new File(f2.getPath()).lastModified(), new File(f1.getPath()).lastModified()));
        return list;
    }

    // 辅助方法，简化文件创建
    private File createFile(String dirPath, String name) {
        File fileDirectory = new File(dirPath);
        if (!fileDirectory.exists()) {
            fileDirectory.mkdirs();
        }
        File file = new File(dirPath + name);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 压缩文件，防重名
     *
     * @param context
     * @param files
     * @param zipName
     * @return
     */
    public Single<File> compressFiles(Context context, List<File> files, String zipName) {
        return Single.create(emitter -> {
            File zipFile = new File(context.getCacheDir(), zipName);
            try (FileOutputStream fos = new FileOutputStream(zipFile);
                 BufferedOutputStream bos = new BufferedOutputStream(fos);
                 ZipOutputStream zos = new ZipOutputStream(bos)) {

                //确保文件名不重复
                Set<String> fileNames = new HashSet<>();

                for (File file : files) {
                    String fileName = file.getName();

                    // 检查文件名是否重复，如果重复则添加唯一前缀
                    if (fileNames.contains(fileName)) {
                        fileName = fileName + "_repeat_" + System.currentTimeMillis();
                    }

                    fileNames.add(fileName);

                    try (FileInputStream fis = new FileInputStream(file);
                         BufferedInputStream bis = new BufferedInputStream(fis)) {

                        // 创建新的 ZipEntry，避免重复文件名
                        ZipEntry zipEntry = new ZipEntry(fileName);
                        zos.putNextEntry(zipEntry);

                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = bis.read(buffer)) > 0) {
                            zos.write(buffer, 0, len);
                        }

                        zos.closeEntry();
                    } catch (IOException e) {
                        emitter.onError(e);
                        return;
                    }
                }

                zos.finish();
                emitter.onSuccess(zipFile);

            } catch (IOException e) {
                emitter.onError(e);
            }
        });
    }

}
