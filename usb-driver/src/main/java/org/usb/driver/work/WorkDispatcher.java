package org.usb.driver.work;

import android.os.Handler;
import android.os.Message;

import org.usb.driver.Instruct;
import org.usb.driver.config.WorkConfig;
import org.usb.driver.driver.DriverManager;
import org.usb.driver.utils.CRC16X25Util;
import org.usb.driver.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.usb.driver.config.InstructConfig.ADDRESS_SIZE;
import static org.usb.driver.config.InstructConfig.DATA_LENGTH;
import static org.usb.driver.config.InstructConfig.END_SIZE;
import static org.usb.driver.config.InstructConfig.FUNCTION_CODE_SIZE;
import static org.usb.driver.config.InstructConfig.HEAD_SIZE;
import static org.usb.driver.config.InstructConfig.LENGTH_LENGTH;
import static org.usb.driver.config.InstructConfig.LOG_LENGTH;


/**
 * Description : 指令分发
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/17
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class WorkDispatcher implements Handler.Callback {

    //数据长度位置                    地址 功能码  日志 长度 数据 动作 count
    private static final int DATA_LENGTH_INDEX = HEAD_SIZE + ADDRESS_SIZE + FUNCTION_CODE_SIZE + LOG_LENGTH;
    //数据的基础长度值
    private static final int PACKAGE_BASE_LENGTH = DATA_LENGTH_INDEX + DATA_LENGTH + END_SIZE;
    private static final int READ_BYTE_SIZE = 100;

    // 每次读取的指令
    private static byte[] sInstructBuffer = new byte[READ_BYTE_SIZE];
    // 清空 sInstructBuffer 每次擦除的值
    private static byte sWipe = 0;

    // 指令集合
    private static List<Instruct> sInstruct = new ArrayList<>();

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case WorkConfig.ADD:
                add(msg);
                break;
            case WorkConfig.REMOVE:
                sInstruct.remove((Instruct) msg.obj);
                break;
            case WorkConfig.CLEARN:
                sInstruct.clear();
                break;
            case WorkConfig.POLLING:
                // 解析信息
                pooling(msg);
                break;
        }
        return false;
    }


    /**
     * 添加
     */
    private void add(Message msg) {
        Instruct obj = (Instruct) msg.obj;
        if (!sInstruct.contains(obj)) {
            sInstruct.add(obj);
        }
        // 发送指令
        byte[] sendInstruct = obj.getSend();
        // 拿到发送的指令
        int real = DriverManager.getInstance().driver().WriteData(sendInstruct, sendInstruct.length);

        // 失败
        if (real <= 0) {
            // 发送失败
            Utils.printHex(sendInstruct, "发送数据(十六进制)失败:");
            // 失败回调
            obj.getCallback().onError(new RuntimeException("写入单片机失败"));
            return;
        }

        Utils.printHex(sendInstruct, "发送数据(十六进制)成功:");
    }


    //每一次读取多留出来的数据整备下一次整合 在判断
    private static byte[] sPartData;

    /**
     * 轮询读数据
     */
    private void pooling(Message msg) {
        // 擦除缓冲的值
        Arrays.fill(sInstructBuffer, sWipe);
        // 读取长度
        int length = DriverManager.getInstance().driver().ReadData(sInstructBuffer, READ_BYTE_SIZE);
        if (length > 0) {
            //截取数组有效长度
            sInstructBuffer = Arrays.copyOfRange(sInstructBuffer, 0, length);
            if (sInstructBuffer.length > PACKAGE_BASE_LENGTH) {
                if (sPartData != null) {
                    sInstructBuffer = CRC16X25Util.concatAll(sPartData, sInstructBuffer);
                    sPartData = null;
                }
                handleInstruct(sInstructBuffer);
            }
        }
        // 接着轮询遍历
        WorkHandler handler = (WorkHandler) msg.obj;
        handler.polling();
    }


    /**
     * 解析指令
     */
    private void handleInstruct(byte[] instruct) {

        Utils.printHex(instruct, "接收到的数据:");

        // 不是一个合法的包
        if (instruct.length < PACKAGE_BASE_LENGTH) return;

        // 表示数据有问题 包头不对
        if (instruct[0] != -1 || instruct[1] != -2) {
            // 舍弃第一位
            instruct = Arrays.copyOfRange(instruct, 1, instruct.length);
            // 重新判断
            handleInstruct(instruct);
        }

        byte[] len = Arrays.copyOfRange(instruct, DATA_LENGTH_INDEX, DATA_LENGTH_INDEX + DATA_LENGTH);
        //补成四个字节的数据
        byte[] int_len = new byte[]{0, 0, 0, len[0]};

        // 拿到数据的长
        int dataLength = Utils.byteArrayToInt(int_len);

        //如果这组数据等于不够一个包长 无效
        if (dataLength < PACKAGE_BASE_LENGTH) {
            // 拼接给下次用
            sPartData = instruct;
            return;
        }

        // 加上crc校验的两位
        dataLength += 2;

        // 截取此包
        byte[] packageData = Arrays.copyOfRange(instruct, 0, dataLength);

        // 判断包尾
        if (packageData[dataLength - 3] != -1 || packageData[dataLength - 4] != -2) {
            // 包尾不对拼接上来后下次重新执行
            sPartData = instruct;
            return;
        }

        // 数据正常的情况下
        //CRC校验部分
        boolean passCRC = CRC16X25Util.isPassCRC(packageData, dataLength);

        // 成功
        if (passCRC) {
            circulationData(packageData);//校验通过
        }
        Utils.printHex(packageData, "校验数据:" + passCRC);

        // 表示还够一个包
        if (instruct.length > dataLength + PACKAGE_BASE_LENGTH) {
            instruct = Arrays.copyOfRange(instruct, dataLength, instruct.length);
            // 进行下一个包的处理
            handleInstruct(instruct);
        }

        // 不够一个包
        sPartData = instruct;
    }

    /**
     * 处理校验通过的数据
     */
    private void circulationData(byte[] packageData) {
        for (Instruct instruct : sInstruct) {
            if (Utils.equals(instruct.getIntercept(), packageData)) {
                instruct.getCallback().onSuccess(packageData);
                break;
            }
        }
    }
}
