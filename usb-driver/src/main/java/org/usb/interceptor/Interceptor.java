package org.usb.interceptor;

import org.usb.Instruct;

/**
 * Description :
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/17
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public interface Interceptor {

    void intercept(Chain chain);


    interface Chain {

        Instruct instruct();

        void proceed(Instruct instruct);
    }

}
