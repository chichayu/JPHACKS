package entity;

/**
 * Created by xixi on 10/29/16.
 */
public class TimeRequest {
    String flag;
    String username;
    String password;
    String scheduleflag;
    String starttime;
    String endtime;

    public TimeRequest(String username, String password, String scheduleflag, String starttime, String endtime) {
        this.flag = "ST";
        this.username = username;
        this.password = password;
        this.scheduleflag = scheduleflag;
        this.starttime = starttime;
        this.endtime = endtime;
    }
}
