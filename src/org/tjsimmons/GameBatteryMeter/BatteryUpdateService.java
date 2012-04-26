package org.tjsimmons.GameBatteryMeter;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.appwidget.AppWidgetManager;
import android.util.Log;

public class BatteryUpdateService extends Service {
	Context context;
	AppWidgetManager appWidgetManager;
	RemoteViews views;
	ComponentName thisWidget;
	Handler serviceHandler;
	long updateMillis = 60000;
	
	private Runnable updateBatteryTask = new Runnable() {
		public void run() {
			updateBatteryLevel();
			serviceHandler.postDelayed(this, updateMillis);
		}
	};
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		Log.v("BatteryUpdateService::onCreate", "onCreate called");
		
		serviceHandler = new Handler();
		
		context = this;
		appWidgetManager = AppWidgetManager.getInstance(context);
		views = new RemoteViews(context.getPackageName(), R.layout.main);
		thisWidget = new ComponentName(context, GameBatteryMeterWidgetProvider.class);
		
		serviceHandler.post(updateBatteryTask);
	}
	
	@Override
	public void onDestroy() {
		serviceHandler.removeCallbacks(updateBatteryTask);
		Log.v("BatteryUpdateService::onDestroy", "onDestroy called");
		super.onDestroy();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.v("BatteryUpdateService::onStartCommand", "onStartCommand called");
		return START_STICKY;
	}
	
	private void updateBatteryLevel() {
		Log.v("BatteryUpdateService::updateBatteryLevel", "updateBatteryLevel called");
	    BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
	        public void onReceive(Context context, Intent intent) {
	            context.unregisterReceiver(this);
	            
	            int rawlevel = intent.getIntExtra("level", -1);
	            int scale = intent.getIntExtra("scale", -1);
	            int level = -1;
	            int resID;
	            String mDrawableName;
	            
	            if (rawlevel >= 0 && scale > 0) {
	                level = (rawlevel * 100) / scale;
	            }
	            
	            mDrawableName = numToWord(((Integer) (level / 10)).toString() + "0");
	            
	            resID = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
	            
	            views.setImageViewResource(R.id.status_image, resID);
	            
	            appWidgetManager.updateAppWidget(thisWidget, views);
	        }
	    };
	    
	    IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
	    registerReceiver(batteryLevelReceiver, batteryLevelFilter);
	}
	
	private String numToWord(String num) {
		String word;
		
		switch (Integer.parseInt(num)) {
		case 0:
			word = "zero";
			break;
		case 10:
			word = "ten";
			break;
		case 20:
			word = "twenty";
			break;
		case 30:
			word = "thirty";
			break;
		case 40:
			word = "forty";
			break;
		case 50:
			word = "fifty";
			break;
		case 60:
			word = "sixty";
			break;
		case 70:
			word = "seventy";
			break;
		case 80:
			word = "eighty";
			break;
		case 90:
			word = "ninety";
			break;
		case 100:
			word = "hundred";
			break;
		default:
			word = "hundred";
			break;
		}
		
		return word;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}

