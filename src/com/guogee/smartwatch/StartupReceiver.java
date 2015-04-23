package com.guogee.smartwatch;

import com.guogee.smartwatch.service.SWService;
import com.guogee.smartwatch.utils.Log;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartupReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
//        Intent serviceIntent = new Intent(context, SWService.class);
//        context.startActivity(serviceIntent);

//        Intent activityIntent = new Intent(context, MainActivity.class);
//        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(activityIntent);
		
/**		**/
		  if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
			  Log.d("TAG","ACTION_BOOT_COMPLETED ........................ ");
			  Intent serviceIntent = new Intent(context, SWService.class);
//			  Intent serviceIntent = new Intent(context, BluetoothLeService.class);
//			  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
	          context.startService(serviceIntent);
		  } 
	}

}
