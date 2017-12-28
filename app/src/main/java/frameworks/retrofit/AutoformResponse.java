package frameworks.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class AutoformResponse<T> {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("http_code")
    @Expose
    private Integer httpCode;
    @SerializedName("resource")
    @Expose
    private String resource;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private T data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(Integer httpCode) {
        this.httpCode = httpCode;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}