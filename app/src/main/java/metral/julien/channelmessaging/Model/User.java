package metral.julien.channelmessaging.Model;

import java.io.Serializable;

/**
 * Created by Julien on 02/02/2016.
 */
public class User implements Serializable {

    private String identifiant;
    private String password;
    private String token;

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
        return token;
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
}
