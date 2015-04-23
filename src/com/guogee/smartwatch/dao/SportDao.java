package com.guogee.smartwatch.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.guogee.smartwatch.db.DbHelper;
import com.guogee.smartwatch.service.SportService;

public class SportDao implements SportService {

    private DbHelper helper = null;
	
	public SportDao(Context context){
		helper = new DbHelper(context);
	}
		
	@Override
	public boolean addStep(Object[] params) {
		// TODO Auto-generated method stub
		boolean flag = false;
		SQLiteDatabase database = null;
		Log.d("TAG", "SportDao addStep...........................");
		try{
			String sql = "insert into sport (date,month,stepNum,calorie, distance, stepTime, "
					+ "hour0, hour1, hour2, hour3, hour4, hour5, hour6, hour7, hour8, hour9, hour10, hour11, "
					+ "hour12,hour13,hour14,hour15,hour16,hour17,hour18,hour19,hour20,hour21,hour22,hour23,sportTarget,"
					+ "orders) values (?,?,?,?,?,?,  ?,?,?,?,?,   ?,?,?,?,?,  ?,?,?,?,?,  ?,?,?,?,?, ?,?,?,?,?,?)";
			
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
	public boolean deleteStep(Object[] params) {
		// TODO Auto-generated method stub
		boolean flag = false;
		SQLiteDatabase database = null;
		try{
			String sql = "delete from sport where id = ?";
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
	public boolean updateStep(Object[] params) {
		// TODO Auto-generated method stub
		Log.d("TAG", "SportDao updateStep...........................");
		boolean flag = false;
		SQLiteDatabase database = null;
		try{
			String sql = "update sport set date = ?,month = ?, stepNum = ?, calorie = ?,distance = ?, stepTime = ?, hour0 = ?, hour1 = ?, hour2 = ?, "
					+ "hour3 = ?, hour4 = ?, hour5 = ?, hour6 = ?, hour7 = ?, hour8 = ?, hour9 = ?, hour10 = ?, hour11 = ?, "
					+ "hour12 = ?, hour13 = ?, hour14 = ?, hour15 = ?,hour16 = ?, hour17 = ?, hour18 = ?, hour19 = ?, "
					+ "hour20 = ?, hour21 = ?, hour22 = ?, hour23 = ?,orders = ? where id = ?";
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
	public List<LinkedHashMap<String, String>> listStepMaps(String[] seletionArgs) {
		// TODO Auto-generated method stub
		List<LinkedHashMap<String,String>> list = new ArrayList<LinkedHashMap<String,String>>();
		SQLiteDatabase database = null;
		try{
			String sql = "select * from sport";
			database = helper.getReadableDatabase();
			Cursor cursor = database.rawQuery(sql, null);
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
	public List<LinkedHashMap<String, String>> listTodayStepMaps(String[] seletionArgs) {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public List<LinkedHashMap<String, String>> listWeekStepMaps(String[] seletionArgs) {
		// TODO Auto-generated method stub
		
		return null;
	}
	
	@Override
	public List<LinkedHashMap<String, String>> listMonthStepMaps(String[] seletionArgs) {
		// TODO Auto-generated method stub
//		String sql = "select * from devices where devicetype ='ENVSENSER' and room = ? order by orders asc";
		
		List<LinkedHashMap<String,String>> list = new ArrayList<LinkedHashMap<String,String>>();
		SQLiteDatabase database = null;
		try{
			String sql = "select * from sport where month = ? order by orders asc";
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
	
	@Override
	public LinkedHashMap<String, String> listStepFromDays(String[] seletionArgs) {
		// TODO Auto-generated method stub
		LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
		SQLiteDatabase database = null;
		try{
			String sql = "select * from sport where date = ?";
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
	public boolean updateStepTarget(Object[] params) {
		// TODO Auto-generated method stub
		Log.d("TAG", "SportDao updateStepTarget...........................");
		boolean flag = false;
		SQLiteDatabase database = null;
		try{
			String sql = "update sport set sportTarget = ? where id = ?";
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

}
