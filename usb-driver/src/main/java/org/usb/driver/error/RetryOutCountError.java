package org.usb.driver.error;

/**
 * Description :
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/23
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class RetryOutCountError extends Error {
    public RetryOutCountError() {
        super(" 重试超时！！！");
    }
}
