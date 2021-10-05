package tw.com.gbtech.checkhelper.API;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.util.Base64;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tw.com.gbtech.checkhelper.Config;
import tw.com.gbtech.checkhelper.Entity.User;
import tw.com.gbtech.checkhelper.OkHttp;

public class Login {
    Request request;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Call doLogin(User user){
        OkHttpClient client = new OkHttp().client();
        request = new Request.Builder()
                .url(Config.BASE_URL+"/login")
                .header("Content-Type", "application/json")
                .addHeader("Authorization", "Basic "+ Base64.getEncoder().encodeToString((user.getId()+":"+user
                .getPassword()).getBytes()).trim())
                .build();

        return client.newCall(request);
    }

}
