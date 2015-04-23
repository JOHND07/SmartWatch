package com.guogee.smartwatch.ble;


/**
 * 设备状态改变回调接口
 * 
 *
 */
public interface StatusChangedCallback {
    void callbackCall(int msgBleConnected, byte[] dataPacket); 	
    void callbackFail(int command, byte[] dataPacket); 
}