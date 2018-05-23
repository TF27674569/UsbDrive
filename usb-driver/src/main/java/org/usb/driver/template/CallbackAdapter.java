package org.usb.driver.template;

import org.usb.driver.Instruct;

/**
 * Description :
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/23
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public abstract class CallbackAdapter implements Callback {

    /**
     * 回调
     */
    protected Callback callback;

    /**
     * 真实发送指令
     */
    protected byte[] send;

    /**
     * 拦截的指令
     */
    protected byte[] intercept;

    /**
     * 重试次数
     */
    protected int retyrCount;

    /**
     * 重试时间
     */
    protected long retryTimer;


    protected CallbackAdapter(Instruct instruct) {
        callback = instruct.getCallback();
        send = instruct.getSend();
        intercept = instruct.getIntercept();
        retyrCount = instruct.getRetyrCount();
        retryTimer = instruct.getRetryTimer();
    }

    /**
     * 成功
     *
     * @param result 回调指令
     */
    @Override
    public void onSuccess(byte[] result) {
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
}
