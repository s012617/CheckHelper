package tw.com.gbtech.checkhelper.checkTotal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tw.com.gbtech.checkhelper.Entity.TotalBoxCheck;
import tw.com.gbtech.checkhelper.Entity.TotalCheckForView;
import tw.com.gbtech.checkhelper.R;

public class TotalInfoAdapter extends RecyclerView.Adapter<TotalInfoAdapter.ViewHolder> {
    List<TotalCheckForView> checkDataSet;
    public TotalInfoAdapter(List<TotalCheckForView> checkDataSet) {
        this.checkDataSet = checkDataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.total_info_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.getTextViewStore().setText(checkDataSet.get(position).getStore());
        holder.getTextViewDate().setText(checkDataSet.get(position).getDate());
        holder.getTextViewQuantity().setText(checkDataSet.get(position).getTotalQuantity());

        LinearLayout linearLayoutOuter = new LinearLayout(holder.itemView.getContext());
        linearLayoutOuter.setOrientation(LinearLayout.VERTICAL);

        for (TotalBoxCheck dataset:checkDataSet.get(position).getTotalBoxCheck()) {
            LinearLayout linearLayout;
            TextView textViewBoxNum,textViewQuantity;
            linearLayout = new LinearLayout(holder.itemView.getContext());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            textViewBoxNum = new TextView(holder.itemView.getContext());
            textViewQuantity = new TextView(holder.itemView.getContext());
            linearLayout.addView(textViewBoxNum);
            linearLayout.addView(textViewQuantity);
            textViewBoxNum.setText(dataset.getBoxNum());
            textViewQuantity.setText(Integer.toString(dataset.getQuantity()));
            textViewBoxNum.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,1.0f));
            textViewQuantity.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,1.0f));
            linearLayout.setBackgroundColor(0xFFF2F4F4);
            linearLayoutOuter.addView(linearLayout);
        }
        if(holder.getFrameLayout().getChildCount()>0)
            holder.getFrameLayout().removeViewAt(0);
        holder.getFrameLayout().addView(linearLayoutOuter);

    }

    @Override
    public int getItemCount() {
        return checkDataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewStore;
        TextView textViewDate;
        TextView textViewQuantity;
        LinearLayout linearLayoutInner;
        FrameLayout frameLayout;
        public ViewHolder(View view){
            super(view);

            textViewStore = view.findViewById(R.id.textViewStore);
            textViewDate = view.findViewById(R.id.textViewDate);
            textViewQuantity = view.findViewById(R.id.textViewQuantity);
            linearLayoutInner = view.findViewById(R.id.linerLayoutInner);
            frameLayout = view.findViewById(R.id.frameLayoutBox);
        }

        public TextView getTextViewStore() {
            return textViewStore;
        }

        public TextView getTextViewDate() {
            return textViewDate;
        }

        public TextView getTextViewQuantity() {
            return textViewQuantity;
        }

        public FrameLayout getFrameLayout() {
            return frameLayout;
        }
    }
}
