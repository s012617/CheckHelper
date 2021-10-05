package tw.com.gbtech.checkhelper.Entity;

public class TotalBoxCheck {
    String boxNum;
    int quantity;

    public TotalBoxCheck(String boxNum, int quantity) {
        this.boxNum = boxNum;
        this.quantity = quantity;
    }

    public String getBoxNum() {
        return boxNum;
    }

    public void setBoxNum(String boxNum) {
        this.boxNum = boxNum;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "TotalBoxCheck{" +
                "boxNum='" + boxNum + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
