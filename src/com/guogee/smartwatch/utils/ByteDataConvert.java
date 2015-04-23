package com.guogee.smartwatch.utils;

public class ByteDataConvert {
	public static void BinnCat(byte[] from, byte to, int offset, int len) {
		int max = offset + len;
		int min = offset;
		for (int i = min, j = 0; i < max; i++, j++) {
			to = from[j];
		}
	}
	/***
	 * longת��Ϊbyte����
	 * @param from
	 * @param len
	 * @return
	 */
	public static byte[] LongToBin( long from, int len )
	{
	byte[] to = new byte[len];
	int max = len;

	for( int i_move = max - 1, i_to = 0; i_move >= 0; i_move--, i_to++ )
	{
	to[i_to] = (byte)( from >> ( 8 * i_move ) );
	}

	return to;
	}
	/***
	 * ��longת����Byte �ڼ������೤
	 * @param from
	 * @param to
	 * @param offset
	 * @param len
	 */
	public static void LongToBin( long from, byte[] to, int offset, int len )
	{
	int max = len;
	int min = offset;

	for( int i_move = max - 1, i_to = min; i_move >= 0; i_move--, i_to++ )
	{
	to[i_to] = (byte)( from >> ( 8 * i_move ) );
	}
	}
	/***
	 * int��ת����byte����
	 * @param from
	 * @param len
	 * @return
	 */
	public static byte[] IntToBin( int from, int len )
	{
	byte[] to = new byte[len];
	int max = len;

	for( int i_move = max - 1, i_to = 0; i_move >= 0; i_move--, i_to++ )
	{
	to[i_to] = (byte)( from >> ( 8 * i_move ) );
	}

	return to;
	}
	
	/**
	 * int��ת����byte����  (��λ��ǰ����λ�ں�)
	 * @param from
	 * @param len
	 * @return
	 */
	public static byte[] IntToBinByLow( int from, byte[] to, int offset, int len )
	{
		int max = len;
		int min = offset;

		for( int i_move = max - 1, i_to = min; i_move >= 0; i_move--, i_to++ )
		{
		to[i_to] = (byte)( from >> ( 8 * (len - 1 - i_move) ) & 0xff);
		}

		return to;
	}
	
	
	/***
	 * ת��int���浽byte�ĵڼ�����
	 * @param from
	 * @param to
	 * @param offset
	 * @param len
	 * @return
	 */
	public static byte[] IntToBin( int from, byte[] to, int offset, int len )
	{
	int max = len;
	int min = offset;

	for( int i_move = max - 1, i_to = min; i_move >= 0; i_move--, i_to++ )
	{
	to[i_to] = (byte)( from >> ( 8 * i_move ) );
	}

	return to;
	}
	/***
	 * byte���ݵĵڼ�������ת��Ϊlong
	 * @param from
	 * @param offset
	 * @param len
	 * @return
	 */
	public static long BinToLong( byte[] from, int offset, int len )
	{
	long to;
	int min = offset;
	to = 0;
	for( int i_move = len - 1, i_from = min; i_move >= 0; i_move--, i_from++ )
	{
	to = to << 8 | ( from[i_from] & 0xff );
	}
	return to;
	}
/***
 * ��byte�ĵڼ�������ת����int
 * @param from
 * @param offset
 * @param len
 * @return
 */
	public static int BinToInt( byte[] from, int offset, int len )
	{
	int to = 0;
	int min = offset;
	to = 0;

	for( int i_move = len - 1, i_from = min; i_move >= 0; i_move--, i_from++ )
	{
	to = to << 8 | ( from[i_from] & 0xff );
	}
	return to;
	}
	
	/***
	 * ��byte�ĵڼ�������ת����int by Low
	 * @param from
	 * @param offset
	 * @param len
	 * @return
	 */
		public static int BinToIntByLow( byte[] from, int offset, int len )
		{
		int to = 0;

		int i_from = offset;
		to = 0;

		for( int i_move = len - 1; i_move >= 0; i_move--)
		{
		to = to << 8 | ( from[i_from + i_move] & 0xff );
		}
		return to;
		}
	
	
	/***
	 * ��MAC��ַת��Ϊbyte����
	 * @param mac
	 * @return
	 */
	public static byte [] getMacBytes(String mac){
		  byte []macBytes = new byte[6];
		  String [] strArr = mac.split(":");
		  
		  for(int i = 0;i < strArr.length; i++){
		   int value = Integer.parseInt(strArr[i],16);
		   macBytes[i] = (byte) value;
		  }
		  return macBytes;
	}
	
	public static String getStrBytes(byte[] data,int offset,int len){
		if(data.length<(offset+len))return null;
		String str="";
		for(int i=0;i<len;i++){
			str +=String.format("%02X",data[offset+i]);
		}
		return str;
	}
	
	//add by john
	public static byte getByteByArray(byte[] data){
		
		byte wk = 0x00;
	    for (int j=0; j<8; j++) {
	        if (data[j] != 0) {//�ظ�
	        	wk = (byte) (wk | (0x01 << j));
	        }
	    }

	    return wk;
	}
	
	
}
