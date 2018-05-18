package org.usb;

import org.usb.base.Callback;

/**
 * Description : 指令封装对象
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/17
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class Instruct {

    /**
     * 真实发送指令
     */
    private byte[] send;

    /**
     * 拦截的指令
     */
    private byte[] intercept;

    /**
     * 回调 防止null指针
     */
    private Callback callback = Callback.DEFAULE_CALLBACK;

    /**
     * 重试次数
     */
    private int retyrCount = 5;

    /**
     * 重试时间
     */
    private long retryTimer = 2000;

    public Instruct() {
    }

    public Instruct(byte[] send) {
        this.send = send;
        this.intercept = send;
    }

    public Instruct(byte[] send, byte[] intercept) {
        this.send = send;
        this.intercept = intercept;
    }


    public Instruct(byte[] send, byte[] intercept, int retyrCount, long retryTimer, Callback callback) {
        this.send = send;
        this.intercept = intercept;
        this.callback = callback;
        this.retyrCount = retyrCount;
        this.retryTimer = retryTimer;
    }

    public Instruct(byte[] send, byte[] intercept, Callback callback) {
        this.send = send;
        this.intercept = intercept;
        this.callback = callback;
    }

    public byte[] getSend() {
        return send;
    }

    public void setSend(byte[] send) {
        this.send = send;
    }

    public byte[] getIntercept() {
        return intercept;
    }

    public void setIntercept(byte[] intercept) {
        this.intercept = intercept;
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public int getRetyrCount() {
        return retyrCount;
    }

    public void setRetyrCount(int retyrCount) {
        this.retyrCount = retyrCount;
    }

    public long getRetryTimer() {
        return retryTimer;
    }

    public void setRetryTimer(long retryTimer) {
        this.retryTimer = retryTimer;
    }
}
