package tw.com.gbtech.checkhelper.Response;

public class Response {
    String status = null;
    String errorMessage = null;

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

    public Response(String status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }
}
