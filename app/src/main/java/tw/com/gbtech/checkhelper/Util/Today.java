package tw.com.gbtech.checkhelper.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Today {
    public static String getDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(Calendar.getInstance().getTime());

    }
    public static String getTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        return simpleDateFormat.format(Calendar.getInstance().getTime());
    }
}
