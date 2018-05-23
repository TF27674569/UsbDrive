package org.usb.driver.interceptor;

import org.usb.driver.Instruct;
import org.usb.driver.driver.DriverManager;
import org.usb.driver.template.CallbackAdapter;
import org.usb.driver.template.Interceptor;


/**
 * Description : 移除指令的拦截器 任务执行成功失败都需要移除指令
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/23
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class RemoveInstructInterceptor implements Interceptor {

    /**
     * 拦截操作
     *
     * @param chain
     */
    @Override
    public void intercept(Chain chain) {
        Instruct instruct = chain.instruct();
        instruct.setCallback(new RemoveInstructCallback(instruct));
        chain.proceed(instruct);
    }

    private class RemoveInstructCallback extends CallbackAdapter {
        private Instruct instruct;
        protected RemoveInstructCallback(Instruct instruct) {
            super(instruct);
            this.instruct = instruct;
        }

        /**
         * 成功
         *
         * @param result 回调指令
         */
        @Override
        public void onSuccess(byte[] result) {
            DriverManager.getInstance().removeInstruct(instruct);
            callback.onSuccess(result);
        }

        /**
         * 失败
         *
         * @param throwable 失败原因
         */
        @Override
        public void onError(Throwable throwable) {
            DriverManager.getInstance().removeInstruct(instruct);
            callback.onError(throwable);
        }
    }

}
