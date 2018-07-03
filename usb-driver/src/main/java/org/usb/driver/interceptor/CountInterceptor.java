package org.usb.driver.interceptor;


import org.usb.driver.Instruct;
import org.usb.driver.error.OutOfTimeError;
import org.usb.driver.template.Callback;
import org.usb.driver.template.CallbackAdapter;
import org.usb.driver.template.Interceptor;

import java.util.List;

/**
 * Description : count 处理拦截器
 * 1. 发送指令 如果回调error 继续发送 动作不变 count + 1  5次都error后回调error
 * 2. 发送指令 成功 回调 判断 如果是 三 发送指令 动作变为 4 回调成功
 * <p>
 * 1. 5次error之内 打断责任连 修改指令 重新生成新的链子 调起链子
 * 2. 回调成功里面处理 对应的
 * <p>
 * Created : TIAN FENG
 * Date : 2018/5/23
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class CountInterceptor implements Interceptor {

    /**
     * 最大发起次数
     */
    private static final int MAX_COUNT = 5;

    /**
     * 拦截操作
     */
    @Override
    public void intercept(Chain chain) {

        // 指令
        Instruct instruct = chain.instruct();

        // 重定义新的拦截器
        List<Interceptor> redefineInterceptors = redefineInterceptor(chain);

        // 修改回调为代理回调
        RetryCallback callback = new RetryCallback(instruct, redefineInterceptors);

        // 代理callback
        instruct.setCallback(callback);

        // 分发
        chain.proceed(instruct);
    }

    /**
     * 重新获取之后的CountInterceptor之后的拦截器
     */
    private List<Interceptor> redefineInterceptor(Chain chain) {

        // 目标拦截器集合
        List<Interceptor> targetInterceptors = chain.interceptors();

        // 拿到自己所在的位置
        int index = targetInterceptors.indexOf(this);

        // 获取自己往后的拦截器集合
        return targetInterceptors.subList(index + 1, targetInterceptors.size());
    }


    /**
     * 无论发送成功与否 都需要一条新的责任连
     * 所以需要一个新的拦截器
     * super的callback处理回调给调用者的数据
     * <p>
     * 1. onSuccess和onError里面修改指令
     * 2. 轮询就已超时的1秒为基准 error 一次 轮训一次 count+1
     * 3. 动作的监听在 onSuccess里面
     */
    class RetryCallback extends CallbackAdapter {

        /**
         * 当前发送的count
         */
        private byte count = 1;

        /**
         * 发送指令
         */
        private Instruct instruct;

        /**
         * 重定向 需要的拦截器集合
         */
        private List<Interceptor> interceptors;

        private RetryCallback(Instruct instruct, List<Interceptor> interceptors) {
            super(instruct);
            this.instruct = instruct;
            // 保证最后回调在这里
            instruct.setCallback(this);
            this.interceptors = interceptors;
        }

        @Override
        public void onSuccess(byte[] result) {
            // 动作角标
            int anctionIndex = result.length - 1 - 6;

            // 动作
            byte action = result[anctionIndex];

            // 数据接收没毛病
            if (action == 3) {
                // 回调成功
                super.onSuccess(result);
                // 还需要发一个4给单片机
                action();
                withInterceptorChain();
            }


        }


        @Override
        public void onError(Throwable throwable) {
            /**
             * 如果是超时且count 在 最大次数之内
             */
            if (throwable instanceof OutOfTimeError && count <= MAX_COUNT) {
                count++;
                count();
                withInterceptorChain();
            } else {
                super.onError(throwable);
            }
        }

        /**
         * 修改动作这里没有加crc校验
         */
        private void action() {
            byte[] send = instruct.getSend();
            send[send.length - 2] = 4;
            instruct.setSend(send);
            instruct.setCallback(Callback.DEFAULE_CALLBACK);
        }

        /**
         * 修改指令 这里没有加crc校验
         */
        private void count() {
            byte[] send = instruct.getSend();
            send[send.length - 1] = count;
            instruct.setSend(send);
        }

        /**
         * 形成新的链
         */
        private void withInterceptorChain() {
            RealInterceptorChain chain = new RealInterceptorChain(interceptors, instruct, 0);
            chain.proceed(instruct);
        }
    }
}
