package metral.julien.channelmessaging.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Julien on 08/02/2016.
 */
public class Channel implements Serializable {
    private Integer channelID;
    private String name;
    @SerializedName("connectedusers")
    private Integer connectedUsers;

    public Channel(Integer channelID, String name, Integer connectedUsers) {
        this.channelID = channelID;
        this.name = name;
        this.connectedUsers = connectedUsers;
    }

    public Channel() {
    }

    public Integer getChannelID() {
        return channelID;
    }

    public void setChannelID(Integer channelID) {
        this.channelID = channelID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getConnectedUsers() {
        return connectedUsers;
    }

    public void setConnectedUsers(Integer connectedUsers) {
        this.connectedUsers = connectedUsers;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "channelID=" + channelID +
                ", name='" + name + '\'' +
                ", connectedUsers=" + connectedUsers +
                '}';
    }
}
