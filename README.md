# usb转串口

### **依赖 compile 'com.tianfeng:usbdriver:1.0.7'**

### **一、仿okhttp解耦**
1. 初始化
```java
 OkDriveClient client = new OkDriveClient.Builder(App.application)
                .timeOut(60000)// 整个指令从开始到结束响应超时时间
                .intercept()//可以自定义拦截器 比如指令检验等
                .build();
```
2. 发送指令
```java
// 封装指令对象直接发送 指令对象中含有callback
 client.writeInstruct(new Instruct());
```

3. 注意写指令和读取指令是以轮询的方式完成 通过handler发送命令轮询

### **二、图结构**
![  ](https://github.com/TF27674569/UsbDrive/blob/master/driver.png)  
