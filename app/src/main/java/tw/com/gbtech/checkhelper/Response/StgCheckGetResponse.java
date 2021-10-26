package tw.com.gbtech.checkhelper.Response;

import java.util.List;

import tw.com.gbtech.checkhelper.Entity.StgCheckGet;

public class StgCheckGetResponse {
    String status;
    String errorMessage;
    StgCheckGet data;

    public StgCheckGetResponse(String status, String errorMessage, StgCheckGet data) {
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

    public StgCheckGet getData() {
        return data;
    }

    public void setData(StgCheckGet data) {
        this.data = data;
    }
}
