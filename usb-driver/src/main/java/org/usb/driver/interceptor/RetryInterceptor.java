package org.usb.driver.interceptor;

import android.os.Message;

import org.usb.driver.Instruct;
import org.usb.driver.driver.DriverManager;
import org.usb.driver.error.RetryOutCountError;
import org.usb.driver.template.CallbackAdapter;
import org.usb.driver.template.Interceptor;

/**
 * Description : 重试拦截器
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/23
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class RetryInterceptor implements Interceptor {

    /**
     * 拦截操作
     */
    @Override
    public void intercept(Chain chain) {
        // 指令
        Instruct instruct = chain.instruct();

        // 修改回调为代理回调
        RetryCallback callback = new RetryCallback(instruct);

        // 代理callback
        instruct.setCallback(callback);

        // 分发
        chain.proceed(instruct);

        // 重试轮询
        callback.retry();
    }


    class RetryCallback extends CallbackAdapter implements Runnable {

        // 当前次数
        private int count;
        private Instruct instruct;

        protected RetryCallback(Instruct instruct) {
            super(instruct);
            this.instruct = instruct;
        }

        @Override
        public void onSuccess(byte[] result) {
            INTERCEPTOR_HANDLER.removeMessages(WHAT_RETRY);
            super.onSuccess(result);
        }

        private void retry() {
            Message obtain = Message.obtain(INTERCEPTOR_HANDLER, this);
            obtain.what = WHAT_RETRY;
            INTERCEPTOR_HANDLER.sendMessageDelayed(obtain, retryTimer);
        }

        @Override
        public void run() {
            // 重试超过后
            if (count > retyrCount) {
                // 回调重试异常
                onError(new RetryOutCountError());
            }

            // 重新发送指令
            DriverManager.getInstance().writeInstruct(instruct);

            // 轮询
            retry();
            count++;

        }
    }
}
