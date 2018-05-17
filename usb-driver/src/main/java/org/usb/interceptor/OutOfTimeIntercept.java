package org.usb.interceptor;

import android.os.Handler;

import org.usb.Instruct;
import org.usb.base.Callback;
import org.usb.exception.OutOfTimeError;

/**
 * Description :
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

        public OutOfTimeCallback(Instruct instruct) {
            this.callback = instruct.getCallback();
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
            callback.onError(throwable);
        }

        public void onOutTime() {
            handler.postDelayed(this, outTime);
        }

        @Override
        public void run() {
            callback.onError(new OutOfTimeError());
        }
    }
}