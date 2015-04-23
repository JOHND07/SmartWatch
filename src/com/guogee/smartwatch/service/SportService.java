package com.guogee.smartwatch.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface SportService {

	public boolean addStep(Object[] params);

	public boolean deleteStep(Object[] params);

	public boolean updateStep(Object[] params);
	
	public boolean updateStepTarget(Object[] params);

//	public List<Map<String, String>> listStepMaps(String[] seletionArgs);
	
	public List<LinkedHashMap<String, String>> listStepMaps(String[] seletionArgs);
	
	public List<LinkedHashMap<String, String>> listTodayStepMaps(String[] seletionArgs);
	
	public List<LinkedHashMap<String, String>> listWeekStepMaps(String[] seletionArgs);

	public List<LinkedHashMap<String, String>> listMonthStepMaps(String[] seletionArgs);
	
	public Map<String, String> listStepFromDays(String[] seletionArgs);

}
