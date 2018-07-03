package org.usb.driver.template;

import android.os.Handler;

import org.usb.driver.Instruct;

import java.util.List;

/**
 * Description : 拦截器
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/23
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public interface Interceptor {

    /**
     * 拦截操作
     */
    void intercept(Chain chain);


    interface Chain {

        /**
         * 所有拦截器
         */
        List<Interceptor> interceptors();

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
