package tw.com.gbtech.checkhelper;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class OkHttp {

    public OkHttp() {

    }
    public OkHttpClient client(){
        OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(10000, TimeUnit.SECONDS)
                .readTimeout(10000, TimeUnit.SECONDS)
                .build();
        return client;
    }

}
