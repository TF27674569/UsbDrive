package org.usb.driver.template;

import android.content.Context;

import org.usb.driver.Instruct;

import cn.wch.ch34xuartdriver.CH34xUARTDriver;


/**
 * Description : 驱动抽象层
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/17
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public interface Manager {

    /**
     * 打开驱动
     *
     * @return 是否打开成功
     */
    boolean openDriver(Context context);


    /**
     * 是否已经打开
     */
    boolean isOpen();


    /**
     * 往串口写入数据
     *
     * @param instruct 数据封装对象
     */
    void writeInstruct(Instruct instruct);

    /**
     * usb转串口驱动
     */
    CH34xUARTDriver driver();


    /**
     * 移除数据
     *
     * @param instruct 数据封装对象
     */
    void removeInstruct(Instruct instruct);

    /**
     * 清空池中的对象
     */
    void clearnInstructs();
}
