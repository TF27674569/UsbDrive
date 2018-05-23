package org.usb.driver.interceptor;

import org.usb.driver.Instruct;
import org.usb.driver.template.Interceptor;

import java.util.List;

/**
 * Description : 真正调起责任链
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/23
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class RealInterceptorChain implements Interceptor.Chain {

    private List<Interceptor> interceptors;
    private Instruct instruct;
    private int index;

    public RealInterceptorChain(List<Interceptor> interceptors, Instruct instruct, int index) {
        this.interceptors = interceptors;
        this.instruct = instruct;
        this.index = index;
    }


    /**
     * 获取任务
     */
    @Override
    public Instruct instruct() {
        return instruct;
    }

    /**
     * 分发操作
     */
    @Override
    public void proceed(Instruct instruct) {

        // 已经执行了最后一个任务后 就停止
        if (index>=interceptors.size()) return;

        // 获取当前需要分发的任务
        Interceptor interceptor = interceptors.get(index);

        // 角标+1
        index++;

        // 调起责任连
        interceptor.intercept(this);
    }
}
