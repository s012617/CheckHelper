package tw.com.gbtech.checkhelper.checkTotal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import tw.com.gbtech.checkhelper.FunctionMenuActivity;
import tw.com.gbtech.checkhelper.checkGoods.CheckGoodsActivity;
import tw.com.gbtech.checkhelper.Entity.Check;
import tw.com.gbtech.checkhelper.Util.SwipeHelper;
import tw.com.gbtech.checkhelper.Util.Today;
import tw.com.gbtech.checkhelper.db.DBHelper;
import tw.com.gbtech.checkhelper.Entity.TotalCheck;
import tw.com.gbtech.checkhelper.Entity.TotalCheckForView;
import tw.com.gbtech.checkhelper.R;

// 顯示在本機資料庫的盤點資料

public class TotalInfoActivity extends AppCompatActivity {
    List<TotalCheck> totalChecks;
    TotalInfoAdapter totalInfoAdapter;
    RecyclerView recyclerViewTotalCheck;
    FrameLayout frameLayoutWholeBox;
    Button buttonGoToCheckGoods;
    boolean showBoxActive = false;
    List<TotalCheckForView> totalCheckForViewList;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate");
        setContentView(R.layout.activity_total_info);

        frameLayoutWholeBox = findViewById(R.id.frameLayoutWholeBox);
        recyclerViewTotalCheck = findViewById(R.id.recyclerViewTotalCheck);
        buttonGoToCheckGoods = findViewById(R.id.buttonGoToCheckGoods);
        dbHelper = new DBHelper(getApplicationContext());

        buttonGoToCheckGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToCheckGoods(v);
            }
        });


        totalChecks = dbHelper.getAppDatabase().checkDao().getTotalCheck();
        totalCheckForViewList = new ArrayList<TotalCheckForView>();
        for (TotalCheck dataSet:totalChecks
             ) {
            totalCheckForViewList.add(new TotalCheckForView(
                    dataSet.getStore(),dataSet.getCheckDate(),dataSet.getQuantity(),dbHelper.getAppDatabase().checkDao().getTotalBoxCheck(dataSet.getStore(),dataSet.getCheckDate()))
            );
        }
///

        recyclerViewTotalCheck.setLayoutManager(new LinearLayoutManager(this));
        totalInfoAdapter = new TotalInfoAdapter(totalCheckForViewList);
        recyclerViewTotalCheck.setAdapter(totalInfoAdapter);

        //  實現拖移、左右滑動刪除的效果
        SwipeHelper swipeHelper = new SwipeHelper(this,recyclerViewTotalCheck){
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton("刪除",0, getResources().getColor(R.color.delete,viewHolder.itemView.getContext().getTheme()), new UnderlayButtonClickListener() {
                    @Override
                    public void onClick(int pos) {
                        System.out.println(pos);
                        dbHelper.getAppDatabase().checkDao().deleteByStoreAndDate(
                                totalCheckForViewList.get(pos).getStore(),totalCheckForViewList.get(pos).getDate());
                        totalCheckForViewList.remove(pos);
                        recyclerViewTotalCheck.getAdapter().notifyDataSetChanged();

                        totalInfoAdapter = new TotalInfoAdapter(totalCheckForViewList);
                        recyclerViewTotalCheck.setAdapter(totalInfoAdapter);
                    }
                }));
            }
        };
    }
    @Override
    public void onResume() {
        super.onResume();
        buttonGoToCheckGoods.setEnabled(true);
        totalChecks = dbHelper.getAppDatabase().checkDao().getTotalCheck();
        totalCheckForViewList = new ArrayList<TotalCheckForView>();
        for (TotalCheck dataSet:totalChecks
        ) {
            totalCheckForViewList.add(new TotalCheckForView(
                    dataSet.getStore(),dataSet.getCheckDate(),dataSet.getQuantity(),dbHelper.getAppDatabase().checkDao().getTotalBoxCheck(dataSet.getStore(),dataSet.getCheckDate()))
            );
        }
///

        recyclerViewTotalCheck.setLayoutManager(new LinearLayoutManager(this));
        totalInfoAdapter = new TotalInfoAdapter(totalCheckForViewList);
        recyclerViewTotalCheck.setAdapter(totalInfoAdapter);


    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    void GoToCheckGoods(View v){
        Button button = findViewById(R.id.buttonGoToCheckGoods);
        button.setEnabled(false);
        Intent intent = new Intent();
        intent.setClass(this, CheckGoodsActivity.class);
        startActivity(intent);
        //TotalInfoActivity.this.finish();
    }
}