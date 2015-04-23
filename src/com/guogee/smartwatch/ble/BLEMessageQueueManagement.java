package com.guogee.smartwatch.ble;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class BLEMessageQueueManagement {

	/*
	 * ble 数据包队列
	 */
	private ConcurrentMap<Integer, byte[]> BLEPacketMap;
	
	public BLEMessageQueueManagement(){
		BLEPacketMap  		= new ConcurrentHashMap<Integer, byte[]>();
	}
	
	/**
	 * 增加数据包  
	 * @param seq
	 * @param packet
	 */
	public void addUDPPacket(int command, byte[] packet){
		BLEPacketMap.put(command, packet);
	}

	/**
	 * 获得数据包
	 * @param seq
	 * @return
	 */
	public byte[] getUDPPacket(int command){
		return BLEPacketMap.get(command);
	}
	
	/**
	 * 删除数据包
	 * @param seq
	 * @return
	 */
	public boolean removeUDPPacket(int command){
		BLEPacketMap.remove(command);
		return true;
	}
	
	/**
	 *  清数据包，不清CALLBACK
	 */
	public void clearUDPPacket(){
		BLEPacketMap.clear();
	}
	
	/**
	 * 销毁
	 */
	public void onDestroy(){
		BLEPacketMap.clear();
	}
	
	
}
