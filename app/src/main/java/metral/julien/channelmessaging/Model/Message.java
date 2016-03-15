package metral.julien.channelmessaging.Model;

import java.io.Serializable;

/**
 * Created by Julien on 08/02/2016.
 */
public class Message implements Serializable {

    private Integer userID;
    private String username;
    private String message;
    private String date;
    private String imageUrl;
    private Double latitude;
    private Double longitude;
    private String messageImageUrl;
    private String soundUrl;

    public Message(Integer userID, String username, String message, String date, String imageUrl, Double latitude, Double longitude, String messageImageUrl, String soundUrl) {
        this.userID = userID;
        this.username = username;
        this.message = message;
        this.date = date;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.messageImageUrl = messageImageUrl;
        this.soundUrl = soundUrl;
    }

    public Message() {
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getMessageImageUrl() {
        return messageImageUrl;
    }

    public void setMessageImageUrl(String messageImageUrl) {
        this.messageImageUrl = messageImageUrl;
    }

    public String getSoundUrl() {
        return soundUrl;
    }

    public void setSoundUrl(String soundUrl) {
        this.soundUrl = soundUrl;
    }

    @Override
    public String toString() {
        return "Message{" +
                "userID=" + userID +
                ", username='" + username + '\'' +
                ", message='" + message + '\'' +
                ", date='" + date + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", messageImageUrl='" + messageImageUrl + '\'' +
                ", soundUrl='" + soundUrl + '\'' +
                '}';
    }
}
