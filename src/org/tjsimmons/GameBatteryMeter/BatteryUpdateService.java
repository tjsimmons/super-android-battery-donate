package org.tjsimmons.GameBatteryMeter;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.appwidget.AppWidgetManager;

public class BatteryUpdateService extends Service {
	Context context;
	AppWidgetManager appWidgetManager;
	RemoteViews views;
	ComponentName thisWidget;
	
	private final IBinder mBinder = new LocalBinder();
	
	public class LocalBinder extends Binder {
        BatteryUpdateService getService() {
            return BatteryUpdateService.this;
        }
    }
	
	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		appWidgetManager = AppWidgetManager.getInstance(context);
		views = new RemoteViews(context.getPackageName(), R.layout.main);
		thisWidget = new ComponentName(context, GameBatteryMeterWidgetProvider.class);
		getBatteryLevel();
	}
	
	private void getBatteryLevel() {
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
		return mBinder;
	}
}

