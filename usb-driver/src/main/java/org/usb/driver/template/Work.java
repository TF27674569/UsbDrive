package org.usb.driver.template;


import org.usb.driver.Instruct;

/**
 * Description : 任务操作
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/17
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public interface Work {

    /**
     * 发送一条指令
     */
    void addInstruct(Instruct instruct);


    /**
     * 移除一条指令
     */
    void removeInstruct(Instruct instruct);

    /**
     * 清空所有指令
     */
    void clearnInstructs();

    /**
     * 轮询
     */
    void  polling();
}
