package tw.com.gbtech.checkhelper.CheckGoods;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;

import java.util.List;

import tw.com.gbtech.checkhelper.Entity.CheckBoxNum;
import tw.com.gbtech.checkhelper.Entity.CheckWithFlag;
import tw.com.gbtech.checkhelper.LoginActivity;
import tw.com.gbtech.checkhelper.R;

public class CheckGoodsAdapter extends RecyclerView.Adapter<CheckGoodsAdapter.ViewHolder>  {
    List<CheckBoxNum> checkBoxNums;

    //
    static final String logTag = "ActivitySwipeDetector";
    static final int MIN_DISTANCE = 100;// TODO change this runtime based on screen resolution. for 1920x1080 is to small the 100 distance
    private float downX, downY, upX, upY;

    public CheckGoodsAdapter(List<CheckBoxNum> checkBoxNums, Activity activity) {
        this.checkBoxNums = checkBoxNums;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.check_goods_recylerview, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        

        holder.getTextViewBoxNum().setText(checkBoxNums.get(position).getBoxnum());
        holder.getLinearLayoutCheckContent().removeAllViews();


        for (CheckWithFlag checks: checkBoxNums.get(position).getChecks()) {
            SwipeRevealLayout swipeRevealLayout = new SwipeRevealLayout(holder.itemView.getContext());

            FrameLayout frameLayout = new FrameLayout(holder.itemView.getContext());
            Button buttonDelete = new Button(holder.itemView.getContext());
            buttonDelete.setText("刪除");
            frameLayout.addView(buttonDelete);

            LinearLayout linearLayout = new LinearLayout(holder.itemView.getContext());
            linearLayout.setId(R.id.rows);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            p.setMargins(0,10,0,10);
            linearLayout.setLayoutParams(p);

            TextView textViewBarcode, textViewQuantity, textViewFlag;
            textViewBarcode = new TextView(holder.itemView.getContext());
            textViewQuantity = new TextView(holder.itemView.getContext());
            textViewFlag = new TextView(holder.itemView.getContext());



            linearLayout.addView(textViewBarcode);
            linearLayout.addView(textViewQuantity);
            linearLayout.addView(textViewFlag);
            textViewBarcode.setText(checks.getCheck().barcode);
            textViewQuantity.setText(Integer.toString(checks.getCheck().quantity));
            textViewFlag.setText((checks.getFlag()==1)?"錯誤":"");
            textViewFlag.setWidth(48);
            textViewFlag.setTextColor(Color.RED);
            textViewBarcode.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,1.0f));
            textViewQuantity.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,1.0f));


            swipeRevealLayout.addView(frameLayout);
            swipeRevealLayout.addView(linearLayout);
            holder.getLinearLayoutCheckContent().addView(swipeRevealLayout);
        }


    }

    @Override
    public int getItemCount() {
        return checkBoxNums.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayoutCheckContent;
        TextView textViewBoxNum;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayoutCheckContent = itemView.findViewById(R.id.linearLayoutCheckContent);
            textViewBoxNum = itemView.findViewById(R.id.textViewBoxNum);
        }

        public LinearLayout getLinearLayoutCheckContent() {
            return linearLayoutCheckContent;
        }

        public TextView getTextViewBoxNum() {
            return textViewBoxNum;
        }
    }
}
