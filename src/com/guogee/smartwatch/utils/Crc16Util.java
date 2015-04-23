package com.guogee.smartwatch.utils;


/**
 * @author fly
 *
 *2014年1月2日
 * 
 */
public class Crc16Util {
	public static  boolean checkCrc(byte[] data,int crccount){
		
		if(crc16(data,data.length-crccount)==ByteDataConvert.BinToInt(data, data.length-crccount, crccount)){
			return true;
		}
		return false;
	}
	public static int crc16(byte[] buf, int length)
	{
		int i,j,c;
		int crc = 0xFFFF;
		for (i = 0; i < length; i++) {
			c = buf[i] & 0x00FF;
			crc ^= c;
			for (j = 0; j < 8; j++) {
				if ((crc & 0x0001) != 0) {
					crc >>= 1;
					crc ^= 0xA001;
				} else {
					crc >>= 1;
				}
			}
		}
		crc = (crc >> 8) + (crc << 8);
		return (short)crc;
	}
	
	public static byte[] addCrc(byte[] value,int digit){
		int crcint=crc16(value, value.length-digit);
		byte[] crcbyte=ByteDataConvert.IntToBin(crcint, digit);
		 System.arraycopy(crcbyte, 0, value, value.length-digit, digit);
		 return value;
	}
}
