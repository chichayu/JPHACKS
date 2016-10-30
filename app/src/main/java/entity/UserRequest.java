package entity;

/**
 * Created by xixi on 10/29/16.
 */
public class UserRequest {
    String flag;
    String name;
    String password;

    public UserRequest() {}

    public UserRequest (String name, String password) {
        this.flag = "SR";
        this.name = name;
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}