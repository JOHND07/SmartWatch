package com.guogee.smartwatch.service;

import java.util.List;
import java.util.Map;

public interface BindDeviceService {

	public boolean addBleDevice(Object[] params);

	public boolean deleteBleDevice(Object[] params);

	public boolean updateBleDevice(Object[] params);
	
	public List<Map<String, String>> listBleDevice(String[] seletionArgs);
	
//	public List<Map<String, String>> listBleDeviceByAddress(String[] seletionArgs);
}
