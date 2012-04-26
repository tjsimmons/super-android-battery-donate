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
		
		//Log.v("BatteryUpdateService::onCreate", "onCreate called");
		
		serviceHandler = new Handler();
		
		context = this;
		appWidgetManager = AppWidgetManager.getInstance(context);
		views = new RemoteViews(context.getPackageName(), R.layout.main);
		thisWidget = new ComponentName(context, GameBatteryMeterWidgetProvider.class);
		
		serviceHandler.post(updateBatteryTask);
		//getBatteryLevel();
	}
	
	@Override
	public void onDestroy() {
		serviceHandler.removeCallbacks(updateBatteryTask);
		//Log.v("BatteryUpdateService::onDestroy", "onDestroy called");
		super.onDestroy();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//Log.v("BatteryUpdateService::onStartCommand", "onStartCommand called");
		return START_STICKY;
	}
	
	private void updateBatteryLevel() {
		//Log.v("BatteryUpdateService::updateBatteryLevel", "updateBatteryLevel called");
	    BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
	        public void onReceive(Context context, Intent intent) {
	            context.unregisterReceiver(this);
	            int rawlevel = intent.getIntExtra("level", -1);
	            int scale = intent.getIntExtra("scale", -1);
	            int level = -1;
	            
	            if (rawlevel >= 0 && scale > 0) {
	                level = (rawlevel * 100) / scale;
	            }
	            
	            views.setTextViewText(R.id.status_text, ((Integer) level).toString() + "%");
	            appWidgetManager.updateAppWidget(thisWidget, views);
	        }
	    };
	    
	    IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
	    registerReceiver(batteryLevelReceiver, batteryLevelFilter);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}

