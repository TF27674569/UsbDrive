package org.usb.interceptor;

import android.os.Handler;

import org.usb.Instruct;
import org.usb.base.Callback;
import org.usb.driver.DriverManager;
import org.usb.exception.OutOfTimeError;
import org.usb.exception.RetryError;

/**
 * Description : 超时拦截器
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/17
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class OutOfTimeIntercept implements Interceptor {

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

    class OutOfTimeCallback implements Callback, Runnable {

        private Callback callback;
        private Handler handler = new Handler();
        // 重试时间
        private long retryTimer;

        private OutOfTimeCallback(Instruct instruct) {
            this.callback = instruct.getCallback();
            this.retryTimer = instruct.getRetyrCount() * instruct.getRetryTimer();
        }


        /**
         * 成功
         *
         * @param result 回调指令
         */
        @Override
        public void onSuccess(byte[] result) {
            handler.removeCallbacks(this);
            callback.onSuccess(result);
        }

        /**
         * 失败
         *
         * @param throwable 失败原因
         */
        @Override
        public void onError(Throwable throwable) {
            // 1. 判断后面抛出的是否是重试的异常,不是直接抛
            // 2. 是重试的异常 判断用户给的时间满足超时 还 重试
            //    重试： 如果是后面的异常先抛则 不抛这里的异常
            //    超时： 如果后面的异常慢抛 则不抛后面的异常 以时间为判断条件

            if (!(throwable instanceof RetryError)&&outTime > retryTimer){
                handler.removeCallbacks(this);
                callback.onError(throwable);
            }
        }

        private void onOutTime() {
            handler.postDelayed(this, outTime);
        }

        @Override
        public void run() {
            // 回调超时异常
            callback.onError(new OutOfTimeError());
        }
    }
}
