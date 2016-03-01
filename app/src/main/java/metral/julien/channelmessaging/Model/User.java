package metral.julien.channelmessaging.Model;

import java.io.Serializable;

/**
 * Created by Julien on 02/02/2016.
 */
public class User implements Serializable {

    private String identifiant;
    private String password;
    private String token;
    private String username;
    private String imageUrl;

    public User(String identifiant, String password, String token) {
        this.identifiant = identifiant;
        this.password = password;
        this.token = token;
    }

    public User(){

    }

    public User(String identifiant, String password) {
        this.identifiant = identifiant;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "identifiant='" + identifiant + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                ", username='" + username + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
