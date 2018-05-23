package org.usb.driver.interceptor;

import org.usb.driver.Instruct;
import org.usb.driver.driver.DriverManager;
import org.usb.driver.template.Interceptor;

/**
 * Description : 实现写数据的拦截器  为最后一个 拦截器
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/23
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class WriteDataIntercept implements Interceptor {
    /**
     * 拦截操作
     *
     * @param chain
     */
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
