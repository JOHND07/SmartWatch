/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.guogee.smartwatch.utils;

import java.util.HashMap;
import java.util.UUID;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

/**
 * 这个类包含用于演示目的标准关贸总协定的一小部分属性。
 * 		UUID uu=UUID.fromString("00002a29-0000-1000-8000-00805f9b34fb");
		BluetoothGattService bg=mBluetoothGatt.getService(uu);
		UUID uuid=mBluetoothGatt.getServices().get(3).getCharacteristics().get(6).getUuid();
		BluetoothGattCharacteristic c=mBluetoothGatt.getService(UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb")).getCharacteristic(UUID.fromString("00002a29-0000-1000-8000-00805f9b34fb"));
		
		if(c!=null){
			Log.i(TAG,"相等");
		}
		
 */
public class SampleGattAttributes {
    private static HashMap<String, String> attributes = new HashMap();
    public static String HEART_RATE_MEASUREMENT = "0000C004-0000-1000-8000-00805f9b34fb";
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    public static String SERVICE_UUID = "00000a60-0000-1000-8000-00805f9b34fb";
    										   //0000fff6-0000-1000-8000-00805f9b34fb
    public static String GATCHARACTERISTIC_UUID="00000a66-0000-1000-8000-00805f9b34fb"; 
    public static String RED_UUID="00000a67-0000-1000-8000-00805f9b34fb";
    static {
        // 样品服务。
        attributes.put("00000a600000-1000-8000-00805f9b34fb", "Heart Rate Service");
        attributes.put("0000180a-0000-1000-8000-00805f9b34fb", "Device Information Service");
        //样本特征。
        attributes.put(HEART_RATE_MEASUREMENT, "Heart Rate Measurement");
        attributes.put("00002a29-0000-1000-8000-00805f9b34fb", "Manufacturer Name String");
    }
    
    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}

 