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

    String companyflag;
    String deliveryname;
    String scheduledday;
    String scheduletime;

    Baggage(String companyflag, String deliveryname, String scheduledday, String scheduletime) {
        this.companyflag = companyflag;
        this.deliveryname = deliveryname;
        this.scheduledday = scheduledday;
        this.scheduletime = scheduletime;
    }

    public String getCompanyflag() {
        return companyflag;
    }

    public String getDeliveryname() {
        return deliveryname;
    }

    public String getScheduledday() {
        return scheduledday;
    }

    public String getScheduletime() {
        return scheduletime;
    }

}

