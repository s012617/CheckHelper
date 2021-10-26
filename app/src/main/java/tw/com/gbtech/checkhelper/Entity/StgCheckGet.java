package tw.com.gbtech.checkhelper.Entity;

public class StgCheckGet {
    String checkDate;
    String checkStore;
    int totalQty;
    String checkFlag;

    public StgCheckGet(String checkDate, String checkStore, int totalQty, String checkFlag) {
        this.checkDate = checkDate;
        this.checkStore = checkStore;
        this.totalQty = totalQty;
        this.checkFlag = checkFlag;
    }

    public String getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
    }

    public String getCheckStore() {
        return checkStore;
    }

    public void setCheckStore(String checkStore) {
        this.checkStore = checkStore;
    }

    public int getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(int totalQty) {
        this.totalQty = totalQty;
    }

    public String getCheckFlag() {
        return checkFlag;
    }

    public void setCheckFlag(String checkFlag) {
        this.checkFlag = checkFlag;
    }

    @Override
    public String toString() {
        return
                "盤點日期 = " + checkDate + '\n' +
                "庫點代號 = " + checkStore + '\n' +
                "盤點總量 = " + totalQty + '\n' +
                "過帳識別 = " + checkFlag + '\n' + '\n';
    }
}
