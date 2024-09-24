package com.ljkj.lib_common.utils;

import com.ljkj.lib_common.bean.PathPointBean;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
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

}
