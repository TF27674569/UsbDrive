package org.usb.driver.interceptor;

import org.usb.driver.Instruct;
import org.usb.driver.template.CallbackAdapter;
import org.usb.driver.template.Interceptor;
import org.usb.driver.utils.Utils;

/**
 * Description : 握手拦截器,规定返回数据与发送数据一模一样时为握手
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/23
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class HandshakeInterceptor implements Interceptor {
    /**
     * 拦截操作
     */
    @Override
    public void intercept(Chain chain) {

        // 获取当前指令
        Instruct instruct = chain.instruct();

        // 修改回调为代理回调
        instruct.setCallback(new HandshakeCallback(instruct));

        // 继续分发
        chain.proceed(instruct);
    }


    /**
     * 握手代理回调
     */
    class HandshakeCallback extends CallbackAdapter {

        HandshakeCallback(Instruct instruct) {
            super(instruct);
        }

        @Override
        public void onSuccess(byte[] result) {
            //  判断是否是握手的数据,不是握手则回调
            if (!Utils.beEqualTo(result, send)) {
                super.onSuccess(result);
            }
        }
    }
}
