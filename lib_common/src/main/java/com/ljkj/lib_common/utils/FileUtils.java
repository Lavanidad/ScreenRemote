package com.ljkj.lib_common.utils;

/**
 * 作者: fzy
 * 日期: 2024/9/19
 * 描述: 日志文件相关
 */

import android.content.Context;
import android.os.Environment;

import com.ljkj.lib_common.bean.LogListBean;

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

    // 文件路径常量
    public String LOCAL_FILE_PATH;
    public String LOCAL_DOWNLOAD_FILE_PATH;
    public String CLOUD_DOWNLOAD_FILE_PATH;
    public String APK_DOWNLOAD_FILE_PATH;
    public String CAN_LOG_FILE_PATH;
    public String WORK_LOG_FILE_PATH;
    public String NAV_LOG_FILE_PATH;
    public String ZIP_FILE_PATH;
    public String BLUETOOTH_FILE_PATH;
    public String LOG_FILE_PATH;
    public String OTHER_LOG_FILE_PATH;
    public String CRASH_LOG_FILE_PATH;

    private String MQTT_INFO_FILE_PATH;

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
        LOCAL_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ljkj_remote/";
        LOCAL_DOWNLOAD_FILE_PATH = LOCAL_FILE_PATH + "download/local/";
        APK_DOWNLOAD_FILE_PATH = LOCAL_FILE_PATH + "download/apk/";
        CLOUD_DOWNLOAD_FILE_PATH = LOCAL_FILE_PATH + "download/cloud/";
        CAN_LOG_FILE_PATH = LOCAL_FILE_PATH + "Log/CAN/";
        WORK_LOG_FILE_PATH = LOCAL_FILE_PATH + "Log/ACTION/";
        NAV_LOG_FILE_PATH = LOCAL_FILE_PATH + "Log/NAV/";
        ZIP_FILE_PATH = LOCAL_FILE_PATH + "ZIP/";
        BLUETOOTH_FILE_PATH = LOCAL_FILE_PATH + "bluetooth/";
        MQTT_INFO_FILE_PATH = LOCAL_FILE_PATH + "Log/MQTT/";
        LOG_FILE_PATH = LOCAL_FILE_PATH + "Log";
        CRASH_LOG_FILE_PATH = LOCAL_FILE_PATH + "Log/CRASH/";
        OTHER_LOG_FILE_PATH = LOCAL_FILE_PATH + "Log/OTHER/";

        createFileDirs(LOCAL_FILE_PATH);
        createFileDirs(LOCAL_DOWNLOAD_FILE_PATH);
        createFileDirs(APK_DOWNLOAD_FILE_PATH);
        createFileDirs(CLOUD_DOWNLOAD_FILE_PATH);
        createFileDirs(CAN_LOG_FILE_PATH);
        createFileDirs(WORK_LOG_FILE_PATH);
        createFileDirs(NAV_LOG_FILE_PATH);
        createFileDirs(ZIP_FILE_PATH);
        createFileDirs(BLUETOOTH_FILE_PATH);
        createFileDirs(MQTT_INFO_FILE_PATH);
        createFileDirs(LOG_FILE_PATH);
        createFileDirs(CRASH_LOG_FILE_PATH);
        createFileDirs(OTHER_LOG_FILE_PATH);
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
        File file = new File(LOCAL_DOWNLOAD_FILE_PATH + name);
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

    public File createIngressCallBack(String name) {
        return createFile(LOCAL_DOWNLOAD_FILE_PATH, name);
    }

    // 创建日志文件
    public File createLogFile(String name) {
        return createFile(NAV_LOG_FILE_PATH, name);
    }

    public File createImuLogFile(String name) {
        return createFile(NAV_LOG_FILE_PATH, name);
    }

    public File createAPKFile(String name) {
        return new File(APK_DOWNLOAD_FILE_PATH + name);
    }

    // 创建CAN通讯日志文件
    public File createCanLogFile(String name) {
        return createFile(CAN_LOG_FILE_PATH, name);
    }

    // 创建工作日志文件
    public File createWorkLogFile(String name) {
        return createFile(WORK_LOG_FILE_PATH, name);
    }

    // 创建MQTT信息日志文件
    public File createMqttFile(String name) {
        File file = new File(MQTT_INFO_FILE_PATH + name);
        try {
            if (file.exists() && file.length() > (1024 * 1024 * 2048)) {
                file.delete();
                file.createNewFile();
            } else if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 压缩文件
     */
    public static File compress(String compressFilePath, String name, String toLocalPath) throws IOException {
        File file = new File(compressFilePath);
        File compressDir = new File(toLocalPath);
        if (!compressDir.exists()) {
            compressDir.mkdirs();
        }
        String compressName = name.split("\\.")[0] + ".zip";
        File compressFile = new File(toLocalPath + compressName);

        if (!compressFile.exists()) {
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(compressFile));
                 FileInputStream fileInputStream = new FileInputStream(file)) {

                ZipEntry zipEntry = new ZipEntry(name);
                zipOutputStream.putNextEntry(zipEntry);
                byte[] buffer = new byte[5000];
                int len;
                while ((len = fileInputStream.read(buffer)) != -1) {
                    zipOutputStream.write(buffer, 0, len);
                }
                zipOutputStream.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
        return compressFile;
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
        File fileDirectory = new File(BLUETOOTH_FILE_PATH);
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
