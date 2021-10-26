package tw.com.gbtech.checkhelper.Entity;

public class StgCheckBoxNew {
    String barCode;
    int qty;

    public StgCheckBoxNew(String barCode, int qty) {
        this.barCode = barCode;
        this.qty = qty;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return "(" +
                "條碼 = " + barCode + ' ' +
                ')'+'\n';
    }
}
