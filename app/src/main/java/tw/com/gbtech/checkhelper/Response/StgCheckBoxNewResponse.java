package tw.com.gbtech.checkhelper.Response;

import java.util.List;

import tw.com.gbtech.checkhelper.Entity.StgCheckBoxNew;

public class StgCheckBoxNewResponse {
    String status;
    String errorMessage;
    List<StgCheckBoxNew> errorData;

    public StgCheckBoxNewResponse(String status, String errorMessage, List<StgCheckBoxNew> errorData) {
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

    public List<StgCheckBoxNew> getErrorData() {
        return errorData;
    }

    public void setErrorData(List<StgCheckBoxNew> errorData) {
        this.errorData = errorData;
    }
}
