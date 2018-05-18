package org.usb.interceptor;

import org.usb.Instruct;
import org.usb.base.Callback;
import org.usb.driver.DriverManager;
import org.usb.utils.PrintUtils;

/**
 * Description : 握手拦截器
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/18
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class HandshakeIntercept implements Interceptor{


    @Override
    public void intercept(Chain chain) {
        Instruct instruct = chain.instruct();
        instruct.setCallback(new HandshakeCallback(instruct));
        chain.proceed(instruct);
    }

    /**
     * 失败成功都将这一条指令移除
     */
    class HandshakeCallback implements Callback {

        private Callback callback;
        private Instruct instruct;
        private byte[] byteInstruct;

        public HandshakeCallback(Instruct instruct) {
            this.instruct = instruct;
            this.callback = instruct.getCallback();
            this.byteInstruct = instruct.getSend();
        }

        /**
         * 成功
         *
         * @param result 回调指令
         */
        @Override
        public void onSuccess(byte[] result) {
            // 一样表示握手
            if (!PrintUtils.beEqualTo(result,byteInstruct)){

                // 不一样不是握手可以回调
                callback.onSuccess(result);
            }
        }

        /**
         * 失败
         *
         * @param throwable 失败原因
         */
        @Override
        public void onError(Throwable throwable) {
            callback.onError(throwable);
        }
    }
}
