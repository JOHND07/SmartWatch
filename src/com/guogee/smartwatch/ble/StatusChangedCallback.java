package com.guogee.smartwatch.ble;


/**
 * �豸״̬�ı�ص��ӿ�
 * 
 *
 */
public interface StatusChangedCallback {
    void callbackCall(int msgBleConnected, byte[] dataPacket); 	
    void callbackFail(int command, byte[] dataPacket); 
}