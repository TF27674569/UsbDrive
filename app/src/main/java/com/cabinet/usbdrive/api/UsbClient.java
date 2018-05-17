package com.cabinet.usbdrive.api;

import com.cabinet.usbdrive.App;

import org.driver.UsbRetorfit;
import org.driver.adapter.RxJava2CallAdapter;
import org.usb.OkDriveClient;

/**
 * Description :
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/17
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class UsbClient {

    private static final UsbApi sUsbApi;

    static {

        OkDriveClient client = new OkDriveClient.Builder(App.application)
                .timeOut(60000)
                .build();

        UsbRetorfit usbRetorfit = new UsbRetorfit.Builder()
                .addCallAdapter(RxJava2CallAdapter.create())
                .client(client)
                .build();

        sUsbApi = usbRetorfit.create(UsbApi.class);
    }


    public static UsbApi getUsb() {
        return sUsbApi;
    }
}
