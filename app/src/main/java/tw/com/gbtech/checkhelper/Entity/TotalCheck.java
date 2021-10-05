package tw.com.gbtech.checkhelper.Entity;

import java.util.List;

public class TotalCheck {
    String store;
    String checkDate;
    String quantity;

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "TotalCheck{" +
                "store='" + store + '\'' +
                ", checkDate='" + checkDate + '\'' +
                ", quantity='" + quantity + '\'' +
                '}';
    }
}
