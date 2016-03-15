package metral.julien.channelmessaging.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Julien on 08/02/2016.
 */
public class Response implements Serializable {

    private String response;

    private Integer code;

    @SerializedName("accesstoken")
    private String accessToken;

    public Response() {
    }

    public Response(String response, Integer code, String accessToken) {
        this.response = response;
        this.code = code;
        this.accessToken = accessToken;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return "Response{" +
                "response='" + response + '\'' +
                ", code=" + code +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }
}
