package org.usb.driver.interceptor;

import org.usb.driver.Instruct;
import org.usb.driver.template.Interceptor;
import org.usb.driver.utils.CRC16X25Util;

/**
 * Description : crc 校验
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/7/3
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class CrcInterceptor implements Interceptor {
    /**
     * 拦截操作
     *
     * @param chain
     */
    @Override
    public void intercept(Chain chain) {
        Instruct instruct = chain.instruct();
        byte[] send = instruct.getSend();
        byte[] crc = CRC16X25Util.setParamCRC(send);
        instruct.setSend(crc);
        chain.proceed(instruct);
    }
}
