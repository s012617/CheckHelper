package tw.com.gbtech.checkhelper.Entity;

public class CheckWithFlag {
    Check check;
    int flag = 0; //0 = 正常 1 = 有誤

    public CheckWithFlag(Check check, int flag) {
        this.check = check;
        this.flag = flag;
    }

    public Check getCheck() {
        return check;
    }

    public void setCheck(Check check) {
        this.check = check;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "CheckWithFlag{" +
                "check=" + check.toString() +
                ", flag=" + flag +
                '}';
    }
}
