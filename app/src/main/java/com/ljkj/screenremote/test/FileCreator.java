package com.ljkj.screenremote.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileCreator {

    // 创建模拟 CSV 文件
    public static String createMockCsvFile(String directoryPath) {
        String filePath = directoryPath + "/mock_data.csv";
        File file = new File(filePath);

        try {
            FileWriter writer = new FileWriter(file);
            // 写入一些示例数据
            writer.append("ID,Name,Age\n");
            writer.append("1,John Doe,30\n");
            writer.append("2,Jane Smith,25\n");
            writer.append("3,Bob Johnson,40\n");
            writer.flush();
            writer.close();
            System.out.println("Mock CSV file created at: " + filePath);
        } catch (IOException e) {
            System.err.println("Error creating mock CSV file: " + e.getMessage());
        }

        return filePath; // 返回文件路径
    }
}
