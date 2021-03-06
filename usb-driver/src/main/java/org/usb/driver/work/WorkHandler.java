package org.usb.driver.work;

import android.os.Handler;
import android.os.Message;

import org.usb.driver.Instruct;
import org.usb.driver.config.WorkConfig;
import org.usb.driver.template.Work;

/**
 * Description : 保证读写任务只在这一线程中执行
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/17
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class WorkHandler extends Handler implements Work {


    WorkHandler() {
        super(new WorkDispatcher());
    }

    /**
     * 发送一条指令
     */
    @Override
    public void addInstruct(Instruct instruct) {
        sendMessage(instruct, WorkConfig.ADD);
    }

    /**
     * 移除一条指令
     */
    @Override
    public void removeInstruct(Instruct instruct) {
        sendMessage(instruct, WorkConfig.REMOVE);
    }

    /**
     * 清空所有指令
     */
    @Override
    public void clearnInstructs() {
        sendMessage(null, WorkConfig.CLEARN);
    }

    /**
     * 轮询
     */
    @Override
    public void polling() {
        Message message = Message.obtain();
        message.obj = this;
        message.what = WorkConfig.POLLING;
        sendMessageDelayed(message, WorkConfig.POLLING_TIME);
    }

    /**
     * 发送信息
     */
    private void sendMessage(Instruct instruct, int what) {
        Message obtain = Message.obtain();
        obtain.obj = instruct;
        obtain.what = what;
        sendMessage(obtain);
    }

}
