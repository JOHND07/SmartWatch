/*
 *  Copyright 2010 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.guogee.smartwatch.widget;

import java.util.ArrayList;


/**
 * Numeric Wheel adapter.
 */
public class NumericWheelAdapter implements WheelAdapter {
	
	/** The default min value */
	public static final int DEFAULT_MAX_VALUE = 9;

	/** The default max value */
	private static final int DEFAULT_MIN_VALUE = 0;
	
	// Values
	private int minValue;
	private int maxValue;
	
	// format
	private String format;
	
	/**
	 * Default constructor
	 */
	public NumericWheelAdapter() {
		this(DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE);
	}
	
	private ArrayList<Integer> arryDatas;
	private ArrayList<String> arryStrDatas;
	
	public NumericWheelAdapter(ArrayList<Integer> arrys) {
		arryDatas = arrys;
	}
	
	public void setListStr(ArrayList<String> strDatas){
		arryStrDatas = strDatas;
	}
	
//	public NumericWheelAdapter(ArrayList<String> arrayStr){
//		arryStrDatas = arrayStr;
//	}
	

	/**
	 * Constructor
	 * @param minValue the wheel min value
	 * @param maxValue the wheel max value
	 */
	public NumericWheelAdapter(int minValue, int maxValue) {
		this(minValue, maxValue, null);
	}

	/**
	 * Constructor
	 * @param minValue the wheel min value
	 * @param maxValue the wheel max value
	 * @param format the format string
	 */
	public NumericWheelAdapter(int minValue, int maxValue, String format) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.format = format;
	}

	@Override
	public String getItem(int index) {
		if(arryDatas!=null){
			return Integer.toString(arryDatas.get(index));
		}else if(arryStrDatas!=null){
			return arryStrDatas.get(index);
		}
		else if (index >= 0 && index < getItemsCount()) {
			int value = minValue + index;
			return format != null ? String.format(format, value) : Integer.toString(value);
		}
		return null;
	}

	@Override
	public int getItemsCount() {
		if(arryDatas!=null){
			return arryDatas.size();
		}else if(arryStrDatas!=null){
			return arryStrDatas.size();
		}
		else{
			return maxValue - minValue + 1;	
		}
		
	}
	
	@Override
	public int getMaximumLength() {
		int maxLen;
		if(arryDatas!=null){
			int max = Math.max(Math.abs(arryDatas.get(arryDatas.size()-1)), Math.abs(arryDatas.get(0)));
			maxLen = Integer.toString(max).length();
			if (arryDatas.get(0) < 0) {
				maxLen++;
			}
		}else{
			int max = Math.max(Math.abs(maxValue), Math.abs(minValue));
			maxLen = Integer.toString(max).length();
			if (minValue < 0) {
				maxLen++;
			}
		}
		return maxLen;	
		
		
	}
}
