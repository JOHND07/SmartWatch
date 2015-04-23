package com.guogee.smartwatch.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.guogee.smartwatch.db.DbHelper;
import com.guogee.smartwatch.service.SleepService;

public class SleepDao implements SleepService {

    private DbHelper helper = null;
	
	public SleepDao(Context context){
		helper = new DbHelper(context);
	}
	
	@Override
	public boolean addSleep(Object[] params) {
		// TODO Auto-generated method stub
		boolean flag = false;
		SQLiteDatabase database = null;
		try{
			String sql = "insert into sleep (date,month,totalTime,deepSleep,lightSleep,rouseTime,orders) values (?,?,?,?,?,?,?)";
			database = helper.getWritableDatabase();
			database.execSQL(sql, params);
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
	public boolean deleteSleep(Object[] params) {
		// TODO Auto-generated method stub
		boolean flag = false;
		SQLiteDatabase database = null;
		try{
			String sql = "delete from sleep where id = ?";
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
	public boolean updateSleep(Object[] params) {
		// TODO Auto-generated method stub
		boolean flag = false;
		SQLiteDatabase database = null;
		try{
			String sql = "update sleep set totalTime = ?, deepSleep = ?, lightSleep = ?, rouseTime = ? where id = ?";
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
	public List<Map<String, String>> listSleepMaps(String[] seletionArgs) {
		// TODO Auto-generated method stub
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		SQLiteDatabase database = null;
		try{
			String sql = "select * from sleep";
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

	@Override
	public LinkedHashMap<String, String> listSelectDaySleepMaps(String[] seletionArgs) {
		// TODO Auto-generated method stub
		LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
		SQLiteDatabase database = null;
		try{
			String sql = "select * from sleep where date = ?";
			database = helper.getReadableDatabase();
			Cursor cursor = database.rawQuery(sql, seletionArgs);
			int columns = cursor.getColumnCount();
			while(cursor.moveToNext()){
				for(int i=0;i<columns;i++){
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor.getColumnIndex(cols_name));
					if (cols_value == null){
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}
			}
			cursor.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if (database != null)
			{
				database.close();
			}
		} 
		return map;
	}

	@Override
	public List<Map<String, String>> listWeekSleepMaps(String[] seletionArgs) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<LinkedHashMap<String, String>> listMonthSleepMaps(String[] seletionArgs){
		List<LinkedHashMap<String,String>> list = new ArrayList<LinkedHashMap<String,String>>();
		SQLiteDatabase database = null;
		try{
			String sql = "select * from sleep where month = ? order by orders asc";
			database = helper.getReadableDatabase();
			Cursor cursor = database.rawQuery(sql, seletionArgs);
			int columns = cursor.getColumnCount();
			while(cursor.moveToNext()){
				LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
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
			cursor.close();
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
