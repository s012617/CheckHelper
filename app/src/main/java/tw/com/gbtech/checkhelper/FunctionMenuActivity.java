package tw.com.gbtech.checkhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import tw.com.gbtech.checkhelper.Util.Delay;
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
                Button button = findViewById(R.id.buttonGoToCheck);
                button.setEnabled(false);
                goToCheck();
                break;
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        Button button = findViewById(R.id.buttonGoToCheck);
        button.setEnabled(true);
    }
}