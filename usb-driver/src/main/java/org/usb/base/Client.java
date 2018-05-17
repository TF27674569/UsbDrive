package org.usb.base;

import org.usb.Instruct;

/**
 * Description : 对外提供的接口
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/17
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public interface Client {

    /**
     * 对外只提供一个写指令的接口
     *
     * @param instruct 指令封装集合
     */
    void writeInstruct(Instruct instruct);

}
