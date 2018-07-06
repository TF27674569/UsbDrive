package org.usb.driver.config;

/**
 * Description :
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/17
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public final class WorkConfig {
    public static final int ADD = 0x01;
    public static final int REMOVE = 0x02;
    public static final int CLEARN = 0x03;

    /**
     * 重试
     */
    public static final int RETRY = 0x04;
    // 延时时间
    public static long RETRY_TIME = 2000L;

    // 轮询读数据
    public static final int POLLING = 0x05;
    // 延时时间
    public static long POLLING_TIME = 100L;



}
