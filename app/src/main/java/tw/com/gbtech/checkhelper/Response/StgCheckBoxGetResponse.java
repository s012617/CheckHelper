package tw.com.gbtech.checkhelper.Response;

import java.util.List;

import tw.com.gbtech.checkhelper.Entity.StgCheckBoxGet;

public class StgCheckBoxGetResponse {
    String status;
    String errorMessage;
    StgCheckBoxGet data;

    public StgCheckBoxGetResponse(String status, String errorMessage, StgCheckBoxGet data) {
        this.status = status;
        this.errorMessage = errorMessage;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public StgCheckBoxGet getData() {
        return data;
    }

    public void setData(StgCheckBoxGet data) {
        this.data = data;
    }
}
