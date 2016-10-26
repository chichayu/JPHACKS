package entity;

import android.provider.ContactsContract;
import android.widget.ImageView;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by xixi on 10/20/16.
 */
public class Baggage implements Serializable {

    /*
    json data format
    {
    "company": "",
    "name": "",
    "date": ""
    }
     */

    @SerializedName("company")
    String company;

    @SerializedName("product")
    String name;

    @SerializedName("date")
    String date;

    String starttime;
    String endtime;

    public Baggage() {
    }

    public Baggage(String company, String name, String date, String starttime, String endtime) {
        this.company = company;
        this.name = name;
        this.date = date;
        this.starttime = starttime;
        this.endtime = endtime;
    }

    public String getName() {
        return name;
    }

    public void setName(String commodity) {
        this.name = commodity;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStarttime() {
        return starttime;
    }

    public String getEndtime() {
        return endtime;
    }

}

