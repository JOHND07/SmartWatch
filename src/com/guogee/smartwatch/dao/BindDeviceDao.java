package com.guogee.smartwatch.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.guogee.smartwatch.db.DbHelper;
import com.guogee.smartwatch.service.BindDeviceService;

public class BindDeviceDao implements BindDeviceService{

    private DbHelper helper = null;
	
	public BindDeviceDao(Context context)
	{
		helper = new DbHelper(context);
	}
	
	@Override
	public boolean addBleDevice(Object[] params) {
		// TODO Auto-generated method stub
		boolean flag = false;
		SQLiteDatabase database = null;
		try{
//			String sq3 = "CREATE TABLE IF NOT EXISTS binding_device ( " +
//					"id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
//					"device_name TEXT, " +
//					"mac_address TEXT, " +
//					"company_code TEXT, " +
//					"orders INTEGER NOT NULL)";
			
			String sql = "insert into binding_device (device_name,mac_address,company_code, orders) values (?,?,?,?)";
			database = helper.getWritableDatabase();
			database.execSQL(sql, params);
		}catch(Exception e){
			Log.d("TAG", "addBleDevice error .................................... ");
			e.printStackTrace();
		}
		finally{
			if (database != null)
			{
				database.close();
			}
		}
		return flag;
	}

	@Override
	public boolean deleteBleDevice(Object[] params) {
		// TODO Auto-generated method stub
		boolean flag = false;
		SQLiteDatabase database = null;
		try{
			String sql = "delete from binding_device where id = ?";
			database = helper.getWritableDatabase();
			database.execSQL(sql, params);
			flag = true;
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if (database != null)
			{
				database.close();
			}
		}
		return flag;
	}

	@Override
	public boolean updateBleDevice(Object[] params) {
		// TODO Auto-generated method stub
		boolean flag = false;
		SQLiteDatabase database = null;
		try{
			String sql = "update binding_device set device_name = ?, mac_address = ?, company_code = ?, orders = ? where id = ?";
			database = helper.getWritableDatabase();
			database.execSQL(sql, params);
			flag = true;
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if (database != null)
			{
				database.close();
			}
		}
		return flag;
	}

	@Override
	public List<Map<String, String>> listBleDevice(String[] seletionArgs) {
		// TODO Auto-generated method stub
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		SQLiteDatabase database = null;
		try{
			String sql = "select * from binding_device";
			database = helper.getReadableDatabase();
			Cursor cursor = database.rawQuery(sql, null);
			int columns = cursor.getColumnCount();
			while(cursor.moveToNext()){
				Map<String,String> map = new HashMap<String,String>();
				for(int i=0;i<columns;i++){
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor.getColumnIndex(cols_name));
					if (cols_value == null){
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}
				list.add(map);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if (database != null)
			{
				database.close();
			}
		} 
		return list;
	}

}
