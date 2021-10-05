package tw.com.gbtech.checkhelper.Entity;

import java.util.List;
import java.util.Map;

public class CheckBoxNum {
    public String boxnum;
    List<CheckWithFlag> checks;

    public CheckBoxNum(String boxnum, List<CheckWithFlag> checks) {
        this.boxnum = boxnum;
        this.checks = checks;
    }

    public String getBoxnum() {
        return boxnum;
    }

    public void setBoxnum(String boxnum) {
        this.boxnum = boxnum;
    }

    public List<CheckWithFlag> getChecks() {
        return checks;
    }

    public void setChecks(List<CheckWithFlag> checks) {
        this.checks = checks;
    }

    @Override
    public String toString() {
        return "CheckBoxNum{" +
                "boxnum='" + boxnum + '\'' +
                ", checks=" + checks.toString() +
                '}';
    }
}
