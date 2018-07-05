package org.usb.driver;

import android.content.Context;

import org.usb.driver.driver.DriverManager;
import org.usb.driver.error.DriverInitError;
import org.usb.driver.interceptor.CrcInterceptor;
import org.usb.driver.interceptor.OutOfTimeIntercept;
import org.usb.driver.interceptor.RealInterceptorChain;
import org.usb.driver.interceptor.RemoveInstructInterceptor;
import org.usb.driver.interceptor.CountInterceptor;
import org.usb.driver.interceptor.WriteDataIntercept;
import org.usb.driver.template.Client;
import org.usb.driver.template.Interceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * Description :
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/23
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class OkUsbClient implements Client {

    private Builder P;

    protected OkUsbClient(Builder builder) {
        this.P = builder;
    }

    /**
     * 对外只提供一个写指令的接口
     *
     * @param instruct 指令封装集合
     */
    @Override
    public void writeInstruct(Instruct instruct) {
        // 1. 串口打开判断
        DriverManager driverManager = DriverManager.getInstance();
        if (!driverManager.isOpen()) {
            driverManager.openDriver(P.context);
        }
        // 2. 根据串口返回值确定是成功还是失败
        if (!driverManager.isOpen()) {
            instruct.getCallback().onError(new DriverInitError());
            return;
        }
        // 3. 成功写数据 instruct 丢给 对应的类处理
        withInterceptorChain(instruct);
    }

    // 调起责任连
    private void withInterceptorChain(Instruct instruct) {

        // 所有拦截器
        List<Interceptor> interceptors = new ArrayList<>();

        // 处理用户自定义的拦截器
        interceptors.addAll(P.interceptors);

        // 添加重试的拦截器
        if (P.isAddCount){
            interceptors.add(new CountInterceptor());
        }

        // 添加移除指令的拦截器
        interceptors.add(new RemoveInstructInterceptor());

        // 添加超时的拦截器
        interceptors.add(new OutOfTimeIntercept(P.timeOut));

        // crc校验添加
        interceptors.add(new CrcInterceptor());

        // 添加发送指令的拦截器
        interceptors.add(new WriteDataIntercept());

        // 创建责任连
        RealInterceptorChain realInterceptorChain = new RealInterceptorChain(interceptors, instruct, 0);

        // 分发任务调起责任连
        realInterceptorChain.proceed(instruct);
    }


    public static class Builder {

        private List<Interceptor> interceptors;
        private Context context;
        // 超时时间默认1秒
        private long timeOut = 1000;

        private boolean isAddCount = true;

        public Builder(Context context) {
            this.context = context.getApplicationContext();
            interceptors = new ArrayList<>();
        }

        public Builder intercept(Interceptor interceptor) {
            interceptors.add(interceptor);
            return this;
        }

        public Builder timeOut(long timeOut) {
            this.timeOut = timeOut;
            return this;
        }

        public Builder isAddCountInterceptor(boolean isAdd) {
            this.isAddCount = isAdd;
            return this;
        }

        public <T extends OkUsbClient> T build() {
            return (T) new OkUsbClient(this);
        }
    }

}
