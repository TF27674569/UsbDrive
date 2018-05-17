package org.usb;

import android.content.Context;

import org.usb.base.Client;
import org.usb.driver.DriverManager;
import org.usb.exception.DriverInitError;
import org.usb.interceptor.Interceptor;
import org.usb.interceptor.OutOfTimeIntercept;
import org.usb.interceptor.RealInterceptorChain;
import org.usb.interceptor.RetryInterceptor;
import org.usb.interceptor.WriteDataIntercept;

import java.util.ArrayList;
import java.util.List;

/**
 * Description : usb发送的实体类
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/17
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class OkDriveClient implements Client {
    public static final String TAG = "OkDrive";

    private Builder P;

    private OkDriveClient(Builder builder) {
        P = builder;
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

        // 添加超时的拦截器
        interceptors.add(new OutOfTimeIntercept(P.timeOut));

        // 添加重试的拦截器
        interceptors.add(new RetryInterceptor());

        // 添加发送指令的拦截器
        interceptors.add(new WriteDataIntercept());

        // 创建责任连
        RealInterceptorChain realInterceptorChain = new RealInterceptorChain(interceptors, 0, instruct);

        // 分发任务调起责任连
        realInterceptorChain.proceed(instruct);
    }


    public static class Builder {

        private List<Interceptor> interceptors;
        private Context context;
        // 超时时间默认20秒
        private long timeOut = 20000;

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

        public OkDriveClient build() {
            return new OkDriveClient(this);
        }
    }
}
