package org.usb.driver.error;

/**
 * Description : 重试异常
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/17
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class DriverInitError extends Exception{

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public DriverInitError() {
        super(" 串口打开失败！！！");
    }
}
