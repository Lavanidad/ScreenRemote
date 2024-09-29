package com.ljkj.lib_common.utils;

import android.content.Context;
import android.util.Log;

import com.ljkj.lib_common.bean.PathPointBean;
import com.ljkj.lib_common.bean.SerialDataBean;
import com.ljkj.lib_common.manager.FileManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @description .csv文件相关操作
 * @author: Lenovo
 * @date: 2023/2/7
 */
public class CSVUtils {


    /**
     * 已指定文件读取.csv文件
     *
     * @param file
     * @return
     */
    public static ArrayList<PathPointBean> readCsv(File file) {
        ArrayList<PathPointBean> readerArr = new ArrayList<>();
        FileInputStream fileInputStream;
        Scanner in;
        try {
            fileInputStream = new FileInputStream(file);
            in = new Scanner(fileInputStream, "UTF-8");
            in.nextLine();
            while (in.hasNextLine()) {
                String[] lines = in.nextLine().split(",");
                PathPointBean bean = new PathPointBean(Double.parseDouble(lines[0]), Double.parseDouble(lines[1])
                        , Float.parseFloat(lines[2]), Integer.parseInt(lines[3]));
                readerArr.add(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return readerArr;
    }

    /**
     * 串口返回平板数据
     */
    /**
     * 串口返回平板数据
     */
    public static void writeCsvLogFile(Context context, SerialDataBean data, String name) {
        File file = null;
        FileManager ft = FileManager.getInstance();
        try {
            // 创建 CSV 文件
            file = ft.createNavLogFile(name + "_NAV.csv");
            BufferedWriter bw = null;
            try {
                // 打开文件进行写入（追加模式）
                bw = new BufferedWriter(new FileWriter(file, true));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            if (file != null && bw != null) {
                // 如果文件是空的，写入表头
                if (file.length() == 0) {
                    bw.write("timestamp,latitude,longitude,speed,rtkStatus,satelliteCount,liquidLevelValue,oilCount,batteryCount,leftErrorCode,rightErrorCode,broadStatus,diff,medicalPre,deviceId,angle,pathId");
                    bw.write("\r\n");
                }

                // 写入数据行
                bw.write(
                        data.getTimestamp() + "," +
                                data.getLat() + "," +
                                data.getLng() + "," +
                                data.getSpeed() + "," +
                                data.getRtkStatus() + "," +
                                data.getSatelliteCount() + "," +
                                data.getLiquidLevelValue() + "," +
                                data.getOilCount() + "," +
                                data.getBatteryCount() + "," +
                                data.getLeftErrorCode() + "," +
                                data.getRightErrorCode() + "," +
                                Arrays.toString(data.getBroadStatus()) + "," +
                                data.getDiff() + "," +
                                data.getMedicalPre() + "," +
                                data.getDeviceId() + "," +
                                data.getAngle() + "," +
                                data.getPathId()
                );
                bw.write("\r\n");

                // 刷新并关闭 BufferedWriter
                bw.flush();
                bw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 写入 CSV 文件的方法
    public static void writeCsvLogFile(Context context, String data) {
        File file = null;
        FileManager ft = FileManager.getInstance();
        try {
            file = ft.createNavLogFile("button_events.csv");
            BufferedWriter bw = null;
            try {
                bw = new BufferedWriter(new FileWriter(file, true));
            } catch (IOException e) {

            }

            if (file != null && bw != null) {
                if (file.length() == 0) {
                    bw.write("timestamp,buttonAction,oldValue,newValue,actionDescription");
                    bw.write("\r\n");
                }

                bw.write(data);
                bw.write("\r\n");
                bw.flush();
                bw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
