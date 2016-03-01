package metral.julien.channelmessaging.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Julien on 01/03/2016.
 */
public class PrivateMessage implements Serializable{

    private Integer userID;
    @SerializedName("sendbyme")
    private Integer sendByMe;
    private String username;
    private String message;
    private String date;
    private String imageUrl;
    private Integer everRead;

    public PrivateMessage(Integer userID, Integer sendbyme, String username, String message, String date, String imageUrl, Integer everRead) {
        this.userID = userID;
        this.sendByMe = sendbyme;
        this.username = username;
        this.message = message;
        this.date = date;
        this.imageUrl = imageUrl;
        this.everRead = everRead;
    }

    public PrivateMessage() {
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getSendbyme() {
        return sendByMe;
    }

    public void setSendbyme(Integer sendbyme) {
        this.sendByMe = sendbyme;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getEverRead() {
        return everRead;
    }

    public void setEverRead(Integer everRead) {
        this.everRead = everRead;
    }
}
