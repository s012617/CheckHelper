package tw.com.gbtech.checkhelper.Entity;

public class StgCheckBoxGet {
    String checkId;
    String checkStore;
    int totalQty;
    String chechFlag;

    public StgCheckBoxGet(String checkId, String checkStore, int totalQty, String chechFlag) {
        this.checkId = checkId;
        this.checkStore = checkStore;
        this.totalQty = totalQty;
        this.chechFlag = chechFlag;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
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

    public String getChechFlag() {
        return chechFlag;
    }

    public void setChechFlag(String chechFlag) {
        this.chechFlag = chechFlag;
    }

    @Override
    public String toString() {
        return  "盤點單號=" + checkId + '\n' +
                "庫點代號=" + checkStore + '\n' +
                "裝箱量=" + totalQty + '\n' +
                "轉換識別=" + chechFlag + '\n'+ '\n';
    }
}
