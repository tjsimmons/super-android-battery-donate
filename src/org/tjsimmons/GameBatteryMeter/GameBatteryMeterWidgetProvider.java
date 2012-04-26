package org.tjsimmons.GameBatteryMeter;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class GameBatteryMeterWidgetProvider extends AppWidgetProvider {
	RemoteViews views;
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		for (int i = 0; i < appWidgetIds.length; i++) {
			views = new RemoteViews(context.getPackageName(), R.layout.main); 
			
			Log.v("GameBatteryMeterWidgetProvider::onUpdate", "update");
			
			Intent intent = new Intent(context, BatteryActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			context.startActivity(intent);
		}
	}
}
