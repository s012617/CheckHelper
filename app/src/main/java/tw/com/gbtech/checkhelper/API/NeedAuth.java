package tw.com.gbtech.checkhelper.API;

import android.os.Build;

import androidx.annotation.RequiresApi;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import tw.com.gbtech.checkhelper.Config;
import tw.com.gbtech.checkhelper.OkHttp;

public class NeedAuth {
    Request request;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Call get(String tokenstring){
        OkHttpClient client = new OkHttp().client();
        request = new Request.Builder()
                .url(Config.BASE_URL+"/needauth")
                .header("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer  "+ tokenstring)
                .build();

        return client.newCall(request);
    }
}
