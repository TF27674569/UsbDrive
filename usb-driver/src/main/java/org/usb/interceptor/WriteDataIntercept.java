package org.usb.interceptor;

import org.usb.Instruct;
import org.usb.driver.DriverManager;

/**
 * Description : 真正发送指令的拦截器
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/17
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class WriteDataIntercept implements Interceptor{

    @Override
    public void intercept(Chain chain) {
        // 拿到指令
        Instruct instruct = chain.instruct();
        // 交给驱动发送指令
        DriverManager.getInstance().writeInstruct(instruct);
        // 分发任务（正常情况下这里为最后一个拦截器）
        chain.proceed(instruct);
    }
}
