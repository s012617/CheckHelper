package tw.com.gbtech.checkhelper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import tw.com.gbtech.checkhelper.API.Login;
import tw.com.gbtech.checkhelper.API.NeedAuth;
import tw.com.gbtech.checkhelper.Entity.Token;
import tw.com.gbtech.checkhelper.Entity.TotalCheck;
import tw.com.gbtech.checkhelper.Entity.User;
import tw.com.gbtech.checkhelper.Response.LoginResponse;
import tw.com.gbtech.checkhelper.Response.NeedAuthResponse;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editTextID;
    EditText editTextPassword;
    TextView textViewMsg;
    ProgressBar loading;

    @Override
    protected void onStart() {
        super.onStart();
        Token.tokenString = getSharedPreferences(getString(R.string.token), Context.MODE_PRIVATE).getString(getString(R.string.token),"");
        if(!Token.tokenString.isEmpty()){
            textViewMsg.setText("驗證中...");
            loading.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        Call call = new NeedAuth().get(Token.tokenString);
        call.enqueue(new Callback() {
            String msg;
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textViewMsg.setText("驗證失敗,請重新登入");
                        loading.setVisibility(View.INVISIBLE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                });

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response == null){
                    return;
                }else{
                    String body = response.body().string();
                    NeedAuthResponse needAuthResponse= new Gson().fromJson(body,NeedAuthResponse.class);
                    if(needAuthResponse.getStatus().matches("success")){

                    }else{
                        msg = needAuthResponse.getErrorMessage();
                    }
                }
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(msg == null){
                            //登入驗證成功
                            Intent intent = new Intent();
                            intent.setClass(LoginActivity.this,FunctionMenuActivity.class);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        }
                        textViewMsg.setText(msg);

                        loading.setVisibility(View.INVISIBLE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                });
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextID = findViewById(R.id.editTextID);
        editTextPassword = findViewById(R.id.editTextPassword);
        textViewMsg = findViewById(R.id.textViewMsg);
        loading = findViewById(R.id.progressBarLoading);
        findViewById(R.id.buttonLogin).setOnClickListener(this);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void doLogin(View view){
        System.out.println("登入");


        view.setEnabled(false);
        String id,password;
        id = editTextID.getText().toString();
        password = editTextPassword.getText().toString();
        if(id.isEmpty()){
            editTextID.setError("請填寫");
            view.setEnabled(true);
            return;
        }

        textViewMsg.setText("登入中...");

        User user = new User(id,password);
        Call call = new Login().doLogin(user);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textViewMsg.setText("fail: "+e.toString());
                        view.setEnabled(true);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String msg;
                if(response == null){
                    msg = "未知錯誤";
                }else{
                    Gson gson = new Gson();
                    String body =response.body().string();
                    System.out.println();
                    LoginResponse loginResponse = gson.fromJson(body, LoginResponse.class);
                    if(loginResponse.getStatus().equals("success")){
                        Token.tokenString = loginResponse.getToken();
                        msg = null;
                    }else{
                        msg = loginResponse.getErrorMessage();
                    }
                }

                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(msg == null){
                            //登入驗證成功
                            SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.token), Context.MODE_PRIVATE).edit();
                            editor.remove(getString(R.string.token));
                            editor.putString(getString(R.string.token), Token.tokenString);
                            editor.apply();

                            Intent intent = new Intent();
                            intent.setClass(LoginActivity.this,FunctionMenuActivity.class);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        }
                        textViewMsg.setText(msg);
                        view.setEnabled(true);
                    }
                });
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonLogin:
                doLogin(v);
                break;
        }
    }
}