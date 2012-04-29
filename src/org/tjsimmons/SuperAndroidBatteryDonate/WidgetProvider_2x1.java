package org.tjsimmons.SuperAndroidBatteryDonate;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;

public class WidgetProvider_2x1 extends BaseWidgetProvider {
	//RemoteViews views;
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		for (int i = 0; i < appWidgetIds.length; i++) {
			//views = new RemoteViews(context.getPackageName(), R.layout.widget_2x1); 
			
			Intent intent = new Intent(context, BatteryUpdateService.class);
			
			context.startService(intent);
		}
	}
}
