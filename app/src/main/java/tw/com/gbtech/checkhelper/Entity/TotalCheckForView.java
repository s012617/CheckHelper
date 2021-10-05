package tw.com.gbtech.checkhelper.Entity;

import java.util.List;

public class TotalCheckForView {
    String store;
    String date;
    String totalQuantity;
    List<TotalBoxCheck> totalBoxCheck;

    public TotalCheckForView(String store, String date, String totalQuantity, List<TotalBoxCheck> totalBoxCheck) {
        this.store = store;
        this.date = date;
        this.totalQuantity = totalQuantity;
        this.totalBoxCheck = totalBoxCheck;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public List<TotalBoxCheck> getTotalBoxCheck() {
        return totalBoxCheck;
    }

    public void setTotalBoxCheck(List<TotalBoxCheck> totalBoxCheck) {
        this.totalBoxCheck = totalBoxCheck;
    }

    @Override
    public String toString() {
        return "TotalCheckForView{" +
                "store='" + store + '\'' +
                ", date='" + date + '\'' +
                ", totalQuantity='" + totalQuantity + '\'' +
                ", totalBoxCheck=" + totalBoxCheck +
                '}';
    }
}
