package tw.com.gbtech.checkhelper.Util;

import android.os.Handler;

public class Delay {
    public static void delay(int ms){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                //Execute code here

            }
        }, ms);
    }
}
