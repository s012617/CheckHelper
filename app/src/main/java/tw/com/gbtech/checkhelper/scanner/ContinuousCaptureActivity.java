 package tw.com.gbtech.checkhelper.scanner;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageCapture;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.security.Permission;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import tw.com.gbtech.checkhelper.Entity.Check;
import tw.com.gbtech.checkhelper.Entity.CheckBoxNum;
import tw.com.gbtech.checkhelper.R;
import tw.com.gbtech.checkhelper.checkGoods.CheckGoodsActivity;
import tw.com.gbtech.checkhelper.db.DBHelper;

 /**
  * This sample performs continuous scanning, displaying the barcode and source image whenever
  * a barcode is scanned.
  */
 public class ContinuousCaptureActivity extends AppCompatActivity {
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;
    private TextView textViewScanMessage;
    private String boxnum;
    private String checkDate;
    private String store;
    private static boolean writed = false;
    private Vibrator vibrator;
    private boolean firstTime = true;
    private Switch switchFlashlight;
     private boolean flashLightStatus = false;
    DBHelper dbHelper;
    // private Vibrator vibrator =(Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.continuous_scan);

        switchFlashlight = findViewById(R.id.switchFlashlight);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ContinuousCaptureActivity.this, new String[] { Manifest.permission.CAMERA },101);
        }

        dbHelper = new DBHelper(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            boxnum = bundle.getString("boxnum");
            checkDate = bundle.getString("checkDate");
            store = bundle.getString("store");
        }
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39,BarcodeFormat.CODE_128,BarcodeFormat.CODE_93);
        barcodeView = findViewById(R.id.barcode_scanner);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory());

        beepManager = new BeepManager(this);
        textViewScanMessage = findViewById(R.id.textViewScanMessage);
        vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);

        switchFlashlight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked){
                   barcodeView.setTorchOn();
               }else{
                   barcodeView.setTorchOff();
               }
            }
        });
    }

     private BarcodeCallback callback = new BarcodeCallback() {
         @Override
         public void barcodeResult(BarcodeResult result) {
             System.out.println("barcodeResult"+((result==null)?null:result.getText()));
             if(result.getText() == null || writed == true) {
                 // Prevent duplicate scans
                 return;
             }
             writed = true;
             lastText = result.getText();
             barcodeView.setStatusText(lastText);
             beepManager.playBeepSoundAndVibrate();
             barcodeView.setCameraDistance((float)1.87);
             vibrator.vibrate(200);
             //Added preview of scanned barcode
             ImageView imageView = findViewById(R.id.barcodePreview);
             imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));
             String barcode = result.getText();

             ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
             tg.startTone(ToneGenerator.TONE_PROP_BEEP);


             List<Check> checks = dbHelper.getAppDatabase().checkDao().getCheck(store,checkDate,boxnum,barcode);

            if(checks.size()>0){
                int quan = checks.get(0).quantity+1;
                dbHelper.getAppDatabase().checkDao().update(quan,store,checkDate,boxnum,barcode);
                int sum = dbHelper.getAppDatabase().checkDao().getBoxCount(store,checkDate,boxnum);
                textViewScanMessage.setText("箱("+boxnum+")總數: "+sum+", 數量: " +quan);

            }else{
                dbHelper.getAppDatabase().checkDao().insertAll(new Check(barcode,boxnum,checkDate,store,1,null));
                int sum = dbHelper.getAppDatabase().checkDao().getBoxCount(store,checkDate,boxnum);
                textViewScanMessage.setText("箱("+boxnum+")總數: "+sum+", 數量: " +1);
            }
             onPause();
             ContinuousCaptureActivity.this.runOnUiThread(() -> {
                findViewById(R.id.buttonScan).setEnabled(true);

             });
         }

         @Override
         public void possibleResultPoints(List<ResultPoint> resultPoints) {
         }
     };





     @Override
     protected void onResume() {
         super.onResume();
         barcodeView.resume();
     }

     @Override
     protected void onPause() {
         super.onPause();

         barcodeView.pause();
     }

     public void pause(View view) {
         barcodeView.pause();
     }

     public void resume(View view) {
         if(firstTime){
             findViewById(R.id.textViewHint).setVisibility(View.INVISIBLE);
             barcodeView.initializeFromIntent(getIntent());
             barcodeView.decodeContinuous(callback);
             firstTime = false;
         }

         view.setEnabled(false);
         writed = false;
         barcodeView.resume();
     }

     public void triggerScan(View view) {
         barcodeView.decodeSingle(callback);
     }

     @Override
     public boolean onKeyDown(int keyCode, KeyEvent event) {
         return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
     }

 }
