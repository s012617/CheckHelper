package tw.com.gbtech.checkhelper.API;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import tw.com.gbtech.checkhelper.Config;
import tw.com.gbtech.checkhelper.Entity.StgCheckBoxNew;
import tw.com.gbtech.checkhelper.Entity.StgCheckNew;
import tw.com.gbtech.checkhelper.Entity.Token;
import tw.com.gbtech.checkhelper.OkHttp;

public class StgcheckBox {
    Request request;

    public Call stgcheckGet(String checkId, String checkStore){
        OkHttpClient client = new OkHttp().client();
        request = new Request.Builder()
                .url(Config.BASE_URL + "/stgcheckbox?" +
                        "checkId=" + checkId +
                        "&" +
                        "checkStore="+ checkStore
                )
                .header("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer "+ Token.tokenString)
                .build();
        return client.newCall(request);
    }

    public Call stgcheckNew(String checkId, String checkStore, int totalQty, List<StgCheckBoxNew> data){
        OkHttpClient client = new OkHttp().client();
        Gson gson = new Gson();
        Map map = new HashMap<String, Object>();
        map.put("data",data);
        map.put("totalQty",Integer.toString(totalQty));
        map.put("checkStore",checkStore);
        map.put("checkId",checkId);
        RequestBody body = RequestBody.create(gson.toJson(map),  MediaType.parse("application/json"));

        request = new Request.Builder()
                .url(Config.BASE_URL + "/stgcheckbox/new")
                .header("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer "+ Token.tokenString)
                .post(body)
                .build();
        return client.newCall(request);
    }
    public Call stgcheckDelete(String checkId, String checkStore){
        Gson gson = new Gson();
        Map map = new HashMap<String, Object>();
        map.put("checkId",checkId);
        map.put("checkStore",checkStore);
        RequestBody body = RequestBody.create(gson.toJson(map),  MediaType.parse("application/json"));
        OkHttpClient client = new OkHttp().client();
        request = new Request.Builder()
                .url(Config.BASE_URL + "/stgcheckbox?" + "checkId="+checkId+"&checkStore="+checkStore)
                .header("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer "+ Token.tokenString)
                .delete(body)
                .build();
        return client.newCall(request);
    }
    public Call stgcheckDeleteAll(String checkDate, String checkStore){
        Gson gson = new Gson();
        Map map = new HashMap<String, Object>();
        map.put("checkDate",checkDate);
        map.put("checkStore",checkStore);
        RequestBody body = RequestBody.create(gson.toJson(map),  MediaType.parse("application/json"));

        OkHttpClient client = new OkHttp().client();
        request = new Request.Builder()
                .url(Config.BASE_URL + "/stgcheckbox/delall?" + "checkDate="+checkDate+"&checkStore="+checkStore+"&delAll=true")
                .header("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer "+ Token.tokenString)
                .delete(body)
                .build();
        return client.newCall(request);
    }
}
