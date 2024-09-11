package com.ljkj.screenremote.utils;

import java.util.Locale;
import java.util.zip.CRC32;

/**
 * 作者: fzy
 * 日期: 2024/9/11
 * 描述:
 */
public class ByteUtils {

    /**
     * Byte[] 转成十六进制字符串
     */
    public static String byteToHexString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        if (data != null) {
            for (int i = 0; i < data.length; i++) {
                String tempHexStr = Integer.toHexString(data[i] & 0xFF).toUpperCase(Locale.getDefault()) + " ";
                tempHexStr = (tempHexStr.length() == 2) ? "0" + tempHexStr : tempHexStr;
                sb.append(tempHexStr);
            }
        }
        return sb.toString();
    }

    public static int bytesToInt(byte[] b, boolean... lowerBefore) {
        if (lowerBefore.length > 0 && lowerBefore[0]) {
            return byteArrayToLowerInt(b);
        } else {
            return byteArrayToHigherInt(b);
        }
    }

    public static String bytesToString(byte[] b) {
        StringBuilder str = new StringBuilder();
        for (byte value : b) {
            int bInt = value & 0xFF;
            if (bInt == 0) {
                continue;
            }
            str.append((char) bInt);
        }
        return str.toString();
    }

    public static long bytesToLong(byte[] b, boolean... lowerBefore) {
        if (lowerBefore.length > 0 && lowerBefore[0]) {
            return byteArrayToLowerLong(b);
        } else {
            return byteArrayToHigherLong(b);
        }
    }

    /**
     * Byte[] 转换成 Int
     * 低位在前
     */
    private static int byteArrayToLowerInt(byte[] b) {
        int res = 0;
        for (int i = 0; i < b.length; i++) {
            res += (b[i] & 0xff) << (i * 8);
        }
        return res;
    }

    /**
     * Byte[] 转换成 Long
     * 低位在前
     */
    private static long byteArrayToLowerLong(byte[] b) {
        long res = 0L;
        for (int i = 0; i < b.length; i++) {
            res += (b[i] & 0xffL) << (i * 8);
        }
        return res;
    }

    /**
     * Byte[] 转换成 Int
     * 高位在前
     */
    private static int byteArrayToHigherInt(byte[] b) {
        int res = 0;
        for (int i = 0; i < b.length; i++) {
            res += (b[i] & 0xff) << ((b.length - 1 - i) * 8);
        }
        return res;
    }

    /**
     * Byte[] 转换成 Long
     * 高位在前
     */
    private static long byteArrayToHigherLong(byte[] b) {
        long res = 0L;
        for (int i = 0; i < b.length; i++) {
            res += (b[i] & 0xffL) << ((b.length - 1 - i) * 8);
        }
        return res;
    }

    /**
     * bytes 转二进制数组
     */
    public static int[] bytesToBinaryIntArray(byte[] b) {
        int[] data = new int[8 * b.length];
        for (int i = 0; i < b.length; i++) {
            int byteValue = b[i] & 0xff;
            for (int j = 0; j < 8; j++) {
                data[i * 8 + j] = (byteValue >> (7 - j)) & 0x01;
            }
        }
        return data;
    }

    /**
     * 计算 CRC32 校验码
     * @param data 输入的字节数组
     * @return CRC32 校验码的十六进制字符串表示
     */
    public static String crc32(byte[] data) {
        CRC32 crc32 = new CRC32();
        crc32.reset();
        crc32.update(data, 0, data.length - 4);
        return Long.toHexString(crc32.getValue());
    }


    public static int bytesToInt(byte[] b, boolean lowerBefore) {
        int res = 0;
        if (lowerBefore) {
            for (int i = 0; i < b.length; i++) {
                res += (b[i] & 0xff) << (i * 8);
            }
        } else {
            for (int i = 0; i < b.length; i++) {
                res += (b[i] & 0xff) << ((b.length - 1 - i) * 8);
            }
        }
        return res;
    }

    public static long bytesToLong(byte[] b, boolean lowerBefore) {
        long res = 0L;
        if (lowerBefore) {
            for (int i = 0; i < b.length; i++) {
                res += ((long) b[i] & 0xff) << (i * 8);
            }
        } else {
            for (int i = 0; i < b.length; i++) {
                res += ((long) b[i] & 0xff) << ((b.length - 1 - i) * 8);
            }
        }
        return res;
    }
}
