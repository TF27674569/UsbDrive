package org.usb.interceptor;

import org.usb.Instruct;

import java.util.List;

/**
 * Description : 真实的拦截链 thanks for okhttp
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/17
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class RealInterceptorChain implements Interceptor.Chain {

    private int index;
    private List<Interceptor> interceptors;
    private Instruct instruct;

    public RealInterceptorChain(List<Interceptor> interceptors, int index,Instruct instruct) {
        this.index = index;
        this.interceptors = interceptors;
        this.instruct = instruct;
    }


    @Override
    public Instruct instruct() {
        return instruct;
    }

    @Override
    public void proceed(Instruct instruct) {
        // 到最后一个拦截器就不分发了
        if (index >= interceptors.size()) return;

        // 分发任务
        interceptors.get(index).intercept(this);

        // 任务角标++
        index++;
    }
}
