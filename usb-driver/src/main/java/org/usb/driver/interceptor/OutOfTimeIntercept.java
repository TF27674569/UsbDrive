package org.usb.driver.interceptor;

import android.os.Handler;
import android.os.Message;

import org.usb.driver.Instruct;
import org.usb.driver.error.OutOfTimeError;
import org.usb.driver.template.CallbackAdapter;
import org.usb.driver.template.Interceptor;

/**
 * Description : 超时拦截器
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/17
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class OutOfTimeIntercept implements Interceptor {

    private static Handler mHandler = new Handler();

    // 超时时间
    private long outTime;

    public OutOfTimeIntercept(long outTime) {
        this.outTime = outTime;
    }

    @Override
    public void intercept(Chain chain) {
        Instruct instruct = chain.instruct();
        // 重置callback
        OutOfTimeCallback outOfTimeCallback = new OutOfTimeCallback(instruct);
        // 代理callback
        instruct.setCallback(outOfTimeCallback);
        // 执行超时等待
        outOfTimeCallback.onOutTime();
        // 分发任务
        chain.proceed(instruct);
    }

    class OutOfTimeCallback extends CallbackAdapter implements Runnable {

        private boolean mIsCallError;

        protected OutOfTimeCallback(Instruct instruct) {
            super(instruct);
        }

        /**
         * 成功
         *
         * @param result 回调指令
         */
        @Override
        public void onSuccess(byte[] result) {
            mHandler.removeCallbacks(this);
            callback.onSuccess(result);
        }

        /**
         * 失败
         *
         * @param throwable 失败原因
         */
        @Override
        public void onError(Throwable throwable) {
            mHandler.removeCallbacks(this);
            if (!mIsCallError) {
                callback.onError(throwable);
            }
        }

        /**
         * 重试等待
         */
        private void onOutTime() {
            mHandler.postDelayed(this, outTime);
        }

        @Override
        public void run() {
            mIsCallError = true;
            callback.onError(new OutOfTimeError());
        }
    }
}
