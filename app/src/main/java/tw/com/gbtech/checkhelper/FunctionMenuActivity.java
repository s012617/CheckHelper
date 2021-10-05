package tw.com.gbtech.checkhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import tw.com.gbtech.checkhelper.checkTotal.TotalInfoActivity;

public class FunctionMenuActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_menu);

        findViewById(R.id.buttonGoToCheck).setOnClickListener(this::onClick);
    }
    public void goToCheck(){
        Intent intent = new Intent();
        intent.setClass(FunctionMenuActivity.this, TotalInfoActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonGoToCheck:
                goToCheck();
                break;
        }
    }
}