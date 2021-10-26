package tw.com.gbtech.checkhelper.checkGoods;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tw.com.gbtech.checkhelper.Entity.CheckBoxNum;
import tw.com.gbtech.checkhelper.Entity.CheckWithFlag;
import tw.com.gbtech.checkhelper.R;

public class CheckGoodsAdapter extends RecyclerView.Adapter<CheckGoodsAdapter.ViewHolder>  {
    List<CheckBoxNum> checkBoxNums;
    ViewGroup parent;
    View.OnClickListener DeleteOnClickListener,MinusOnClickListener;
    public CheckGoodsAdapter(List<CheckBoxNum> checkBoxNums, View.OnClickListener DeleteOnClickListener,View.OnClickListener MinusOnClickListener) {
        this.checkBoxNums = checkBoxNums;
        this.DeleteOnClickListener =DeleteOnClickListener;
        this.MinusOnClickListener = MinusOnClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.check_goods_recylerview, parent,false);
        this.parent = parent;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getTextViewBoxNum().setText(checkBoxNums.get(position).getBoxnum());
        holder.getLinearLayoutCheckContent().removeAllViews();

        for (CheckWithFlag checks: checkBoxNums.get(position).getChecks()) {
            View view = LayoutInflater.from(holder.itemView.getContext()).inflate(R.layout.swipe,parent.findViewById(R.id.recyclerviewCheckContent),false);
            TextView textViewBarcode, textViewQuantity, textViewFlag;
            textViewBarcode = view.findViewById(R.id.textViewBarcode);
            textViewQuantity = view.findViewById(R.id.textViewGoodQuantity);
            textViewFlag = view.findViewById(R.id.textViewFlag);

            textViewBarcode.setText(checks.getCheck().barcode);
            textViewQuantity.setText(Integer.toString(checks.getCheck().quantity));
            textViewFlag.setText((checks.getFlag()==1)?"錯誤":"");
            textViewBarcode.setTextColor(Color.BLACK);
            textViewQuantity.setTextColor(Color.BLACK);
            textViewFlag.setTextColor(Color.RED);
            textViewBarcode.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,1.0f));
            textViewQuantity.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,1.0f));
            ViewParent viewParent = view.getParent();

            if(viewParent instanceof ViewGroup){
                ((ViewGroup) viewParent).removeView(view);
            }
            Button buttonDelete = view.findViewById(R.id.buttonDelete);
            Button buttonMinus = view.findViewById(R.id.buttonMinus);
            buttonDelete.setTag(checks);

            buttonMinus.setTag(checks);
            buttonDelete.setOnClickListener(DeleteOnClickListener);
            buttonMinus.setOnClickListener(MinusOnClickListener);


            holder.getLinearLayoutCheckContent().addView(view);
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
