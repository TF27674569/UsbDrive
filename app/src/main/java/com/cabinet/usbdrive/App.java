package com.cabinet.usbdrive;

import android.app.Application;

/**
 * Description :
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/17
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class App extends Application{
    public static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }
}
