package com.guogee.smartwatch.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
	
	public static String name = "SmartWatchDB.db";
	private static int version = 1;

	public DbHelper(Context context) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		///////////////////////////////////////////////////////////////
		/**
		 * "sport" date base 
		 * ble's  date, step number, step percent ,calorie, sport distance, step time
		 
		String sq1 = "CREATE TABLE IF NOT EXISTS sport ( " +
		"id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
		"date TEXT, " +
		"stepNum INTEGER NOT NULL, " +
        "calorie INTEGER NOT NULL, " +
        "distance INTEGER NOT NULL, " +
        "stepTime TEXT, " +
		"orders INTEGER NOT NULL)";
        db.execSQL(sq1);
        
        */
		
		String sq1 = "CREATE TABLE IF NOT EXISTS sport ( " +
		"id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
		"date TEXT, " +
		"month TEXT, " +
		"stepNum TEXT, " + 
        "calorie TEXT, " +
        "distance TEXT, " +
        "stepTime TEXT, " +
        "hour0 TEXT, " +
        "hour1 TEXT, " +
        "hour2 TEXT, " +
        "hour3 TEXT, " +
        "hour4 TEXT, " +
        "hour5 TEXT, " +
        "hour6 TEXT, " +
        "hour7 TEXT, " +
        "hour8 TEXT, " +
        "hour9 TEXT, " +
        "hour10 TEXT, " +
        "hour11 TEXT, " +
        "hour12 TEXT, " +
        "hour13 TEXT, " +
        "hour14 TEXT, " +
        "hour15 TEXT, " +
        "hour16 TEXT, " +
        "hour17 TEXT, " +
        "hour18 TEXT, " +
        "hour19 TEXT, " +
        "hour20 TEXT, " +
        "hour21 TEXT, " +
        "hour22 TEXT, " +
        "hour23 TEXT, " +
        "sportTarget TEXT, " +
        "orders INTEGER NOT NULL)";
        db.execSQL(sq1);
        
		
        /**
		 * "sleep" date base 
		 * ble'  date, sleep time number, deep sleep number ,shallow sleep number, rouse number
		 */
		String sq2 = "CREATE TABLE IF NOT EXISTS sleep ( " +
		"id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
		"date TEXT, " +
		"month TEXT, " +
		"totalTime INTEGER NOT NULL, " +
		"deepSleep  INTEGER NOT NULL, " +
        "lightSleep INTEGER NOT NULL, " +
        "rouseTime INTEGER NOT NULL, " +
		"orders INTEGER NOT NULL)";
        db.execSQL(sq2);
        
        
//        "INSERT into binding_device (device_name,device_code,mac_address,device_id,company_code,add_date,userid) values (?,?,?, ?, ?,?,?)",
        /**
		 * "bind ble" date base 
		 * ble'  date, sleep time number, deep sleep number ,shallow sleep number, rouse number
		 */
		String sq3 = "CREATE TABLE IF NOT EXISTS binding_device ( " +
		"id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
		"device_name TEXT, " +
		"mac_address TEXT, " +
		"company_code TEXT, " +
		"orders INTEGER NOT NULL)";
        db.execSQL(sq3);        
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int verOld, int verNew) {
		// TODO Auto-generated method stub
	}

}
