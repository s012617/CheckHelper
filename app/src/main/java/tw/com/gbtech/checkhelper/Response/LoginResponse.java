package tw.com.gbtech.checkhelper.Response;

public class LoginResponse {
    String token;
    String status;
    String errorMessage;

    public LoginResponse(String token, String status, String errorMessage) {
        this.token = token;
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
}
