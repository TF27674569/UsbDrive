package org.usb.driver.interceptor;


import org.usb.driver.Instruct;
import org.usb.driver.error.OutOfTimeError;
import org.usb.driver.template.Callback;
import org.usb.driver.template.CallbackAdapter;
import org.usb.driver.template.Interceptor;

import java.util.Arrays;
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
    private int mMaxCount;
    /**
     * 当前发送的count
     */
    private byte mCurrentCount = 1;

    /**
     * 与外部联系的callback
     * 防止打断链接之后回调停止在内部CountCallback里面
     * 从而结果不能回调
     */
    private Callback mSuperCallback;

    /**
     * 拦截操作
     */
    @Override
    public void intercept(Chain chain) {

        // 指令
        Instruct instruct = chain.instruct();

        // 重定义新的拦截器
        List<Interceptor> redefineInterceptors = redefineInterceptor(chain);

        // 链接外部的callback
        // 打断链接之后在分发就不赋值了
        // 表示第一次进
        if (mSuperCallback == null) {
            mSuperCallback = instruct.getCallback();
            // 第一次进来没有进入crc 直接改count值 第一次肯定是1
            instruct.getSend()[3] = mCurrentCount;
            // zui大count次数拿一次就够了
            mMaxCount = instruct.getRetyrCount();
        }

        // 修改回调为代理回调
        CountCallback callback = new CountCallback(instruct, redefineInterceptors);

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
        return targetInterceptors.subList(index, targetInterceptors.size());
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
    class CountCallback extends CallbackAdapter {

        /**
         * 发送指令
         */
        private Instruct instruct;

        /**
         * 重定向 需要的拦截器集合
         */
        private List<Interceptor> interceptors;

        private CountCallback(Instruct instruct, List<Interceptor> interceptors) {
            super(instruct);
            this.instruct = instruct;
            // 保证最后回调在这里
            this.instruct.setCallback(this);
            this.interceptors = interceptors;
        }

        @Override
        public void onSuccess(byte[] result) {
            // 动作角标
            int anctionIndex = result.length - 6;

            // 动作
            byte action = result[anctionIndex];

            switch (action) {
                // 握手成功
                // 但是此时已经没有回调了 在链子里面被移除掉了
                case 2:
                    // 指令置为null 单纯拦截
                    instruct.setSend(null);
                    // 重新调起责任连
                    withInterceptorChain();
                    break;
                // 数据接收没毛病
                case 3:
                    // 回调成功
                    mSuperCallback.onSuccess(result);
                    // 还需要发一个4给单片机
                    action();
                    withInterceptorChain();
                    break;
            }
        }


        @Override
        public void onError(Throwable throwable) {
            /**
             * 如果是超时且count 在 最大次数之内
             */
            if (throwable instanceof OutOfTimeError && mCurrentCount < mMaxCount) {
                mCurrentCount++;
                count();
                withInterceptorChain();
            } else {
                mSuperCallback.onError(throwable);
            }
        }

        /**
         * 修改动作这里没有加crc校验
         */
        private void action() {
            // 发送之后的指令被crc拦截器 加了两位校验码
            byte[] send = Arrays.copyOfRange(instruct.getSend(), 0, instruct.getSend().length - 2);
            // 还有包尾两个字节  count一个字节
            send[send.length - 4] = 4;
            instruct.setSend(send);
            instruct.setCallback(Callback.DEFAULE_CALLBACK);
        }

        /**
         * 修改指令 这里没有加crc校验
         */
        private void count() {
            // 发送之后的指令被crc拦截器 加了两位校验码
            byte[] send = Arrays.copyOfRange(instruct.getSend(), 0, instruct.getSend().length - 2);
            // 还有包尾两个字节
            send[send.length - 3] = mCurrentCount;
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
