package com.cabinet.usbdrive.api;

import android.content.Context;

import com.cabinet.usbdrive.App;


import org.usb.driver.OkUsbClient;
import org.usb.retorfit.UsbRetorfit;
import org.usb.retorfit.factory.OkUsbDriver;
import org.usb.retorfit.factory.RxJava2CallAdapterFactory;

/**
 * Description :
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/17
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class UsbClient {

    private static  UsbApi sUsbApi;

   public static void init(Context context){
       OkUsbDriver client = new OkUsbDriver.Builder(context)
               .build();

       UsbRetorfit usbRetorfit = new UsbRetorfit.Builder()
               .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
               .addUsbDriver(client)
               .build();

       sUsbApi = usbRetorfit.create(UsbApi.class);
   }


    public static UsbApi getUsb() {
        return sUsbApi;
    }
}
