package com.guogee.smartwatch.ble;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class BLEMessageQueueManagement {

	/*
	 * ble ���ݰ�����
	 */
	private ConcurrentMap<Integer, byte[]> BLEPacketMap;
	
	public BLEMessageQueueManagement(){
		BLEPacketMap  		= new ConcurrentHashMap<Integer, byte[]>();
	}
	
	/**
	 * �������ݰ�  
	 * @param seq
	 * @param packet
	 */
	public void addUDPPacket(int command, byte[] packet){
		BLEPacketMap.put(command, packet);
	}

	/**
	 * ������ݰ�
	 * @param seq
	 * @return
	 */
	public byte[] getUDPPacket(int command){
		return BLEPacketMap.get(command);
	}
	
	/**
	 * ɾ�����ݰ�
	 * @param seq
	 * @return
	 */
	public boolean removeUDPPacket(int command){
		BLEPacketMap.remove(command);
		return true;
	}
	
	/**
	 *  �����ݰ�������CALLBACK
	 */
	public void clearUDPPacket(){
		BLEPacketMap.clear();
	}
	
	/**
	 * ����
	 */
	public void onDestroy(){
		BLEPacketMap.clear();
	}
	
	
}
