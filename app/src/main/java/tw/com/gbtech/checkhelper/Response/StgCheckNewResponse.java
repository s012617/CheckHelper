package tw.com.gbtech.checkhelper.Response;

import java.util.List;

import tw.com.gbtech.checkhelper.Entity.StgCheckNew;

public class StgCheckNewResponse {
    String status;
    String errorMessage;
    List<StgCheckNew> errorData;

    public StgCheckNewResponse(String status, String errorMessage, List<StgCheckNew> errorData) {
        this.status = status;
        this.errorMessage = errorMessage;
        this.errorData = errorData;
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

    public List<StgCheckNew> getErrorData() {
        return errorData;
    }

    public void setErrorData(List<StgCheckNew> errorData) {
        this.errorData = errorData;
    }
}
