package org.usb.interceptor;

import org.usb.Instruct;
import org.usb.base.Callback;
import org.usb.driver.DriverManager;

/**
 * Description : 移除已发指令的拦截器
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/18
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class RemoveInstructInterceptor implements Interceptor {

    @Override
    public void intercept(Chain chain) {
        Instruct instruct = chain.instruct();
        instruct.setCallback(new RemoveInstructCallback(instruct));
        chain.proceed(instruct);
    }


    /**
     * 失败成功都将这一条指令移除
     */
    class RemoveInstructCallback implements Callback {

        private Callback callback;
        private Instruct instruct;

        public RemoveInstructCallback(Instruct instruct) {
            this.instruct = instruct;
            this.callback = instruct.getCallback();
        }

        /**
         * 成功
         *
         * @param result 回调指令
         */
        @Override
        public void onSuccess(byte[] result) {
            callback.onSuccess(result);
            DriverManager.getInstance().removeInstruct(instruct);
        }

        /**
         * 失败
         *
         * @param throwable 失败原因
         */
        @Override
        public void onError(Throwable throwable) {
            callback.onError(throwable);
            DriverManager.getInstance().removeInstruct(instruct);
        }
    }
}
