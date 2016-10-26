package jp.ac.u_tokyo.jphacks;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import adapter.GridAdapter;
import entity.Baggage;
import picker.TimePickerDialogFragment;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final int[] iconItems = {
            android.R.drawable.ic_yamato,
            android.R.drawable.ic_sagawa,
            android.R.drawable.ic_nihon
    };

    ArrayList<Baggage> bagList = new ArrayList<Baggage>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setTitle("メインページ");

        // GridViewの設定
        GridView gridview = (GridView) findViewById(R.id.gridView);
        GridAdapter adapter = new GridAdapter(this.getApplicationContext(), R.layout.grid_items, bagList);
        gridview.setAdapter(adapter);
        gridview.getView();

        // データの取得
        // データの読み込み方法

    static final String readLinesText(InputStream is) throws IOException {
        final StringBuilder str = new StringBuilder();
        final String sep = System.getProperty("line.separator");  //改行コード
        final BufferedReader br = new BufferedReader(new InputStreamReader(is));
        try {
            while (br.ready()) {
                str.append(br.readLine());  //終端文字は含まない
                str.append(sep);  //改行コード追加
            }
            return str.toString();
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                //IOException
            }
            str.setLength(0);
        }
    }

    // サーバーとの接続
    final int CONNECT_TIMEOUT = 10 * 1000;  //接続タイムアウト[ms]
    final int READ_TIMEOUT = 5 * 1000;  //読み取りタイムアウト[ms]
    final String ENCORDING = "UTF-8";  //loadText() 用

    String phpUrl = "http://www.jigenji.biz/json/json_out1b.php"; //JSON 送信用 PHPのURL

    private void connectWeb() {
        try {
            URL url = new URL(phpUrl);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(CONNECT_TIMEOUT);  //接続タイムアウト
            con.setReadTimeout(READ_TIMEOUT);  //読み取りタイムアウト
            con.setRequestMethod("GET");
            con.connect();

            String text = readLinesText(con.getInputStream());

            con.disconnect();
            //System.out.println(text3);

            //JSON 解析して、データ用クラスに代入
            Gson gson = new Gson();
            Baggage bags = gson.fromJson(text, Baggage.class);
            bagList.add(bags);
            //System.out.println(bags.getCompany());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


        /*
        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            // アイコンタップでTextViewに拡大表示する
            textView.setText(il.getName(position));
        }
        */

    // 配達時間の指定
    final RadioGroup lmdradiogroup = (RadioGroup) findViewById(R.id.radiogroup);

    // 送信ボタン
    Button btn_submit = (Button) findViewById(R.id.button);
    btn_submit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick (View v){

        int checkedId = lmdradiogroup.getCheckedRadioButtonId();
        RadioButton lmdradiobtn = (RadioButton) findViewById(checkedId);
        String lmd = lmdradiobtn.getText().toString();
        if (lmd == "no") {
            //make everything gray
            //say no to the companies
        } else {
            //get time
            //send time to the companies
            // 1 or 0 with error code
            //and recolor the items according to the time
        }

        //print "Done" if get 1 from the server
    });

    //I dont want to do any time-picking-thing...
    public void TimePicker(View v) {
        final Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        final TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Toast.makeText(MainActivity.this, hourOfDay + "時" + minute + "分", Toast.LENGTH_LONG).show();

                    }
                }, hour, minute, true);
        timePickerDialog.show();
    }
}



/*set background colors
        static ArrayList displayColor(ArrayList list) {
            Date today = get Today
            for (int i = 0; i < list.size(); i++) {
                if (list[i][2] == "yamato") {
                    if (list[i][1] == today) {
                        //set the background color dark green
                    } else {
                        //set the background color light green
                    }
                } else if (list[i][2] == "nihon") {
                    if (list[i][1] == today) {
                        //set the background color dark red
                    } else {
                        //set the background color light red
                } else if (list[i][2] == "sagawa") {
                    if (list[i][1] == today) {
                        //set the background color dark blue
                    } else {
                        //set the background color light blue
            }
        }
        */