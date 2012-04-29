package org.tjsimmons.SuperAndroidBatteryDonate;

import java.lang.Thread.UncaughtExceptionHandler;
import android.content.Context;
import android.content.Intent;

public class CustomExceptionHandler implements UncaughtExceptionHandler {
    private UncaughtExceptionHandler defaultUEH;
    //private String localPath;
    Context context;

    /* 
     * if any of the parameters is null, the respective functionality 
     * will not be used 
     */
    public CustomExceptionHandler(/*String localPath,*/ Context context) {
        //this.localPath = localPath;
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        this.context = context;
    }

    public void uncaughtException(Thread t, Throwable e) {
    	/*SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmmss");
        String timestamp = sdf.format(new Date());
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        String stacktrace = result.toString();
        printWriter.close();
        String filename = timestamp + ".stacktrace";

        if (localPath != null) {
        	writeToFile(stacktrace, filename);
        }*/       

        defaultUEH.uncaughtException(t, e);
        
        // restart our service
        Intent serviceIntent = new Intent();
		serviceIntent.setAction("org.tjsimmons.GameBatteryMeter.BatteryUpdateService");
		context.startService(serviceIntent);
    }

    /*private void writeToFile(String stacktrace, String filename) {
    	  File parentDir = new File("/sdcard/superandroidbattery");
    	  if (!parentDir.exists()) {
    		  parentDir.mkdirs();
    	  }
    	  
        try {
            BufferedWriter bos = new BufferedWriter(new FileWriter(
                    localPath + "/" + filename));
            bos.write(stacktrace);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
