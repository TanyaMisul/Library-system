package Entites;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String login;
    private String password;
    private UserStatus status;
    private UserProfile profile;

    public User() {
    }

    public User(int id, String login, String password, UserStatus status, UserProfile profile) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.status = status;
        this.profile = profile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }
}
