package org.usb.interceptor;


import org.usb.Instruct;
import org.usb.base.Callback;
import org.usb.driver.DriverManager;
import org.usb.exception.RetryTimeOutError;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description : 重试拦截器
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/17
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class RetryInterceptor implements Interceptor {

    @Override
    public void intercept(Chain chain) {
        Instruct instruct = chain.instruct();
        // 重置callback
        RetryCallback retryCallback = new RetryCallback(instruct);
        // 代理callback
        instruct.setCallback(retryCallback);
        // 分发任务
        chain.proceed(instruct);
        // 开始轮询重试操作
        retryCallback.retry();
    }


    class RetryCallback implements Callback, Runnable {

        private Callback callback;
        private Instruct instruct;
        private int count;
        private ExecutorService executorService;


        RetryCallback(Instruct instruct) {
            this.instruct = instruct;
            this.callback = instruct.getCallback();
            executorService = Executors.newSingleThreadExecutor();
        }

        void retry() {
            executorService.execute(this);
        }

        /**
         * 成功
         *
         * @param result 回调指令
         */
        @Override
        public void onSuccess(byte[] result) {
            executorService.shutdown();
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


        @Override
        public void run() {
            while (count <= instruct.getRetyrCount()) {
                // 隔多久轮询一次
                try {
                    Thread.sleep(instruct.getRetryTimer());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 重新发指令
                DriverManager.getInstance().writeInstruct(instruct);
                count++;
            }

            // 如果执行完之后的重试次数大于给予的重试次数则回调异常
            callback.onError(new RetryTimeOutError());
            // 关闭
            executorService.shutdown();
        }
    }
}
