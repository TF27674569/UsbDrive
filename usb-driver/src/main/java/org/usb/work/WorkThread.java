package org.usb.work;

import android.os.Looper;
import android.os.Message;

import org.usb.config.WorkConfig;

/**
 * Description : 工作线程一经开启，不会停止
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/17
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public final class WorkThread extends Thread {
    private WorkHandler workHandler;

    public WorkThread() {
        super("work");
    }

    @Override
    public void run() {
        Looper.prepare();
        workHandler = new WorkHandler();
        workHandler.polling();
        Looper.loop();
    }

    public WorkHandler handler(){
        return workHandler;
    }
}
