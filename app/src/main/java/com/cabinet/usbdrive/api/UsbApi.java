package com.cabinet.usbdrive.api;


import org.usb.retorfit.annotation.Adress;
import org.usb.retorfit.annotation.End;
import org.usb.retorfit.annotation.Fun;
import org.usb.retorfit.annotation.Head;
import org.usb.retorfit.annotation.Intercept;
import org.usb.retorfit.annotation.Log;
import org.usb.retorfit.annotation.Retry;

import io.reactivex.Observable;


/**
 * Description :
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/17
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public interface UsbApi {

    /**
     * 发送指令后 拦截新的指令
     */
    @Head(0xfffe)
    @Adress(0x05)
    @Fun(0x06)
    @Log(Log.Logger.ON)
    @Retry(value = 0x1,time = 3000)
    @End(0xfeff)
    @Intercept({-1,-2,10,10,9,10,10,-2,-1})
    Observable<byte[]> check();
}
