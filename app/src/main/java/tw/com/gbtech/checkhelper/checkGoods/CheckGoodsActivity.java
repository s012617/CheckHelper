package tw.com.gbtech.checkhelper.checkGoods;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.zxing.client.android.BeepManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okio.Buffer;
import tw.com.gbtech.checkhelper.API.Stgcheck;
import tw.com.gbtech.checkhelper.API.StgcheckBox;
import tw.com.gbtech.checkhelper.Entity.Check;
import tw.com.gbtech.checkhelper.Entity.CheckBoxNum;
import tw.com.gbtech.checkhelper.Entity.CheckWithFlag;
import tw.com.gbtech.checkhelper.Entity.StgCheckBoxNew;
import tw.com.gbtech.checkhelper.Entity.StgCheckNew;
import tw.com.gbtech.checkhelper.R;
import tw.com.gbtech.checkhelper.Response.StgCheckBoxDeleteResponse;
import tw.com.gbtech.checkhelper.Response.StgCheckBoxGetResponse;
import tw.com.gbtech.checkhelper.Response.StgCheckBoxNewResponse;
import tw.com.gbtech.checkhelper.Response.StgCheckDeleteResponse;
import tw.com.gbtech.checkhelper.Response.StgCheckGetResponse;
import tw.com.gbtech.checkhelper.Response.StgCheckNewResponse;
import tw.com.gbtech.checkhelper.Util.Today;
import tw.com.gbtech.checkhelper.db.DBHelper;
import tw.com.gbtech.checkhelper.scanner.ContinuousCaptureActivity;

public class CheckGoodsActivity extends AppCompatActivity implements TextView.OnEditorActionListener, View.OnTouchListener, View.OnClickListener, TextWatcher {
    List<Check> checks;
    List<CheckWithFlag> checkWithFlags;
    List<CheckBoxNum> checkBoxNums;
    EditText store, checkdate, boxnum, barcode,type;
    TextView textViewForFocus;
    RecyclerView recyclerviewCheckContent;
    DBHelper dbHelper;
    ConstraintLayout constraintLayoutBase;
    Call call;
    boolean typeMode = false;

    private Vibrator vibrator;

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
        textViewForFocus = findViewById(R.id.textViewForFocus);
        type = findViewById(R.id.editTextType);
        type.setVisibility(View.INVISIBLE);

        dbHelper = new DBHelper(this);

        //日期預設今天
        checkdate.setText(Today.getDate());

        checkBoxNums = new ArrayList<CheckBoxNum>();

        CheckGoodsAdapter adapter = new CheckGoodsAdapter(
                checkBoxNums,
                v -> {
                    CheckWithFlag checkWithFlag = (CheckWithFlag) v.getTag();
                    dbHelper.getAppDatabase().checkDao().delete(
                            checkWithFlag.getCheck().store,
                            checkWithFlag.getCheck().checkDate,
                            checkWithFlag.getCheck().name,
                            checkWithFlag.getCheck().barcode
                    );
                    CheckWithFlag checkf = (CheckWithFlag) v.getTag();
                    checkWithFlags.remove(checkWithFlags.indexOf(checkf));
                    dataChange(checkWithFlags);
                },
                v -> {
                    CheckWithFlag checkWithFlag = (CheckWithFlag) v.getTag();
                    int q = checkWithFlag.getCheck().quantity - 1;
                    CheckWithFlag checkf = (CheckWithFlag) v.getTag();
                    if (q > 0) {
                        checkWithFlags.get(checkWithFlags.indexOf(checkf)).getCheck().quantity -= 1;
                        dbHelper.getAppDatabase().checkDao().update(
                                q,
                                checkWithFlag.getCheck().store,
                                checkWithFlag.getCheck().checkDate,
                                checkWithFlag.getCheck().name,
                                checkWithFlag.getCheck().barcode
                        );
                    } else {
                        dbHelper.getAppDatabase().checkDao().delete(
                                checkWithFlag.getCheck().store,
                                checkWithFlag.getCheck().checkDate,
                                checkWithFlag.getCheck().name,
                                checkWithFlag.getCheck().barcode);


                        checkWithFlags.remove(checkWithFlags.indexOf(checkf));
                    }
                    dataChange(checkWithFlags);
                });
        recyclerviewCheckContent.setLayoutManager(new LinearLayoutManager(this));
        recyclerviewCheckContent.setAdapter(adapter);

        vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        
        store.setOnEditorActionListener(this::onEditorAction);
        checkdate.setOnEditorActionListener(this::onEditorAction);
        boxnum.setOnEditorActionListener(this::onEditorAction);
        barcode.setOnEditorActionListener(this::onEditorAction);
        type.setOnEditorActionListener(this::onEditorAction);

        barcode.addTextChangedListener(this);
        //掃描槍輸入完清空
        barcode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER)
                    barcode.setText("");
                return false;
            }
        });

        barcode.setShowSoftInputOnFocus(false);
        checkdate.setShowSoftInputOnFocus(false);

        checkdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hideKeyBorad(v);
                    String date = Today.getDate();
                    new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {
                            checkdate.setText(String.valueOf(year) + String.format("%02d", month + 1) + String.format("%02d", day));   //取得選定的日期指定給日期編輯框
                        }
                    }, Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(4, 6)) - 1, Integer.parseInt(date.substring(6, 8))).show();
                }
            }
        });

        barcode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    hideKeyBorad(v);
            }
        });

        findViewById(R.id.buttonGoToScan).setOnClickListener(this::onClick);
        findViewById(R.id.buttonBoxDelete).setOnClickListener(this::onClick);
        findViewById(R.id.buttonDeleteRemote).setOnClickListener(this::onClick);
        findViewById(R.id.buttonSearchRemote).setOnClickListener(this::onClick);
      //  findViewById(R.id.buttonBoxSearchRemote).setOnClickListener(this::onClick);
        findViewById(R.id.buttonUpdateToRemote).setOnClickListener(this::onClick);
      //  findViewById(R.id.buttonBoxUpdateToRemote).setOnClickListener(this::onClick);
        findViewById(R.id.buttonChange).setOnClickListener(this::onClick);
        textViewForFocus.setOnFocusChangeListener((v, hasFocus) -> {

            barcode.setFocusable(true);
            barcode.setFocusableInTouchMode(true);
            barcode.requestFocus();

        });

        for (int i = 0; i < constraintLayoutBase.getChildCount(); i++) {
            constraintLayoutBase.getChildAt(i).setOnTouchListener(this::onTouch);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        dataChange(null);
        findViewById(R.id.buttonGoToScan).setEnabled(true);
    }


    void dataChange(List<CheckWithFlag> l) {
        System.out.println("do data Change!!");
        checks = dbHelper.getAppDatabase().checkDao().getCheck(store.getText().toString(), checkdate.getText().toString());
        checkBoxNums.clear();

        if (checks != null) {
            if (l != null) {
                checkWithFlags = l;
            } else {
                checkWithFlags = new ArrayList<CheckWithFlag>();
                for (Check check : checks) {
                    int flag = 0;
                    checkWithFlags.add(new CheckWithFlag(check, flag));
                }
            }
            for (CheckWithFlag dataset : checkWithFlags) {
                boolean isNew = true;
                for (CheckBoxNum checkBoxNum : checkBoxNums) {
                    if (dataset.getCheck().name.matches(checkBoxNum.boxnum)) {
                        isNew = false;
                        checkBoxNums.get(checkBoxNums.indexOf(checkBoxNum)).getChecks().add(dataset);
                        break;
                    }
                }
                if (isNew) {
                    List<CheckWithFlag> list = new ArrayList<CheckWithFlag>();
                    list.add(dataset);
                    checkBoxNums.add(new CheckBoxNum(dataset.getCheck().name, list));
                }
            }
            System.out.println(checkBoxNums);
            System.out.println(recyclerviewCheckContent.getAdapter().getItemCount());
            System.out.println("data Changed!!");
        }
        recyclerviewCheckContent.getAdapter().notifyDataSetChanged();
    }

    void insertBarcode(String store, String checkdate, String boxnum, String barcode) {
        List<Check> check = dbHelper.getAppDatabase().checkDao().getCheck(
                store,
                checkdate,
                boxnum,
                barcode
        );
        if (check.size() > 0) {
            int quan = check.get(0).quantity+1;
            dbHelper.getAppDatabase().checkDao().update(quan,store,checkdate,boxnum,barcode);
        } else {
            dbHelper.getAppDatabase().checkDao().insertAll(new Check(
                    barcode,
                    boxnum,
                    checkdate,
                    store,
                    1,
                    null
            ));
        }
        dataChange(null);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (v.getId()) {
            case R.id.editTextStore:
            case R.id.editTextCheckDate:
                dataChange(null);
                break;
            case R.id.editTextBoxNum:
                hideKeyBorad(v);
                break;
            case R.id.editTextBarcode:
                break;
            case R.id.editTextType:
                insertBarcode(store.getText().toString(),checkdate.getText().toString(),boxnum.getText().toString(),v.getText().toString());
                vibrator.vibrate(150);
                break;
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (v.getId()) {
            case R.id.editTextStore:

            case R.id.editTextBoxNum:
            case R.id.editTextBarcode:
                break;
            case R.id.editTextCheckDate:
                break;

            default:
                hideKeyBorad(v);
                dataChange(null);
                break;
        }

        return false;
    }


    private void hideKeyBorad(View v) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonGoToScan:
                v.setEnabled(false);
                Intent intent = new Intent(this, ContinuousCaptureActivity.class);
                intent.putExtra("boxnum", boxnum.getText().toString());
                intent.putExtra("checkDate", checkdate.getText().toString());
                intent.putExtra("store", store.getText().toString());

                startActivity(intent);
                break;
            case R.id.buttonBoxDelete:
                v.setEnabled(false);
                doublecCheckDialog("(箱)刪除", String.format("清空指定的遠端資料\n庫點代號: %1s\n箱號: %2s\n日期: %3s",
                        store.getText().toString(),
                        boxnum.getText().toString(),
                        checkdate.getText().toString()),
                        v,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                buttonBoxDeleteOnClick(v);
                            }
                        });
                break;
            case R.id.buttonDeleteRemote:
                v.setEnabled(false);
                doublecCheckDialog("刪除", String.format("清空指定的遠端資料\n庫點代號: %1s\n日期: %2s",
                        store.getText().toString(),
                        checkdate.getText().toString()),
                        v,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                buttonDeleteAllOnClick(v);
                            }
                        });
                break;
//            case R.id.buttonBoxSearchRemote:
//                v.setEnabled(false);
//                doublecCheckDialog("(箱)查詢", String.format("查詢指定的遠端資料\n庫點代號: %1s\n箱號: %2s\n日期: %3s",
//                        store.getText().toString(),
//                        boxnum.getText().toString(),
//                        checkdate.getText().toString()),
//                        v,
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                buttonBoxSearchRemoteOnClick(v);
//                            }
//                        });
//                break;
            case R.id.buttonSearchRemote:
                v.setEnabled(false);
                doublecCheckDialog("(箱)查詢", String.format("查詢指定的遠端資料\n庫點代號: %1s\n箱號: %2s\n日期: %3s",
                        store.getText().toString(),
                        boxnum.getText().toString(),
                        checkdate.getText().toString()),
                        v,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                buttonBoxSearchRemoteOnClick(v);
                            }
                        });
                break;


//                v.setEnabled(false);
//                doublecCheckDialog("查詢", String.format("查詢指定的遠端資料\n庫點代號: %1s\n日期: %2s",
//                        store.getText().toString(),
//                        checkdate.getText().toString()),
//                        v,
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                buttonSearchRemoteOnClick(v);
//                            }
//                        });
//                break;
            case R.id.buttonUpdateToRemote:
                v.setEnabled(false);
                doublecCheckDialog("(箱)同步", String.format("將本機資料上傳到遠端\n庫點代號: %1s\n箱號: %2s\n日期: %3s",
                        store.getText().toString(),
                        boxnum.getText().toString(),
                        checkdate.getText().toString()),
                        v,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                buttonBoxUpdateToRemoteOnClick(v);
                            }
                        });
                break;
//
//                v.setEnabled(false);
//                doublecCheckDialog("同步", String.format("將本機資料上傳到遠端\n庫點代號: %1s\n日期: %2s",
//                        store.getText().toString(),
//                        checkdate.getText().toString()),
//                        v,
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                buttonUpdateToRemoteOnClick(v);
//                            }
//                        });
//                break;
//            case R.id.buttonBoxUpdateToRemote:
//                v.setEnabled(false);
//                doublecCheckDialog("(箱)同步", String.format("將本機資料上傳到遠端\n庫點代號: %1s\n箱號: %2s\n日期: %3s",
//                        store.getText().toString(),
//                        boxnum.getText().toString(),
//                        checkdate.getText().toString()),
//                        v,
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                buttonBoxUpdateToRemoteOnClick(v);
//                            }
//                        });
//                break;
            case R.id.buttonChange:
                typeMode = !typeMode;
                if(typeMode){
                    barcode.setVisibility(View.INVISIBLE);
                    type.setVisibility(View.VISIBLE);
                }else{
                   type.setVisibility(View.INVISIBLE);
                   barcode.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
    private void doublecCheckDialog(String title, String msg, View v, DialogInterface.OnClickListener onClickListener) {
        CheckGoodsActivity.this.runOnUiThread(() -> {
            AlertDialog dialog = (
                    new AlertDialog.Builder(v.getContext())
                            .setTitle(title)
                            .setMessage(msg)
                            .setPositiveButton("確定", onClickListener))
                    .setNegativeButton("取消", null)
                    .create();
            dialog.show();
            v.setEnabled(true);
        });
    }

    private void buttonBoxUpdateToRemoteOnClick(View v) {
        checks = dbHelper.getAppDatabase().checkDao().getCheck(store.getText().toString(), checkdate.getText().toString(), boxnum.getText().toString());
        int totalQty = 0;
        List<StgCheckBoxNew> list = new ArrayList<>();
        for (Check data : checks) {
            totalQty += data.quantity;
            list.add(new StgCheckBoxNew(data.barcode, data.quantity));
        }
        Log.d("update", new String(new Gson().toJson(list).getBytes(StandardCharsets.UTF_8)));
        call = new StgcheckBox().stgcheckNew(
                checkdate.getText().toString() + boxnum.getText().toString(),
                store.getText().toString(),
                totalQty,
                list
        );
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                CheckGoodsActivity.this.runOnUiThread(() -> {
                    AlertDialog dialog = (
                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("(箱)同步")
                                    .setMessage("錯誤: " + e.toString() + "(請聯絡開發人員)")
                                    .setPositiveButton("確定", null))
                            .create();
                    dialog.show();
                    v.setEnabled(true);
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String msg = null;
                if (response == null) {
                    msg = "錯誤: 未知錯誤";
                } else {
                    Gson gson = new Gson();
                    String body = response.body().string();
                    System.out.println(body);

                    try {

                        StgCheckBoxNewResponse res = gson.fromJson(body, StgCheckBoxNewResponse.class);

                        if (res.getStatus() != null && res.getStatus().matches("success")) {
                            msg = "已上傳";
                        } else {
                            if (res.getErrorData() != null) {
                                msg = res.getErrorMessage() + "\n條碼錯誤: \n" + res.getErrorData().toString();
                                for (StgCheckBoxNew data : res.getErrorData()) {
                                    for (CheckWithFlag d : checkWithFlags) {
                                        if (d.getCheck().barcode.matches(data.getBarCode())) {
                                            checkWithFlags.get(checkWithFlags.indexOf(d)).setFlag(1);
                                            Log.d("errorBarcode", "1");
                                            break;
                                        }
                                    }
                                }
                                Log.d("errorBarcode", checkWithFlags.toString());
                            } else {
                                msg = "錯誤:" + res.getErrorMessage();

                            }
                        }
                    } catch (IllegalStateException | JsonSyntaxException exception) {
                        Log.d("searchRes", exception.getMessage() + "\n" + body);
                        msg = "錯誤: json格式有誤(請聯絡開發人員)";
                    }
                }
                String finalMsg = msg;
                CheckGoodsActivity.this.runOnUiThread(() -> {
                    //
                    final Buffer buffer = new Buffer();
                    try {
                        call.request().body().writeTo(buffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(response.toString());
                    System.out.println(call.request().toString() +
                            buffer.readUtf8());
                    //
                    dataChange(checkWithFlags);
                    AlertDialog dialog = (
                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("(箱)同步")
                                    .setMessage(finalMsg)
                                    .setPositiveButton("確定", null))
                            .create();
                    dialog.show();
                    v.setEnabled(true);
                });

            }
        });
    }

    private void buttonUpdateToRemoteOnClick(View v) {
        checks = dbHelper.getAppDatabase().checkDao().getCheck(store.getText().toString(), checkdate.getText().toString());
        int totalQty = 0;
        List<StgCheckNew> list = new ArrayList<StgCheckNew>();
        for (Check data : checks) {
            totalQty += data.quantity;
            list.add(new StgCheckNew(data.barcode, data.quantity));
        }
        Log.d("update", new String(new Gson().toJson(list).getBytes(StandardCharsets.UTF_8)));
        call = new Stgcheck().stgcheckNew(
                checkdate.getText().toString(),
                store.getText().toString(),
                totalQty,
                list
        );
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                CheckGoodsActivity.this.runOnUiThread(() -> {
                    AlertDialog dialog = (
                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("同步")
                                    .setMessage("錯誤: " + e.toString() + "(請聯絡開發人員)")
                                    .setPositiveButton("確定", null))
                            .create();
                    dialog.show();
                    v.setEnabled(true);
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String msg = null;
                if (response == null) {
                    msg = "錯誤: 未知錯誤";
                } else {
                    Gson gson = new Gson();
                    String body = response.body().string();
                    System.out.println(body);

                    try {
                        StgCheckNewResponse res = gson.fromJson(body, StgCheckNewResponse.class);
                        if (res.getStatus() != null && res.getStatus().matches("success")) {
                            msg = "已上傳";
                        } else {
                            if (res.getErrorData() != null) {
                                msg = res.getErrorMessage() + "\n條碼錯誤: \n" + res.getErrorData().toString();
                                for (StgCheckNew data : res.getErrorData()) {
                                    for (CheckWithFlag d : checkWithFlags) {
                                        if (d.getCheck().barcode.matches(data.getBarCode())) {
                                            checkWithFlags.get(checkWithFlags.indexOf(d)).setFlag(1);
                                            Log.d("errorBarcode", "1");
                                            break;
                                        }
                                    }
                                }
                                Log.d("errorBarcode", checkWithFlags.toString());
                            } else {
                                msg = "錯誤: " + res.getErrorMessage() + response;
                            }
                        }
                    } catch (IllegalStateException | JsonSyntaxException exception) {
                        Log.d("searchRes", exception.getMessage() + "\n" + body);
                        msg = "錯誤: json格式有誤(請聯絡開發人員)";
                    }
                }
                String finalMsg = msg;
                CheckGoodsActivity.this.runOnUiThread(() -> {
                    dataChange(checkWithFlags);
                    AlertDialog dialog = (
                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("同步")
                                    .setMessage(finalMsg)
                                    .setPositiveButton("確定", null))
                            .create();
                    dialog.show();
                    v.setEnabled(true);
                });

            }
        });

    }

    private void buttonSearchRemoteOnClick(View v) {
        call = new Stgcheck().stgcheckGet(checkdate.getText().toString(), store.getText().toString());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                CheckGoodsActivity.this.runOnUiThread(() -> {
                    AlertDialog dialog = (
                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("查詢")
                                    .setMessage("錯誤: " + e.toString() + "(請聯絡開發人員)")
                                    .setPositiveButton("確定", null))
                            .create();
                    dialog.show();
                    v.setEnabled(true);
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String msg = null;
                if (response == null) {
                    msg = "錯誤: 未知錯誤";
                } else {
                    Gson gson = new Gson();
                    String body = response.body().string();
                    System.out.println(body);

                    try {
                        StgCheckGetResponse res = gson.fromJson(body, StgCheckGetResponse.class);
                        if (res.getStatus() != null && res.getStatus().matches("success")) {
                            msg = res.getData().toString();
                        } else {
                            msg = "錯誤:" + res.getErrorMessage();
                        }
                    } catch (IllegalStateException | JsonSyntaxException exception) {
                        Log.d("searchRes", exception.getMessage() + "\n" + body);
                        msg = "錯誤: json格式有誤(請聯絡開發人員)";
                    }
                }
                String finalMsg = msg;
                CheckGoodsActivity.this.runOnUiThread(() -> {
                    AlertDialog dialog = (
                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("查詢")
                                    .setMessage(finalMsg)
                                    .setPositiveButton("確定", null))
                            .create();
                    dialog.show();
                    v.setEnabled(true);
                });

            }
        });
    }

    private void buttonBoxSearchRemoteOnClick(View v) {
        call = new StgcheckBox().stgcheckGet(checkdate.getText().toString() + boxnum.getText().toString(), store.getText().toString());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                CheckGoodsActivity.this.runOnUiThread(() -> {
                    AlertDialog dialog = (
                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("(箱)查詢")
                                    .setMessage(e.toString())
                                    .setPositiveButton("確定", null))
                            .create();
                    dialog.show();
                    v.setEnabled(true);
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String msg = null;
                if (response == null) {
                    msg = "錯誤: 未知錯誤";
                } else {
                    Gson gson = new Gson();
                    String body = response.body().string();

                    try {
                        StgCheckBoxGetResponse res = gson.fromJson(body, StgCheckBoxGetResponse.class);
                        if (res.getStatus() != null && res.getStatus().matches("success")) {
                            msg = res.getData().toString();
                        } else {
                            msg = "錯誤:" + res.getErrorMessage();
                        }
                    } catch (IllegalStateException | JsonSyntaxException exception) {
                        Log.d("searchRes", exception.getMessage() + "\n" + body);
                        msg = "錯誤: json格式有誤(請聯絡開發人員)";
                    }
                }
                String finalMsg = msg;
                CheckGoodsActivity.this.runOnUiThread(() -> {
                    AlertDialog dialog = (
                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("(箱)查詢")
                                    .setMessage(finalMsg)
                                    .setPositiveButton("確定", null))
                            .create();
                    dialog.show();
                    v.setEnabled(true);
                });

            }
        });
    }

    private void buttonDeleteOnClick(View v) {
        call = new Stgcheck().stgcheckDelete(checkdate.getText().toString(), store.getText().toString());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                CheckGoodsActivity.this.runOnUiThread(() -> {
                    AlertDialog dialog = (
                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("刪除")
                                    .setMessage(e.toString())
                                    .setPositiveButton("確定", null))
                            .create();
                    dialog.show();
                    v.setEnabled(true);
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String msg;
                if (response == null) {
                    msg = "錯誤: 未知錯誤";
                } else {
                    Gson gson = new Gson();
                    String body = response.body().string();
                    StgCheckDeleteResponse res = gson.fromJson(body, StgCheckDeleteResponse.class);
                    if (res.getStatus() != null && res.getStatus().matches("success")) {
                        msg = "已刪除";
                    } else {
                        msg = "錯誤:" + res.getErrorMessage();
                    }
                }
                CheckGoodsActivity.this.runOnUiThread(() -> {
                    AlertDialog dialog = (
                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("刪除")
                                    .setMessage(msg)
                                    .setPositiveButton("確定", null))
                            .create();
                    dialog.show();
                    v.setEnabled(true);
                });
            }
        });
    }
    private void buttonDeleteAllOnClick(View v) {
        call = new StgcheckBox().stgcheckDeleteAll(checkdate.getText().toString(), store.getText().toString());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                CheckGoodsActivity.this.runOnUiThread(() -> {
                    AlertDialog dialog = (
                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("刪除")
                                    .setMessage(e.toString())
                                    .setPositiveButton("確定", null))
                            .create();
                    dialog.show();
                    v.setEnabled(true);
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String msg;
                if (response == null) {
                    msg = "錯誤: 未知錯誤";
                } else {
                    Gson gson = new Gson();
                    String body = response.body().string();
                    StgCheckDeleteResponse res = gson.fromJson(body, StgCheckDeleteResponse.class);
                    if (res.getStatus() != null && res.getStatus().matches("success")) {
                        msg = "已刪除";
                    } else {
                        msg = "錯誤:" + res.getErrorMessage();
                    }
                }
                CheckGoodsActivity.this.runOnUiThread(() -> {
                    AlertDialog dialog = (
                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("刪除")
                                    .setMessage(msg)
                                    .setPositiveButton("確定", null))
                            .create();
                    dialog.show();
                    v.setEnabled(true);
                });
            }
        });
    }
    private void buttonBoxDeleteOnClick(View v) {
        call = new StgcheckBox().stgcheckDelete(checkdate.getText().toString() + boxnum.getText().toString(), store.getText().toString());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                CheckGoodsActivity.this.runOnUiThread(() -> {
                    AlertDialog dialog = (
                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("(箱)刪除")
                                    .setMessage(e.toString())
                                    .setPositiveButton("確定", null))
                            .create();
                    dialog.show();
                    v.setEnabled(true);
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String msg;
                if (response == null) {
                    msg = "錯誤: 未知錯誤";
                } else {
                    Gson gson = new Gson();
                    String body = response.body().string();
                    StgCheckBoxDeleteResponse res = gson.fromJson(body, StgCheckBoxDeleteResponse.class);
                    if (res.getStatus() != null && res.getStatus().matches("success")) {
                        msg = "已刪除";
                    } else {
                        System.out.println(body);
                        msg = "錯誤:" + res.getErrorMessage();
                    }
                }
                CheckGoodsActivity.this.runOnUiThread(() -> {
                    AlertDialog dialog = (
                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("(箱)刪除")
                                    .setMessage(msg)
                                    .setPositiveButton("確定", null))
                            .create();
                    dialog.show();
                    v.setEnabled(true);
                });

            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    //barcode輸入完focus回barcode
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!s.toString().isEmpty()) {
            insertBarcode(
                    store.getText().toString(),
                    checkdate.getText().toString(),
                    boxnum.getText().toString(),
                    s.toString());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}