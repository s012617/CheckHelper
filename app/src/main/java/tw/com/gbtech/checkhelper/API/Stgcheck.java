package tw.com.gbtech.checkhelper.API;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Utf8;
import tw.com.gbtech.checkhelper.Config;
import tw.com.gbtech.checkhelper.Entity.StgCheckNew;
import tw.com.gbtech.checkhelper.Entity.Token;
import tw.com.gbtech.checkhelper.Entity.User;
import tw.com.gbtech.checkhelper.OkHttp;

public class Stgcheck {
    Request request;

    public Call stgcheckGet(String checkDate, String checkStore){
        OkHttpClient client = new OkHttp().client();
        request = new Request.Builder()
                .url(Config.BASE_URL + "/stgcheck?" +
                        "checkDate=" + checkDate +
                        "&" +
                        "checkStore="+ checkStore
                )
                .header("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer "+ Token.tokenString)
                .build();
        return client.newCall(request);
    }

    public Call stgcheckNew(String checkDate, String checkStore, int totalQty, List<StgCheckNew> data){
        OkHttpClient client = new OkHttp().client();
        Gson gson = new Gson();
        Map map = new HashMap<String, Object>();
        map.put("checkDate",checkDate);
        map.put("checkStore",checkStore);
        map.put("totalQty",Integer.toString(totalQty));
        map.put("data",data);
        RequestBody body = RequestBody.create(gson.toJson(map),  MediaType.parse("application/json"));
        request = new Request.Builder()
                .url(Config.BASE_URL + "/stgcheck/new")
                .header("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer "+ Token.tokenString)
                .post(body)
                .build();
        return client.newCall(request);
    }
    public Call stgcheckDelete(String checkDate, String checkStore){
        Gson gson = new Gson();
        Map map = new HashMap<String, Object>();
        map.put("checkDate",checkDate);
        map.put("checkStore",checkStore);
        RequestBody body = RequestBody.create(gson.toJson(map),  MediaType.parse("application/json"));

        OkHttpClient client = new OkHttp().client();
        request = new Request.Builder()
                .url(Config.BASE_URL + "/stgcheck?" + "checkDate="+checkDate+"&checkStore="+checkStore)
                .header("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer "+ Token.tokenString)
                .delete(body)
                .build();
        return client.newCall(request);
    }

}
