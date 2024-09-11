package com.ljkj.screenremote.helper;

import android.util.Log;

import com.ljkj.screenremote.bean.SerialDataBean;
import com.ljkj.screenremote.utils.ByteUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * 作者: fzy
 * 日期: 2024/9/11
 * 描述:
 */
public class SerialDataParserHelper {

    public static final String TAG = SerialDataParserHelper.class.getSimpleName();

    private static boolean logOpen = false;


    public static SerialDataBean parseData(byte[] data) {
        if (data == null || data.length < 54) {
            Log.e(TAG, "【RCSDK】Data length is insufficient");
            return null;
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        String frameHead1 = ByteUtils.byteToHexString(new byte[]{buffer.get(0)});
        String frameHead2 = ByteUtils.byteToHexString(new byte[]{buffer.get(1)});
        String version = ByteUtils.byteToHexString(new byte[]{buffer.get(2)});
        String frameId = ByteUtils.byteToHexString(new byte[]{buffer.get(3)});
        int frameLength = ByteUtils.bytesToInt(new byte[]{buffer.get(4), buffer.get(5)}, true);
        long timeStamps = ByteUtils.bytesToLong(new byte[]{buffer.get(6), buffer.get(7), buffer.get(8), buffer.get(9)}, true);
        long latitude = ByteUtils.bytesToLong(new byte[]{buffer.get(10), buffer.get(11), buffer.get(12), buffer.get(13)}, true);
        long longitude = ByteUtils.bytesToLong(new byte[]{buffer.get(14), buffer.get(15), buffer.get(16), buffer.get(17)}, true);
        int speed = ByteUtils.bytesToInt(new byte[]{buffer.get(18), buffer.get(19)}, true);
        int rtkStatus = ByteUtils.bytesToInt(new byte[]{buffer.get(20)}, true);
        int satelliteCount = ByteUtils.bytesToInt(new byte[]{buffer.get(21)}, true);
        int liquidLevelValue = ByteUtils.bytesToInt(new byte[]{buffer.get(22), buffer.get(23)}, true);
        int oilCount = ByteUtils.bytesToInt(new byte[]{buffer.get(24)}, true);
        int batteryCount = ByteUtils.bytesToInt(new byte[]{buffer.get(25)}, true);
        int leftErrorCode = ByteUtils.bytesToInt(new byte[]{buffer.get(26)}, true);
        int rightErrorCode = ByteUtils.bytesToInt(new byte[]{buffer.get(27)}, true);
        int[] broadStatus = ByteUtils.bytesToBinaryIntArray(new byte[]{buffer.get(28)});
        int diff = ByteUtils.bytesToInt(new byte[]{buffer.get(29), buffer.get(30), buffer.get(31), buffer.get(32)}, true);
        int medicalPre = ByteUtils.bytesToInt(new byte[]{buffer.get(33), buffer.get(34)}, true);
        long deviceIdLong = ByteUtils.bytesToLong(new byte[]{buffer.get(35), buffer.get(36), buffer.get(37), buffer.get(38)}, true);
        String deviceId = getLastSixChars(Long.toHexString(deviceIdLong)).toUpperCase();
        long angle = ByteUtils.bytesToLong(new byte[]{buffer.get(39), buffer.get(40), buffer.get(41), buffer.get(42)}, true);
        String pathId = ByteUtils.bytesToString(Arrays.copyOfRange(data, 43, 50));
        String crc = Integer.toHexString(ByteUtils.bytesToInt(new byte[]{buffer.get(50), buffer.get(51), buffer.get(52), buffer.get(53)}, true));

        if (logOpen) {
            Log.d("CRC之前",
                    "【RCSDK】" + "\n" +
                            "frameHead1:" + frameHead1 + "\n" +
                            "frameHead2:" + frameHead2 + "\n" +
                            "version:" + version + "\n" +
                            "frameId:" + frameId + "\n" +
                            "frameLength:" + frameLength + "\n" +
                            "timeStamps:" + timeStamps + "\n" +
                            "latitude:" + latitude + "\n" +
                            "longitude:" + longitude + "\n" +
                            "speed:" + speed + "\n" +
                            "rtkStatus:" + rtkStatus + "\n" +
                            "satelliteCount:" + satelliteCount + "\n" +
                            "liquidLevelValue:" + liquidLevelValue + "\n" +
                            "oilCount:" + oilCount + "\n" +
                            "batteryCount:" + batteryCount + "\n" +
                            "leftErrorCode:" + leftErrorCode + "\n" +
                            "rightErrorCode:" + rightErrorCode + "\n" +
                            "broadStatus:" + Arrays.toString(broadStatus) + "\n" +
                            "diff:" + diff + "\n" +
                            "medicalPre:" + medicalPre + "\n" +
                            "deviceId:" + deviceId + "\n" +
                            "crc:" + crc + "\n" +
                            "angle:" + angle + "\n" +
                            "pathId:" + pathId + "\n"
            );
        }

        // CRC Check
        String signCrc = ByteUtils.crc32(data);
        if (crc.equals(signCrc)) {
            if (logOpen) {
                Log.d(TAG, "【RCSDK】解析成功： CRC check success");
            }
            return new SerialDataBean.Builder()
                    .setTimestamp(timeStamps)
                    .setLatitude(latitude / Math.pow(10, 7))
                    .setLongitude(longitude / Math.pow(10, 7))
                    .setSpeed((float) (speed / Math.pow(10, 2)))
                    .setRtkStatus(rtkStatus)
                    .setSatelliteCount(satelliteCount)
                    .setLiquidLevelValue(liquidLevelValue)
                    .setOilCount(oilCount)
                    .setBatteryCount(batteryCount)
                    .setLeftErrorCode(leftErrorCode)
                    .setRightErrorCode(rightErrorCode)
                    .setBroadStatus(broadStatus)
                    .setDiff(diff)
                    .setMedicalPre(medicalPre)
                    .setDeviceId(deviceId)
                    .setAngle((float) (angle / Math.pow(10, 2)))
                    .setPathId(pathId)
                    .build();
        } else {
            Log.d(TAG, "【RCSDK】解析失败： CRC check failed,byteCRC=" + crc + ",signCRC:" + signCrc);
            return null;
        }
    }

    private static String getLastSixChars(String str) {
        return str.length() > 6 ? str.substring(str.length() - 6) : str;
    }
}
