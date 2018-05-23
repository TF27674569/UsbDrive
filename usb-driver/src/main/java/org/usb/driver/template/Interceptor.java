package org.usb.driver.template;

import android.os.Handler;

import org.usb.driver.Instruct;

/**
 * Description : 拦截器
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/23
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public interface Interceptor {
    // 超时what
    int WHAT_OUT_OF_TIME = 1;
    // 重试what
    int WHAT_RETRY = 2;

    // 多次实例化也只会用这一个handler发送 callback不一样
    static final Handler INTERCEPTOR_HANDLER = new Handler();

    /**
     * 拦截操作
     */
    void intercept(Chain chain);

    interface Chain {

        /**
         * 获取任务
         */
        Instruct instruct();

        /**
         * 分发操作
         */
        void proceed(Instruct instruct);
    }
}
