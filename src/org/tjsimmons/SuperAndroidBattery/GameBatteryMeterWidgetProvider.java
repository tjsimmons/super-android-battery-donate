package org.tjsimmons.SuperAndroidBattery;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class GameBatteryMeterWidgetProvider extends AppWidgetProvider {
	RemoteViews views;
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		for (int i = 0; i < appWidgetIds.length; i++) {
			views = new RemoteViews(context.getPackageName(), R.layout.main); 
			
			Intent intent = new Intent(context, BatteryUpdateService.class);
			
			context.startService(intent);
		}
	}
}
