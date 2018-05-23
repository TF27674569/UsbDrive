package org.usb.driver.utils;

import android.util.Log;

/**
 * Description :
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/23
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class Utils {
    public static final String TAG = "UsbDriver";

    public static int byteArrayToInt(byte[] b) {
        return b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    public static int byteToInt(byte b) {
        //Java 总是把 byte 当做有符处理；我们可以通过将其和 0xFF 进行二进制与得到它的无符值
        return b & 0xFF;
    }

    /**
     * 打印十六进制数据
     *
     * @param command
     * @param message
     */
    public static void printHex(byte[] command, String message) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < command.length; i++) {
            byte b = command[i];
            int i1 = byteToInt(b);
            String s = Integer.toHexString(i1);
            if (s.length() == 1) {
                s = "0" + s;
            }
            buffer.append(s);
        }
        Log.e(TAG, message + ":" + buffer.toString());
    }

    /**
     * 判端两个byte是否是我需要的
     *
     * @param byte1
     * @param byte2
     * @return
     */
    public static boolean equals(byte[] byte1, byte[] byte2) {
        // 地址功能码一样则表示一样
        return byte1[3] == byte2[3] && byte1[4] == byte2[4];
    }

    /**
     * 完全一样时表示握手
     */
    public static boolean beEqualTo(byte[] result, byte[] byteInstruct) {

        if (result.length != byteInstruct.length) return false;

        for (int i = 0; i < result.length; i++) {
            if (result[i] != byteInstruct[1]) {
                return false;
            }
        }
        return true;
    }
}
