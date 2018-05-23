package org.usb.driver.error;

/**
 * Description :
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/23
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class OutOfTimeError extends Error{
    public OutOfTimeError() {
        super(" 请求超时！！！");
    }
}
