package com.guogee.smartwatch.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface SleepService {
	
	public boolean addSleep(Object[] params);

	public boolean deleteSleep(Object[] params);

	public boolean updateSleep(Object[] params);

	public List<Map<String, String>> listSleepMaps(String[] seletionArgs);
	
	public LinkedHashMap<String, String> listSelectDaySleepMaps(String[] seletionArgs);
	
	public List<Map<String, String>> listWeekSleepMaps(String[] seletionArgs);
	
	public List<LinkedHashMap<String, String>> listMonthSleepMaps(String[] seletionArgs);
}
