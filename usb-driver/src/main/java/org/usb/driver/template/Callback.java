package org.usb.driver.template;

/**
 * Description : 回调
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/23
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public interface Callback {
    /**
     * 成功
     *
     * @param result 回调指令
     */
    void onSuccess(byte[] result);

    /**
     * 失败
     *
     * @param throwable 失败原因
     */
    void onError(Throwable throwable);

    Callback DEFAULE_CALLBACK = new Callback() {
        @Override
        public void onSuccess(byte[] result) {

        }

        @Override
        public void onError(Throwable msg) {

        }
    };
}
