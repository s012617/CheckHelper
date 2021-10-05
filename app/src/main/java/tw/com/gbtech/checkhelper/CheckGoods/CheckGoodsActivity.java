package tw.com.gbtech.checkhelper.CheckGoods;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.com.gbtech.checkhelper.Entity.Check;
import tw.com.gbtech.checkhelper.Entity.CheckBoxNum;
import tw.com.gbtech.checkhelper.Entity.CheckWithFlag;
import tw.com.gbtech.checkhelper.R;
import tw.com.gbtech.checkhelper.Util.SwipeHelper;
import tw.com.gbtech.checkhelper.Util.Today;
import tw.com.gbtech.checkhelper.db.DBHelper;

public class CheckGoodsActivity extends AppCompatActivity implements TextView.OnEditorActionListener, View.OnTouchListener {
    List<Check> checks;
    List<CheckBoxNum> checkBoxNums;
    EditText store, checkdate, boxnum, barcode;
    RecyclerView recyclerviewCheckContent;
    DBHelper dbHelper;
    ConstraintLayout constraintLayoutBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_goods);

        recyclerviewCheckContent = findViewById(R.id.recyclerviewCheckContent);
        store = findViewById(R.id.editTextStore);
        checkdate = findViewById(R.id.editTextCheckDate);
        boxnum = findViewById(R.id.editTextBoxNum);
        barcode = findViewById(R.id.editTextBarcode);
        constraintLayoutBase = findViewById(R.id.constraintLayoutBase);

        dbHelper = new DBHelper(this);

        //日期預設今天
        checkdate.setText(Today.getDate());

        checkBoxNums = new ArrayList<CheckBoxNum>();

        CheckGoodsAdapter adapter = new CheckGoodsAdapter(checkBoxNums,this);
        recyclerviewCheckContent.setLayoutManager(new LinearLayoutManager(this));
        recyclerviewCheckContent.setAdapter(adapter);

        store.setOnEditorActionListener(this::onEditorAction);
        checkdate.setOnEditorActionListener(this::onEditorAction);
        boxnum.setOnEditorActionListener(this::onEditorAction);
        barcode.setOnEditorActionListener(this::onEditorAction);

        for(int i=0; i<constraintLayoutBase.getChildCount(); i++){
            constraintLayoutBase.getChildAt(i).setOnTouchListener(this::onTouch);
        }

    }
    void dataChange(View v){
        System.out.println("do data Change!!");
        checks = dbHelper.getAppDatabase().checkDao().getCheck(store.getText().toString(), checkdate.getText().toString());
        checkBoxNums.clear();
        if(checks!=null){
            List<CheckWithFlag> checkWithFlags = new ArrayList<CheckWithFlag>();
            for (Check check:checks) {
                checkWithFlags.add(new CheckWithFlag(check,0));
            }
            for (CheckWithFlag dataset:checkWithFlags) {
                boolean isNew = true;
                for(CheckBoxNum checkBoxNum:checkBoxNums){
                    if(dataset.getCheck().name.matches(checkBoxNum.boxnum)){
                        isNew = false;
                        checkBoxNums.get(checkBoxNums.indexOf(checkBoxNum)).getChecks().add(dataset);
                        break;
                    }
                }
                if(isNew){
                    List<CheckWithFlag> list = new ArrayList<CheckWithFlag>();
                    list.add(dataset);
                    checkBoxNums.add(new CheckBoxNum(dataset.getCheck().name,list));
                }
            }


            System.out.println(checkBoxNums);
            System.out.println(recyclerviewCheckContent.getAdapter().getItemCount());
            System.out.println("data Changed!!");
        }
        recyclerviewCheckContent.getAdapter().notifyDataSetChanged();
    }
    void insertBarcode(View v){
        List<Check> check = dbHelper.getAppDatabase().checkDao().getCheck(
                store.getText().toString(),
                checkdate.getText().toString(),
                boxnum.getText().toString(),
                barcode.getText().toString()
               );
        if(check.size()>0){
            dbHelper.getAppDatabase().checkDao().insertAll(new Check(
                    barcode.getText().toString(),
                    boxnum.getText().toString(),
                    checkdate.getText().toString(),
                    store.getText().toString(),
                    checks.get(0).quantity+1,
                    null
            ));
        }else{
            dbHelper.getAppDatabase().checkDao().insertAll(new Check(
                    barcode.getText().toString(),
                    boxnum.getText().toString(),
                    checkdate.getText().toString(),
                    store.getText().toString(),
                    1,
                    null
            ));
        }
        dataChange(v);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (v.getId()){
            case R.id.editTextStore:
            case R.id.editTextCheckDate:
                dataChange(v);
                hideKeyBorad(v);
                break;
            case R.id.editTextBoxNum:
                hideKeyBorad(v);
                break;
            case R.id.editTextBarcode:
                insertBarcode(v);
                v.requestFocus();
                v.setText("");
                hideKeyBorad(v);
                break;
        }
        return false;
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (v.getId()){
            case R.id.editTextStore:
            case R.id.editTextCheckDate:
            case R.id.editTextBoxNum:
            case R.id.editTextBarcode:
                break;

            default:
                hideKeyBorad(v);
                break;
        }

        return false;
    }



    private void hideKeyBorad(View v){
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }catch (Exception e){
        }
    }
}