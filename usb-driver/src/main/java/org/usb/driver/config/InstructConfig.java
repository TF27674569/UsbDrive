package org.usb.driver.config;

/**
 * Description : 指令常量值
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/17
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class InstructConfig {
    //包头
    public static int DATA_HEAD = 0xFFFE;
    public static int DATA_HEAD_UPDATA = 0x7b7c;
    public static int DATA_END_UPDATA = 0x7c7b;
    //包尾
    public static int DATA_END = 0xFEFF;
    //包头长度
    public static int HEAD_SIZE = 2;
    //设备地址长度
    public static int ADDRESS_SIZE = 1;
    //功能长度
    public static int FUNCTION_CODE_SIZE = 1;
    //日志长度
    public static int LOG_LENGTH = 1;
    //日志长度
    public static int LENGTH_LENGTH = 1;
    // 版本号长度
    public static int VERSION_CODE_SIZE = 2;
    // 当前大小
    public static int CURRENT_CODE_SIZE = 2;
    // 升级总块的大小
    public static int ALL_COUNT_SIZE = 2;
    //数据长度
    public static int DATA_LENGTH = 1;
    //包尾长度
    public static int END_SIZE = 2;
    //充电宝的数据长度
    public static int POWER_BANK_LENGTH = 2;
    //其他的类型数据长度
    public static int OTHER_LENGTH = 1;

    // 自检的包头包尾
    public static int CHECK_DATA_HEAD_AND_END = 0xabba;
}
